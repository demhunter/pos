/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service.impl;

import com.pos.user.constant.UserType;
import com.pos.user.dao.*;
import com.pos.user.domain.*;
import com.pos.user.dto.LoginInfoDto;
import com.pos.user.dto.UserRegConfirmDto;
import com.pos.user.dto.customer.CustomerDto;
import com.pos.user.dto.employee.EmployeeDto;
import com.pos.user.dto.manager.ManagerDto;
import com.pos.user.service.RegisterService;
import com.pos.basic.mq.MQMessage;
import com.pos.basic.mq.MQReceiverType;
import com.pos.basic.mq.MQTemplate;
import com.pos.common.sms.constant.MemcachedPrefixType;
import com.pos.common.sms.service.SmsService;
import com.pos.common.util.basic.AddressUtils;
import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.cache.MemcachedClientUtils;
import com.pos.common.util.exception.CommonErrorCode;
import com.pos.common.util.exception.ErrorCode;
import com.pos.common.util.exception.ValidationException;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.security.MD5Utils;
import com.pos.common.util.validation.FieldChecker;
import com.pos.common.util.validation.Preconditions;
import com.pos.common.util.validation.Validator;
import com.pos.user.constant.CustomerType;
import com.pos.user.constant.EmployeeType;
import com.pos.user.dto.IdentityInfoDto;
import com.pos.user.dto.converter.UserDtoConverter;
import com.pos.user.dto.merchant.MerchantDto;
import com.pos.user.dto.mq.CustomerInfoMsg;
import com.pos.user.exception.UserErrorCode;
import com.pos.user.service.support.UserServiceSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户注册服务实现类
 *
 * @author cc
 * @version 1.0, 2016/6/8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterServiceImpl implements RegisterService {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Resource
    private UserClassDao userClassDao;

    @Resource
    private CustomerDao customerDao;

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private MerchantDao merchantDao;

    @Resource
    private ManagerDao managerDao;

    @Resource
    private SmsService smsService;

    @Resource
    private UserServiceSupport userServiceSupport;

    @Resource
    private MemcachedClientUtils memcachedClientUtils;

    @Resource
    private MQTemplate mqTemplate;

    @Value("${business.register.template}")
    private String registerTemplate;

    @Value("${business.register.exist.template}")
    private String registerExistTemplate;

    @Value("${random.password.size}")
    private String randPasswordSize;

    @Value("${customer.add.confirm.template}")
    private String customerConfirmTemplate;

    @Value("${employee.add.notify.template}")
    private String employeeNotifyTemplate;

    @Value("${employee.add.exist.notify.template}")
    private String employeeExistNotifyTemplate;

    @Value("${manager.add.notify.template}")
    private String managerNotifyTemplate;

    @Value("${manager.add.exist.notify.template}")
    private String managerExistNotifyTemplate;

    /**
     * 缓存中验证码过期时间
     */
    @Value("${cache.expire.sms.verify.code}")
    String cacheExpireSeconds;

    @Override
    public ApiResult<UserRegConfirmDto> addCustomer(LoginInfoDto loginInfoDto, boolean setLoginInfo, CustomerType customerType) {
        IdentityInfoDto identity = loginInfoDto.getIdentityInfoDto();
        ErrorCode err = checkAddCustomer(identity.getLoginName(), identity.getPassword(), identity.getSmsCode());
        if (err != null) {
            return ApiResult.fail(err);
        }

        // 用户不存在的情况
        User existingUser = userDao.getByUserPhone(identity.getLoginName());
        if (existingUser == null) {
            User user = addUser(identity.getLoginName(), identity.getLoginName(),
                    identity.getPassword(), null);
            UserClass userClass = saveUserClass(user, UserType.CUSTOMER, user.getId(), true, setLoginInfo, loginInfoDto);
            Customer customer = saveCustomer(user, customerType);
            CustomerDto customerDto = saveCustomer2IMServer(user, userClass, customer);

            UserRegConfirmDto confirmDto = new UserRegConfirmDto();
            confirmDto.setNeedConfirm(Boolean.FALSE);
            confirmDto.setCustomerDto(customerDto);

            // 发送注册推荐人消息
            sendCustomerRegisterMessage(user.getId(), user.getUserPhone(), loginInfoDto.getInvitationCode());

            return ApiResult.succ(confirmDto);
        }

        // 检查账户是否被删除
        if (existingUser.isDeleted()) {
            return ApiResult.fail(UserErrorCode.ACCOUNT_DELETED);
        }

        // 已存在C端账号的情况
        UserClass existingUserClass = userClassDao.findClass(existingUser.getId(), UserType.CUSTOMER.getValue());
        if (existingUserClass != null) {
            return ApiResult.fail(UserErrorCode.USER_EXISTED);
        }

        // 存在E端或B端账号，返回“错误”，让客户端确认
        UserClass bizExistence = userClassDao.findClass(existingUser.getId(), UserType.BUSINESS.getValue());
        UserClass empExistence = userClassDao.findClass(existingUser.getId(), UserType.EMPLOYEE.getValue());
        if (bizExistence != null || empExistence != null) {
            UserRegConfirmDto confirmDto = new UserRegConfirmDto();
            confirmDto.setMessage(customerConfirmTemplate);
            confirmDto.setNeedConfirm(Boolean.TRUE);
            String token = RandomStringUtils.randomAlphanumeric(Integer.valueOf(randPasswordSize));
            confirmDto.setToken(token);

            memcachedClientUtils.setCacheValue(
                    MemcachedPrefixType.CONFIRM_CUSTOMER.getPrefix() + identity.getLoginName(),
                    Integer.valueOf(cacheExpireSeconds),
                    token);

            return ApiResult.succ(confirmDto);
        }

        // 新增Customer角色
        UserClass userClass = saveUserClass(existingUser, UserType.CUSTOMER, existingUser.getId(), true, setLoginInfo, loginInfoDto);
        Customer customer = saveCustomer(existingUser, customerType);
        CustomerDto customerDto = saveCustomer2IMServer(existingUser, userClass, customer);

        UserRegConfirmDto confirmDto = new UserRegConfirmDto();
        confirmDto.setNeedConfirm(Boolean.FALSE);
        confirmDto.setCustomerDto(customerDto);

        // 发送注册推荐人消息
        sendCustomerRegisterMessage(existingUser.getId(), existingUser.getUserPhone(), loginInfoDto.getInvitationCode());

        return ApiResult.succ(confirmDto);
    }

    @Override
    public ApiResult<CustomerDto> confirmCustomerRegister(LoginInfoDto loginInfoDto, String token, boolean setLoginInfo, CustomerType customerType) {
        IdentityInfoDto identity = loginInfoDto.getIdentityInfoDto();
        Preconditions.checkNotNull(identity.getLoginName(), "用户手机号不能为空！");
        try {
            String cacheToken = memcachedClientUtils.getMemcachedClient().get(
                    MemcachedPrefixType.CONFIRM_CUSTOMER.getPrefix() + identity.getLoginName());
            if (!cacheToken.equals(token)) {
                LOG.warn("[C端注册]确认失败，phoneNumber={}", identity.getLoginName());
                return ApiResult.fail(UserErrorCode.USER_REGISTER_FAILED);
            }
        } catch (Exception e) {
            LOG.warn("[C端注册]Cache已失效，phoneNumber={}", identity.getLoginName(), e);
            return ApiResult.fail(UserErrorCode.CONFIRM_TIMEOUT);
        }

        User existingUser = userDao.getByUserPhone(identity.getLoginName());

        // 新增Customer角色
        UserClass userClass = saveUserClass(existingUser,
                UserType.CUSTOMER, existingUser.getId(), true, setLoginInfo, loginInfoDto);
        Customer customer = saveCustomer(existingUser, customerType);
        CustomerDto customerDto = saveCustomer2IMServer(existingUser, userClass, customer);
        // 发送注册推荐人消息
        sendCustomerRegisterMessage(existingUser.getId(), existingUser.getUserPhone(), loginInfoDto.getInvitationCode());

        return ApiResult.succ(customerDto);
    }

    @Override
    public ApiResult<CustomerDto> confirmCustomerRegister(LoginInfoDto loginInfoDto,boolean setLoginInfo, CustomerType customerType) {
        IdentityInfoDto identity = loginInfoDto.getIdentityInfoDto();
        Preconditions.checkNotNull(identity.getLoginName(), "用户手机号不能为空！");

        User existingUser = userDao.getByUserPhone(identity.getLoginName());

        // 新增Customer角色
        UserClass userClass = saveUserClass(existingUser,
                UserType.CUSTOMER, existingUser.getId(), true, setLoginInfo, loginInfoDto);
        Customer customer = saveCustomer(existingUser, customerType);
        CustomerDto customerDto = saveCustomer2IMServer(existingUser, userClass, customer);
        // 发送注册推荐人消息
        sendCustomerRegisterMessage(existingUser.getId(), existingUser.getUserPhone(), loginInfoDto.getInvitationCode());

        return ApiResult.succ(customerDto);
    }

    @Override
    public ApiResult addEmployee(EmployeeDto employeeDto, Long createUserId) {
        Preconditions.checkNotNull(employeeDto, "业者信息不能为空！");

        User existingUser = userDao.getByUserPhone(employeeDto.getUserPhone());
        if (existingUser == null) {
            String randPassword = RandomStringUtils.randomNumeric(Integer.valueOf(randPasswordSize));
            User user = addUser(employeeDto.getUserPhone(), employeeDto.getUserPhone(), randPassword, employeeDto.getName());

            UserClass userClass = saveUserClass(user, UserType.EMPLOYEE, createUserId, true, true, null);
            employeeDto.setId(user.getId());
            Employee employee = saveEmployee(employeeDto);

            User createUser = userDao.getById(createUserId);
            saveEmployee2IMServer(user, userClass, employee);
            sendSmsNotify(UserType.EMPLOYEE, user.getUserPhone(), String.format(employeeNotifyTemplate, user.getName(), createUser.getName(), randPassword));

            // 注册钱脉用户账户
            //customerAccountService.registerCustomerAccount(user.getId(), UserType.EMPLOYEE.getValue());

            return ApiResult.succ();
        }

        // 检查账户是否被删除
        if (existingUser.isDeleted()) {
            return ApiResult.fail(UserErrorCode.ACCOUNT_DELETED);
        }

        UserClass managerUserClass = userClassDao.findClass(existingUser.getId(), UserType.MANAGER.getValue());
        if (managerUserClass != null){//已是M端用户
            return ApiResult.fail(UserErrorCode.HAS_MANAGER_NO_EMPLOYEE);
        }

        UserClass bizUserClass = userClassDao.findClass(existingUser.getId(), UserType.BUSINESS.getValue());
        if (bizUserClass != null) {// 已存在B端账户
            return ApiResult.fail(UserErrorCode.HAS_BUSINESS_NO_EMPLOYEE);
        }

        UserClass emUserClass = userClassDao.findClass(existingUser.getId(), UserType.EMPLOYEE.getValue());
        if (emUserClass != null) {// 已存在E端账号
            Employee employeeInfo = employeeDao.getByUserId(emUserClass.getUserId());
            if (!employeeInfo.isQuitJobs()) {
                if (EmployeeType.PLATFORM_BD.equals(EmployeeType.getEnum(employeeInfo.getUserDetailType()))){
                     return ApiResult.fail(UserErrorCode.PE_NOT_JOIN_COMPANY);
                }else {
                    String employeeCompanyName = employeeDao.findCompany(employeeInfo.getCompanyId());
                    return ApiResult.failFormatMsg(UserErrorCode.USER_ALREADY_EMPLOYEE, employeeCompanyName);
                }
            }
        }

        // User不为空，一定存在C端账号
        existingUser.setName(employeeDto.getName());
        userDao.update(existingUser);

        if (emUserClass != null) {
            //更新离职的业者信息列表
            EmployeeDto employeeDtoOld = (EmployeeDto) userServiceSupport.findById(emUserClass.getUserId(), UserType.EMPLOYEE, false, false);
            Employee employeeInfo = employeeDao.getByUserId(emUserClass.getUserId());
            if (employeeInfo.isQuitJobs()) {
                if (employeeDto.getCompanyId().equals(employeeInfo.getCompanyId()) && !EmployeeType.getEnum(employeeDto.getUserDetailType()).equals(EmployeeType.getEnum(employeeInfo.getUserDetailType()))){
                    return ApiResult.fail(UserErrorCode.NOT_JOIN_COMPANY);
                }
                Employee employeeNew = updateEmployee(employeeDto, employeeInfo);
                employeeDao.update(employeeNew);
                emUserClass.setAvailable(true);
                userClassDao.update(emUserClass);
                updateEmployee2IMServer(existingUser, emUserClass, employeeNew, employeeDtoOld);
            }
        }else {
            UserClass userClass = saveUserClass(existingUser, UserType.EMPLOYEE, createUserId, true, true, null);
            employeeDto.setId(existingUser.getId());
            Employee employee = saveEmployee(employeeDto);

            User createUser = userDao.getById(createUserId);
            saveEmployee2IMServer(existingUser, userClass, employee);
            sendSmsNotify(UserType.EMPLOYEE, existingUser.getUserPhone(), String.format(employeeExistNotifyTemplate, existingUser.getName(), createUser.getName()));
        }

        // 注册钱脉用户账户
        //customerAccountService.registerCustomerAccount(existingUser.getId(), UserType.EMPLOYEE.getValue());

        return ApiResult.succ();
    }


    private Employee updateEmployee(EmployeeDto employeeDto, Employee employeeInfo) {
        employeeInfo.setNickName(employeeDto.getNickName());
        employeeInfo.setUserDetailType(employeeDto.getUserDetailType());
        employeeInfo.setCompanyId(employeeDto.getCompanyId());
        employeeInfo.setHeadImage(employeeDto.getHeadImage());
        employeeInfo.setLifePhotos(employeeDto.getLifePhotos());
        employeeInfo.setQuitJobs(false);
        employeeInfo.setResume(employeeDto.getResume());
        employeeInfo.setAdvertorial(employeeDto.getArticles());
        employeeInfo.setServiceContent(employeeDto.getServiceContent());
        employeeInfo.setPublicPhone(employeeDto.isPublicPhone());
        return employeeInfo;
    }

    @Override
    public ApiResult openManagerEmployeePart(EmployeeDto employeeDto, Long createUserId) {
        Preconditions.checkNotNull(employeeDto, "业者信息不能为空！");

        User existingUser = userDao.getByUserPhone(employeeDto.getUserPhone());

        UserClass bizUserClass = userClassDao.findClass(existingUser.getId(), UserType.BUSINESS.getValue());
        if (bizUserClass == null) {// 不存在B端账户
            return ApiResult.fail(UserErrorCode.HAS_BUSINESS_NO_EMPLOYEE);
        }
        UserClass emUserClass = userClassDao.findClass(existingUser.getId(), UserType.EMPLOYEE.getValue());
        if (emUserClass != null) {// 已存在E端账号
            return ApiResult.fail(UserErrorCode.USER_EXISTED);
        }

        existingUser.setName(employeeDto.getName());
        userDao.update(existingUser);

        UserClass userClass = saveUserClass(existingUser, UserType.EMPLOYEE, createUserId, true, true, null);
        employeeDto.setId(existingUser.getId());
        Employee employee = saveEmployee(employeeDto);

        saveEmployee2IMServer(existingUser, userClass, employee);

        // 注册钱脉用户账户
        //customerAccountService.registerCustomerAccount(existingUser.getId(), UserType.EMPLOYEE.getValue());

        return ApiResult.succ();
    }

    @Override
    public ApiResult addBusiness(User user, Long companyId, boolean isAvailable, Long createUserId) {
        Preconditions.checkArgument(user != null && companyId != null, "user或companyId不能为空！");

        User existingUser, existingUserByPhone, existingUserByName;
        if (StringUtils.isEmpty(user.getUserPhone())) {
            user.setUserPhone(user.getUserName());
            existingUserByPhone = null;
        } else {
            existingUserByPhone = userDao.getByUserPhone(user.getUserPhone());
        }
        existingUserByName = userDao.getByUserName(user.getUserName());
        if (existingUserByPhone == null && existingUserByName == null) {
            // 生成随机密码
            String initialPassword = RandomStringUtils.randomNumeric(Integer.valueOf(randPasswordSize));
            String encodedInitialPassword = MD5Utils.getMD5Code(initialPassword);
            user.setPassword(encodedInitialPassword);
            // user不存在，新增用户
            userDao.save(user);
            Merchant merchant = saveMerchant(user, companyId);
            UserClass userClass = saveUserClass(user, UserType.BUSINESS, createUserId, true, false, null);

            if (checkMerchantIntegrity(user)) {
                saveMerchant2IMServer(user, userClass, merchant);
            }

            if (isAvailable) {
                smsService.sendMessage(user.getUserPhone(), String.format(registerTemplate, user.getUserName(), initialPassword));
            }

            return ApiResult.succ();
        } else {
            if (existingUserByPhone != null) {
                UserClass businessUserClass =
                        userClassDao.findClass(existingUserByPhone.getId(), UserType.BUSINESS.getValue());
                UserClass customerUserClass =
                        userClassDao.findClass(existingUserByPhone.getId(), UserType.CUSTOMER.getValue());
                UserClass employeeUserClass =
                        userClassDao.findClass(existingUserByPhone.getId(), UserType.EMPLOYEE.getValue());

                if (employeeUserClass != null) {
                    return ApiResult.fail(UserErrorCode.HAS_EMPLOYEE_NO_BUSINESS);
                }
                if (businessUserClass != null) {
                    return ApiResult.fail(UserErrorCode.MERCHANT_PHONE_USED);
                }
                if (customerUserClass != null) {
                    // 存在C端用户，继续添加角色
                    existingUser = existingUserByPhone;
                } else {
                    return ApiResult.fail(UserErrorCode.MERCHANT_PHONE_USED);
                }
            } else {
                return ApiResult.fail(UserErrorCode.USER_EXISTED);
            }
        }

        // 检查账户是否被删除
        if (existingUser.isDeleted()) {
            return ApiResult.fail(UserErrorCode.ACCOUNT_DELETED);
        }

        user.setId(existingUser.getId());
        userDao.update(user);
        Merchant merchant = saveMerchant(existingUser, companyId);
        UserClass userClass = saveUserClass(existingUser, UserType.BUSINESS, createUserId, isAvailable, true, null);

        if (checkMerchantIntegrity(user)) {
            saveMerchant2IMServer(user, userClass, merchant);
        }

        if (isAvailable) {
            smsService.sendMessage(existingUser.getUserPhone(), String.format(registerExistTemplate, user.getUserName()));
        }

        return ApiResult.succ();
    }

    @Override
    public ApiResult addManager(ManagerDto managerDto, Long createUserId) {
        checkAddManager(managerDto, createUserId);

        String notifyMessage;
        User user = getUserByPhoneOrUserName(managerDto.getUserPhone(), managerDto.getUserName());
        if (user == null) {
            // 用户不存在, 使用随机密码创建User
            String randPassword = RandomStringUtils.randomNumeric(Integer.valueOf(randPasswordSize));
            user = addUser(managerDto.getUserName(),
                    managerDto.getUserPhone(), randPassword, managerDto.getName());
            notifyMessage = String.format(managerNotifyTemplate, user.getUserName(), randPassword);
        } else {
            // 用户已存在, 如果User被删除, 或者已开通C端以外的账号, 则不能注册M端账号
            if (user.isDeleted()) {
                return ApiResult.fail(UserErrorCode.ACCOUNT_DELETED);
            }
            List<UserClass> userClasses = userClassDao.findClasses(user.getId());
            if (userClasses != null && !userClasses.isEmpty()) {
                for (UserClass uc : userClasses) {
                    if (!UserType.CUSTOMER.compare(uc.getUserType())) {
                        return ApiResult.fail(UserErrorCode.USER_EXISTED);
                    }
                }
            }
            user.setName(managerDto.getName());
            user.setUserName(managerDto.getUserName());
            userDao.update(user);
            notifyMessage = String.format(managerExistNotifyTemplate, user.getUserName());
        }

        // 为用户创建M端分类
        UserClass userClass = saveUserClass(user, UserType.MANAGER, createUserId, true, false, null);
        // 为用户创建M端账号
        managerDto.setId(user.getId());
        Manager manager = saveManager(managerDto);
        // 将用户信息同步到IM
        saveManager2IMServer(user, userClass, manager);
        // 发送创建成功的短信
        sendSmsNotify(UserType.MANAGER, user.getUserPhone(), notifyMessage);

        return ApiResult.succ();
    }

    private User getUserByPhoneOrUserName(String userPhone, String userName) {
        User user = userDao.getByUserPhone(userPhone);
        if (user == null) {
            user = userDao.getByUserName(userName);
        }
        return user;
    }

    private boolean checkMerchantIntegrity(User user) {
        return user.getUserPhone() != null && user.getName() != null
                && user.getUserName() != null && user.getIdImageA() != null
                && user.getIdHoldImage() != null && user.getIdCard() != null;
    }

    private ErrorCode checkAddCustomer(String userPhone, String password, String verifyCode) {
        Validator.checkMobileNumber(userPhone);
        Validator.checkPassword(password);

        if (userPhone.equals(password)) {
            return UserErrorCode.USER_PWD_DUPLICATE;
        }
        if (!smsService.checkVerifyCode(
                userPhone, verifyCode, MemcachedPrefixType.REGISTER).isSucc()) {
            return CommonErrorCode.VERIFY_CODE_ERROR;
        }

        return null;
    }

    private void checkAddManager(ManagerDto managerDto, Long createUserId) {
        FieldChecker.checkEmpty(createUserId, "createUserId");
        Validator.checkUserName(managerDto.getUserName());
        Validator.checkMobileNumber(managerDto.getUserPhone());
        Validator.checkCnName(managerDto.getName());

        if (managerDto.parseUserDetailType() == null) {
            throw new ValidationException("'managerDto.userDetailType'无效值");
        }
    }

    private User addUser(String userName, String userPhone, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setUserName(userName);
        user.setUserPhone(userPhone);
        user.setPassword(MD5Utils.getMD5Code(password));
        userDao.save(user);
        return user;
    }

    /**
     * 新增用户类型信息，并记录相关注册和登录信息
     *
     * @param user 用户信息
     * @param userType 用户类别
     * @param createUserId 创建者userId
     * @param isAvailable 是否可用
     * @param setLoginInfo 是否记录登录信息
     * @param infoDto 注册或登录信息
     * @return 用户类型信息
     */
    private UserClass saveUserClass(User user, UserType userType, Long createUserId, boolean isAvailable,
                                    boolean setLoginInfo, LoginInfoDto infoDto) {
        Date now = Calendar.getInstance().getTime();
        UserClass userClass = new UserClass();
        userClass.setUserId(user.getId());
        userClass.setUserType(userType.getValue());
        userClass.setCreateUserId(createUserId);
        userClass.setAvailable(isAvailable);
        userClass.setCreateTime(now);
        userClass.setUpdateTime(now);
        // 设置用户注册拓展信息
        if (infoDto != null) {
            userClass.setRegisterIp(infoDto.getIp());
            userClass.setRegisterAddress(AddressUtils.getAddresses(infoDto.getIp()));
            if (infoDto.getUserExtensionInfo() != null) {
                userClass.setRegisterInfo(JsonUtils.objectToJson(infoDto.getUserExtensionInfo()));
            }
        }
        // 设置用户最近一次登录信息
        if (setLoginInfo) {
            userClass.setLastLoginTime(now);
            if (infoDto != null) {
                userClass.setLastLoginIp(infoDto.getIp());
                userClass.setLoginAddress(AddressUtils.getAddresses(infoDto.getIp()));
                if (infoDto.getUserExtensionInfo() != null) {
                    userClass.setLastLoginInfo(JsonUtils.objectToJson(infoDto.getUserExtensionInfo()));
                }
            }
        }
        userClassDao.save(userClass);
        return userClass;
    }

    private Customer saveCustomer(User user, CustomerType customerType) {
        Customer customer = new Customer();
        customer.setUserId(user.getId());
        customer.setCustomerType(customerType.getCode());
        customer.setNickName("业主" + user.getUserPhone().substring(
                user.getUserPhone().length() - 4, user.getUserPhone().length())); // 昵称默认为: 业主 + 手机号后4位
        customerDao.save(customer);
        return customer;
    }

    private Employee saveEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setResume(employeeDto.getResume());
        employee.setUserId(employeeDto.getId());
        employee.setUserDetailType(employeeDto.getUserDetailType());
        employee.setCompanyId(employeeDto.getCompanyId());
        employee.setHeadImage(employeeDto.getHeadImage());
        employee.setLifePhotos(employeeDto.getLifePhotos());
        employee.setServiceContent(employeeDto.getServiceContent());
        employee.setAdvertorial(employeeDto.getArticles());
        employee.setPublicPhone(employeeDto.isPublicPhone());

        employeeDao.save(employee);
        return employee;
    }

    private Merchant saveMerchant(User user, Long companyId) {
        Merchant merchant = new Merchant();
        merchant.setUserId(user.getId());
        merchant.setCompanyId(companyId);
        merchantDao.save(merchant);
        return merchant;
    }

    private Manager saveManager(ManagerDto managerDto) {
        Manager manager = new Manager();
        manager.setUserId(managerDto.getId());
        manager.setUserDetailType(managerDto.getUserDetailType());
        manager.setHeadImage(managerDto.getHeadImage());
        manager.setQuitJobs(managerDto.isQuitJobs());
        managerDao.save(manager);
        return manager;
    }

    private CustomerDto saveCustomer2IMServer(User user, UserClass userClass, Customer customer) {
        CustomerDto customerDto = UserDtoConverter.convert2CustomerDto(user, userClass, customer);
        //userServiceSupport.refresh2IMServer(customerDto);
        return customerDto;
    }

    private EmployeeDto saveEmployee2IMServer(User user, UserClass userClass, Employee employee) {
        EmployeeDto employeeDto = UserDtoConverter.convert2EmployeeDto(user, userClass, employee);
        //userServiceSupport.refresh2IMServer(employeeDto);
        return employeeDto;
    }

    private void updateEmployee2IMServer(User existingUser, UserClass emUserClass, Employee employeeNew, EmployeeDto employeeDtoOld) {
        EmployeeDto employeeDtoNew = UserDtoConverter.convert2EmployeeDto(existingUser, emUserClass, employeeNew);
        //userServiceSupport.refresh2IMServer(employeeDtoOld, employeeDtoNew);
    }

    private MerchantDto saveMerchant2IMServer(User user, UserClass userClass, Merchant merchant) {
        MerchantDto merchantDto = UserDtoConverter.convert2MerchantDto(user, userClass, merchant);
        //userServiceSupport.refresh2IMServer(merchantDto);
        return merchantDto;
    }

    private ManagerDto saveManager2IMServer(User user, UserClass userClass, Manager manager) {
        ManagerDto managerDto = UserDtoConverter.convert2ManagerDto(user, userClass, manager);
        //userServiceSupport.refresh2IMServer(managerDto);
        return managerDto;
    }

    private void sendSmsNotify(UserType userType, String userPhone, String message) {
        ApiResult sendResult = smsService.sendMessage(userPhone, message);
        if (!sendResult.isSucc()) {
            throw new RuntimeException("新增用户账号(" + userType.getValue() + ")，发送短信通知失败");
        }
    }

    private void sendCustomerRegisterMessage(Long userId, String userPhone, String invitationCode) {
        CustomerInfoMsg msg = new CustomerInfoMsg(userId, userPhone, invitationCode);
        mqTemplate.sendDirectMessage(new MQMessage(MQReceiverType.CUSTOMER, "reg.route.key", msg));
        LOG.info("发送一条用户注册的消息");
    }

}
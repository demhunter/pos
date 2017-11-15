/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.im.service.support.netease;

import com.pos.common.util.basic.JsonUtils;
import com.pos.common.util.constans.GlobalConstants;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.im.exception.IMErrorCode;
import com.pos.im.service.support.netease.dto.request.*;
import com.pos.im.service.support.netease.dto.response.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 睿智
 * @version 1.0, 2017/9/6
 */
@Component
public class NeteaseIMClient {

    private static final Logger logger = LoggerFactory.getLogger(NeteaseIMClient.class);

    @Resource
    private GlobalConstants globalConstants;

    private static final String CHARSET = "UTF-8";

    //创建accid
    private static final String CREATE = "https://api.netease.im/nimserver/user/create.action";

    //更新accid
    private static final String UPDATE = "https://api.netease.im/nimserver/user/update.action";

    //刷新token
    private static final String REFRESHTOKEN = "https://api.netease.im/nimserver/user/refreshToken.action";

    //封禁
    private static final String BLOCK = "https://api.netease.im/nimserver/user/block.action";

    //解禁
    private static final String UNBLOCK = "https://api.netease.im/nimserver/user/unblock.action";

    //更新用户名片
    private static final String UPDATEUSERINFO = "https://api.netease.im/nimserver/user/updateUinfo.action";

    //获取用户名片
    private static final String GETUSERINFO = "https://api.netease.im/nimserver/user/getUinfos.action";

    //发送点对点普通消息
    private static final String SENDMSG = "https://api.netease.im/nimserver/msg/sendMsg.action";

    //批量发送点对点普通消息
    private static final String SENDBATCHMSG = "https://api.netease.im/nimserver/msg/sendBatchMsg.action";

    //发送自定义系统通知
    private static final String SENDATTACHMSG = "https://api.netease.im/nimserver/msg/sendAttachMsg.action";

    //批量发送自定义系统通知
    private static final String SENDBATCHATTACHMSG = "https://api.netease.im/nimserver/msg/sendBatchAttachMsg.action";

    //撤回消息
    private static final String RECALL = "https://api.netease.im/nimserver/msg/recall.action";

    //创建群
    private static final String CREATETEAM = "https://api.netease.im/nimserver/team/create.action";

    //加人入群
    private static final String TEAMADD = "https://api.netease.im/nimserver/team/add.action";

    //群里面踢人
    private static final String TEAMKICK = "https://api.netease.im/nimserver/team/kick.action";

    //解散群
    private static final String TEAMREMOVE = "https://api.netease.im/nimserver/team/remove.action";

    //编辑群资料
    private static final String TEAMUPDATE = "https://api.netease.im/nimserver/team/update.action";

    //群信息与成员列表查询
    private static final String TEAMQUERY = "https://api.netease.im/nimserver/team/query.action";

    //获取某用户所加入的群信息
    private static final String JOINTEAMS = "https://api.netease.im/nimserver/team/joinTeams.action";

    //更改群里面的昵称
    private static final String UPDATETEAMNICK = "https://api.netease.im/nimserver/team/updateTeamNick.action";

    //退群
    private static final String TEAMLEAVE = "https://api.netease.im/nimserver/team/leave.action";

    //查询群组的聊天消息
    private static final String QUERYTEAMMSG = "https://api.netease.im/nimserver/history/queryTeamMsg.action";


    private HttpPost setHeaders(HttpPost httpPost){
        String rndNum = String.valueOf(new Random().nextInt(100000));
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(globalConstants.imNeteaseAppSecret, rndNum ,curTime);
        httpPost.addHeader("AppKey", globalConstants.imNeteaseAppKey);
        httpPost.addHeader("Nonce", rndNum);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpPost;
    }

    private List<NameValuePair> buildReqParams(Object bean) throws IllegalAccessException {
        List<NameValuePair> list = new ArrayList<>();
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
        }
        for (Field f : fields) {
            String key = f.toString().substring(f.toString().lastIndexOf(".") + 1);
            Object value = f.get(bean);
            if (value != null && !value.equals(""))
                list.add(new BasicNameValuePair(key,  value.toString()));
        }
        return list;
    }

    private List<NameValuePair> buildReqParams(Map<String,String> params){
        List<NameValuePair> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(params)){
            params.keySet().forEach(e->list.add(new BasicNameValuePair(e,params.get(e))));
        }
        return list;
    }


    public ApiResult<CreateResponseDto> create(CreateRequestDto createRequestDto) {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(CREATE);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(createRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                CreateResponseDto createResponseDto = JsonUtils.jsonToObject(result, new TypeReference<CreateResponseDto>() {});
                return ApiResult.succ(createResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ApiResult update(CreateRequestDto createRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(UPDATE);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(createRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ApiResult<CreateResponseDto> refreshToken(String accid){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(REFRESHTOKEN);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("accid",accid));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                CreateResponseDto createResponseDto = JsonUtils.jsonToObject(result, new TypeReference<CreateResponseDto>() {});
                return ApiResult.succ(createResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ApiResult<OnlyCodeResponseDto> block(String accid){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(BLOCK);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("accid",accid));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ApiResult<OnlyCodeResponseDto> unBlock(String accid){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(UNBLOCK);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("accid",accid));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ApiResult<OnlyCodeResponseDto> updateUserInfo(UserInfoRequestDto userInfoRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(UPDATEUSERINFO);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(userInfoRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public ApiResult<GetUserInfoResponseDto> getUserInfo(List<String> accids){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(GETUSERINFO);
            setHeaders(httpPost);
            JSONArray jsonArray = new JSONArray(accids);
            // 设置请求的参数
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("accids",jsonArray.toString()));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                GetUserInfoResponseDto getUserInfoResponseDto = JsonUtils.jsonToObject(result, new TypeReference<GetUserInfoResponseDto>() {});
                return ApiResult.succ(getUserInfoResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建群
     * @param createTeamRequestDto
     * @return
     */
    public ApiResult<CreateTeamResponseDto> createTeam(CreateTeamRequestDto createTeamRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(CREATETEAM);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(createTeamRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                CreateTeamResponseDto createTeamResponseDto = JsonUtils.jsonToObject(result, new TypeReference<CreateTeamResponseDto>() {});
                return ApiResult.succ(createTeamResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解散群
     * @param teamRemoveRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> removeTeam(TeamRemoveRequestDto teamRemoveRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(TEAMREMOVE);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(teamRemoveRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拉人入群
     * @param teamAddRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> addTeam(TeamAddRequestDto teamAddRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(TEAMADD);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(teamAddRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 踢人出群
     * @param teamKickRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> kickTeam(TeamKickRequestDto teamKickRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(TEAMKICK);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(teamKickRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新群信息
     * @param teamUpdateRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> updateTeam(TeamUpdateRequestDto teamUpdateRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(TEAMUPDATE);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(teamUpdateRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询群以及群组成员信息
     * @param queryTeamInfoRequestDto
     * @return
     */
    public ApiResult<QueryTeamInfoResponseDto> queryTeamInfo(QueryTeamInfoRequestDto queryTeamInfoRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(TEAMQUERY);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(queryTeamInfoRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                QueryTeamInfoResponseDto queryTeamInfoResponseDto = JsonUtils.jsonToObject(result, new TypeReference<QueryTeamInfoResponseDto>() {});
                return ApiResult.succ(queryTeamInfoResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询用户加入的群信息
     * @param accid
     * @return
     */
    public ApiResult<UserJoinTeamResponseDto> queryUserJoinTeam(String accid){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(JOINTEAMS);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("accid",accid));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                UserJoinTeamResponseDto userJoinTeamResponseDto = JsonUtils.jsonToObject(result, new TypeReference<UserJoinTeamResponseDto>() {});
                return ApiResult.succ(userJoinTeamResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户在群里面的昵称
     * @param updateTeamNickRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> updateTeamNick(UpdateTeamNickRequestDto updateTeamNickRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(UPDATETEAMNICK);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(updateTeamNickRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 主动退群
     * @param leaveTeamRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> leaveTeam(LeaveTeamRequestDto leaveTeamRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(TEAMLEAVE);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(leaveTeamRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 点对点的发送消息
     * @param singleSendMsgRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> sendSingleMsg(SingleSendMsgRequestDto singleSendMsgRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SENDMSG);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(singleSendMsgRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量发送点对点消息
     * @param batchSendMsgRequestDto
     * @return
     */
    public ApiResult<BatchSendMsgResponseDto> batchSendMsg(BatchSendMsgRequestDto batchSendMsgRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SENDBATCHMSG);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(batchSendMsgRequestDto);
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                BatchSendMsgResponseDto batchSendMsgResponseDto = JsonUtils.jsonToObject(result, new TypeReference<BatchSendMsgResponseDto>() {});
                return ApiResult.succ(batchSendMsgResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送自定义系统通知
     * @param sendAttachMsgRequestDto
     * @return
     */
    public ApiResult<OnlyCodeResponseDto> sendAttachMsg(SingleSendAttachMsgRequestDto sendAttachMsgRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SENDATTACHMSG);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(sendAttachMsgRequestDto);
//            logger.info("发送自定义系统通知的请求参数："+JsonUtils.objectToJson(list));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
//            logger.info("发送自定义系统通知的返回："+result);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                OnlyCodeResponseDto onlyCodeResponseDto = JsonUtils.jsonToObject(result, new TypeReference<OnlyCodeResponseDto>() {});
                return ApiResult.succ(onlyCodeResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 批量发送自定义系统通知
     * @param batchSendAttachMsgRequestDto
     * @return
     */
    public ApiResult<BatchSendMsgResponseDto> batchSendAttachMsg(BatchSendAttachMsgRequestDto batchSendAttachMsgRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SENDBATCHATTACHMSG);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(batchSendAttachMsgRequestDto);
//            logger.info("批量发送自定义系统通知的请求参数："+JsonUtils.objectToJson(list));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
//            logger.info("批量发送自定义系统通知的返回："+result);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                BatchSendMsgResponseDto batchSendMsgResponseDto = JsonUtils.jsonToObject(result, new TypeReference<BatchSendMsgResponseDto>() {});
                return ApiResult.succ(batchSendMsgResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询群聊的聊天记录
     * @param queryTeamMsgRequestDto
     * @return
     */
    public ApiResult<QueryTeamMsgResponseDto> queryTeamMsg(QueryTeamMsgRequestDto queryTeamMsgRequestDto){
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(QUERYTEAMMSG);
            setHeaders(httpPost);
            // 设置请求的参数
            List<NameValuePair> list = buildReqParams(queryTeamMsgRequestDto);
//            logger.info("批量发送自定义系统通知的请求参数："+JsonUtils.objectToJson(list));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), CHARSET);
//            logger.info("批量发送自定义系统通知的返回："+result);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.get("code").toString().equals("200")){
                QueryTeamMsgResponseDto queryTeamMsgResponseDto = JsonUtils.jsonToObject(result, new TypeReference<QueryTeamMsgResponseDto>() {});
                return ApiResult.succ(queryTeamMsgResponseDto);
            }else{
                return ApiResult.failFormatMsg(IMErrorCode.IM_NETEASE_ERROR,"code:"+jsonObject.get("code")+"   desc:"+jsonObject.get("desc"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }






    public static void main(String[] args) {
       /* CreateRequestDto createRequestDto = new CreateRequestDto();
        createRequestDto.setAccid("lrz0003");
        NeteaseIMClient neteaseIMClient = new NeteaseIMClient();
//        ApiResult<CreateResponseDto> apiResult = neteaseIMClient.create(createRequestDto);
//        if(apiResult.isSucc()){
//            System.out.println("code:"+apiResult.getData().getCode()+"   accid:"+apiResult.getData().getInfo().getAccid()+" name:"
//            +apiResult.getData().getInfo().getName()+"　token:"+apiResult.getData().getInfo().getToken());
//        }else{
//            System.out.println(apiResult.getMessage());
//        }
//        ApiResult<OnlyCodeResponseDto> apiResult = neteaseIMClient.update(createRequestDto);
//        if(apiResult.isSucc()){
//            System.out.println("code:"+apiResult.getData().getCode());
//        }else{
//            System.out.println(apiResult.getMessage());
//        }
        List<String> accids = new ArrayList<>();
        accids.add("lrz0001");
        accids.add("lrz0002");
        accids.add("lrz0005");
        ApiResult<GetUserInfoResponseDto> apiResult = neteaseIMClient.getUserInfo(accids);
        if(apiResult.isSucc()){
            System.out.println("****code:"+apiResult.getData().getCode());

            List<UserInfoRequestDto> users = apiResult.getData().getUinfos();
            users.forEach(e-> System.out.println(e.getAccid()+"           "+e.getName()));
        }else{
            System.out.println(apiResult.getMessage());
        }*/
       logger.info("*****************");
    }
}

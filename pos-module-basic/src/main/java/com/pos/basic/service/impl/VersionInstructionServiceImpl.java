/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.mysql.jdbc.exceptions.MySQLDataException;
import com.pos.basic.dao.VersionInstructionDao;
import com.pos.basic.domain.VersionInstruction;
import com.pos.basic.dto.UserIdentifier;
import com.pos.basic.dto.version.VersionDto;
import com.pos.basic.dto.version.VersionInstructionDto;
import com.pos.basic.exception.BasicErrorCode;
import com.pos.basic.service.VersionInstructionService;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.mvc.support.LimitHelper;
import com.pos.common.util.mvc.support.NullObject;
import com.pos.common.util.mvc.support.Pagination;
import com.pos.common.util.validation.FieldChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 版本更新介绍ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/12/12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class VersionInstructionServiceImpl implements VersionInstructionService {

    private static final Logger LOG = LoggerFactory.getLogger(VersionInstructionServiceImpl.class);

    @Resource
    private VersionInstructionDao versionInstructionDao;

    @Override
    public ApiResult<NullObject> saveOrUpdateInstruction(VersionInstructionDto instruction, UserIdentifier operator) {
        FieldChecker.checkEmpty(instruction, "instruction");
        FieldChecker.checkEmpty(operator, "operator");
        instruction.check();
        operator.check("operator");
        VersionDto newVersion = VersionDto.parse(instruction.getVersion());

        if (instruction.getId() == null) {
            // 新增版本说明介绍
            VersionInstruction existedVersion = versionInstructionDao.findByVersion(newVersion.toString());
            if (existedVersion != null) {
                return ApiResult.fail(BasicErrorCode.VERSION_INSTRUCTION_ERROR_DUPLICATE_VERSION);
            }
            existedVersion = new VersionInstruction();
            existedVersion.setVersion(newVersion.toString());
            existedVersion.setInstruction(instruction.getInstruction());
            existedVersion.setCreateUserId(operator.getUserId());
            existedVersion.setAvailable(Boolean.TRUE); // 新增成功默认启用

            try {
                versionInstructionDao.save(existedVersion);
            } catch (Exception e) {
                LOG.error("保存版本介绍时发生异常！instruction = {}", existedVersion);
                throw e;
            }
        } else {
            VersionInstruction existedVersionById = versionInstructionDao.findById(instruction.getId());
            if (existedVersionById == null) {
                return ApiResult.fail(BasicErrorCode.VERSION_INSTRUCTION_ERROR_NOT_EXISTED);
            }
            VersionDto existedVersion = VersionDto.parse(existedVersionById.getVersion());
            boolean isUpdateVersion = existedVersion.compare(newVersion, false) != 0;
            if (isUpdateVersion) {
                // 更新有改变版本号
                VersionInstruction existedVersionByStr = versionInstructionDao.findByVersion(newVersion.toString());
                if (existedVersionByStr != null) {
                    return ApiResult.fail(BasicErrorCode.VERSION_INSTRUCTION_ERROR_DUPLICATE_VERSION);
                }
            }
            existedVersionById.setInstruction(instruction.getInstruction());
            if (isUpdateVersion) {
                existedVersionById.setVersion(newVersion.toString());
            }
            existedVersionById.setUpdateUserId(operator.getUserId());

            try {
                versionInstructionDao.update(existedVersionById);
            } catch (Exception e) {
                LOG.error("更新版本介绍时发生异常！instruction = {}", existedVersionById);
                throw e;
            }
        }

        return ApiResult.succ();
    }

    @Override
    public VersionInstructionDto findInstruction(Long instructionId) {
        FieldChecker.checkEmpty(instructionId, "instructionId");

        VersionInstruction instruction = versionInstructionDao.findById(instructionId);
        if (instruction == null) {
            return null;
        }

        return toVersionInstructionDto(instruction);
    }

    @Override
    public VersionInstructionDto findInstruction(VersionDto version) {
        FieldChecker.checkEmpty(version, "version");
        version.check("version");

        VersionInstruction instruction = versionInstructionDao.findByVersion(version.toString());
        if (instruction == null) {
            return null;
        }
        return toVersionInstructionDto(instruction);
    }

    private VersionInstructionDto toVersionInstructionDto(VersionInstruction instruction) {
        VersionInstructionDto dto = new VersionInstructionDto();

        BeanUtils.copyProperties(instruction, dto);

        return dto;
    }

    @Override
    public ApiResult<NullObject> updateAvailable(Long instructionId, boolean available, UserIdentifier operator) {
        FieldChecker.checkEmpty(instructionId, "instructionId");
        FieldChecker.checkEmpty(operator, "operator");

        VersionInstruction instruction = versionInstructionDao.findById(instructionId);
        if (instruction == null) {
            return ApiResult.fail(BasicErrorCode.VERSION_INSTRUCTION_ERROR_NOT_EXISTED);
        }

        if (!instruction.getAvailable().equals(available)) {
            versionInstructionDao.updateAvailable(instructionId, available, operator.getUserId());
        }
        return ApiResult.succ();
    }

    @Override
    public ApiResult<Pagination<VersionInstructionDto>> queryInstructions(Boolean available, LimitHelper limitHelper) {
        FieldChecker.checkEmpty(limitHelper, "limitHelper");

        int totalCount = versionInstructionDao.queryCount(available);
        Pagination<VersionInstructionDto> pagination = Pagination.newInstance(limitHelper, totalCount);
        if (totalCount > 0) {
            List<VersionInstruction> instructions = versionInstructionDao.queryInstructions(available, limitHelper);
            pagination.setResult(instructions.stream()
                    .map(this :: toVersionInstructionDto).collect(Collectors.toList()));
        }

        return ApiResult.succ(pagination);
    }
}

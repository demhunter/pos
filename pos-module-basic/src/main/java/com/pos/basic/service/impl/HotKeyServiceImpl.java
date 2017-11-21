/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.basic.service.impl;

import com.pos.basic.constant.RedisConstants;
import com.pos.basic.dao.HotKeyDao;
import com.pos.basic.dto.HotKeyDto;
import com.pos.basic.service.HotKeyService;
import com.pos.common.util.date.SimpleDateUtils;
import com.pos.common.util.mvc.support.ApiResult;
import com.pos.common.util.validation.FieldChecker;
import org.joda.time.DateTime;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 热词相关ServiceImpl
 *
 * @author wangbing
 * @version 1.0, 2017/01/06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HotKeyServiceImpl implements HotKeyService {

    @Resource
    private HotKeyDao hotKeyDao;

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;
    /**
     * 更新热词信息
     *
     * @param hotKeyDto
     * @return true = 更新成功，false = 更新失败
     */
    @Override
    public ApiResult<HotKeyDto> saveHotKey(HotKeyDto hotKeyDto) {
        FieldChecker.checkEmpty(hotKeyDto, "hotKeyDto");

        hotKeyDao.saveHotKey(hotKeyDto);
        return ApiResult.succ(hotKeyDao.queryHotKeyByName(hotKeyDto.getKeyName()));
    }

    /**
     * 批量更新热词信息
     *
     * @param hotKeyDtoList 需要更新的列表
     * @return true = 更新成功，false = 更新失败
     */
    @Override
    public boolean updateHotkeys(List<HotKeyDto> hotKeyDtoList) {
        FieldChecker.checkEmpty(hotKeyDtoList, "hotKeyList");

        hotKeyDao.updateHotKeys(hotKeyDtoList);
        return true;
    }

    /**
     * 根据查询关键字查询数据库中的热词信息
     *
     * @param queryKey 关键字
     * @return 热词信息
     */
    @Override
    public ApiResult<HotKeyDto> queryHotKeyByName(String queryKey) {
        FieldChecker.checkEmpty(queryKey, "queryKey");

        return ApiResult.succ(hotKeyDao.queryHotKeyByName(queryKey));
    }

    @Override
    public void handleHotKey(String hotKey) {
        DateTime dateTime = new DateTime();
        String weekKey = RedisConstants.HOT_KEY_WEEK + dateTime.getWeekyear() + dateTime.getWeekOfWeekyear();
        String monthKey = RedisConstants.HOT_KEY_MONTH + dateTime.getYear() + dateTime.getMonthOfYear();
        Boolean isMember = redisTemplate.opsForSet().isMember(RedisConstants.HOT_KEY, hotKey);
        if (isMember == null || !isMember) {
            // 热词不在Set中，那么先存数据库，再存redis，再更新周月数据
            HotKeyDto hotKeyDto = queryHotKeyByName(hotKey).getData();
            if (hotKeyDto == null) {
                hotKeyDto = saveHotKey(new HotKeyDto(hotKey, (byte) 1)).getData();
            }
            Map<String, Object> hotKeyInfos = new HashMap<>();
            hotKeyInfos.put("id", hotKeyDto.getId().toString());
            hotKeyInfos.put("keyName", hotKeyDto.getKeyName());
            hotKeyInfos.put("keyType", hotKeyDto.getKeyType().toString());
            hotKeyInfos.put("totalCount", hotKeyDto.getTotalCount().toString());
            hotKeyInfos.put("createTime", SimpleDateUtils.formatDate(hotKeyDto.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            hotKeyInfos.put("updateTime", SimpleDateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            hotKeyInfos.put("isUpdated", Long.valueOf(0L).toString());
            redisTemplate.opsForHash().putAll(RedisConstants.HOT_KEY_INFO + hotKey, hotKeyInfos);
            redisTemplate.opsForSet().add(RedisConstants.HOT_KEY, hotKey);
        } else {
            redisTemplate.opsForHash().increment(RedisConstants.HOT_KEY_INFO + hotKey, "totalCount", 1L);
            redisTemplate.opsForHash().put(
                    RedisConstants.HOT_KEY_INFO + hotKey,
                    "updateTime", SimpleDateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            redisTemplate.opsForHash().put(RedisConstants.HOT_KEY_INFO + hotKey, "isUpdated", Long.valueOf(1L).toString());
        }
        // 增加hotKey年周搜索排行
        redisTemplate.opsForZSet().incrementScore(weekKey, hotKey, RedisConstants.HOT_KEY_ITEM_SCORE);
        // 增加hotKey年月搜索排行
        redisTemplate.opsForZSet().incrementScore(monthKey, hotKey, RedisConstants.HOT_KEY_ITEM_SCORE);
    }
}

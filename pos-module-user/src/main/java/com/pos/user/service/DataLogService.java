/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.user.service;

import com.pos.user.dto.DataLogDto;

/**
 * @author 睿智
 * @version 1.0, 2017/10/19
 */
public interface DataLogService {

    void saveDataLog(DataLogDto dataLogDto);
}

/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangbing
 * @version 1.0, 2017/03/21
 */
public class TimeJsonDeserializer extends JsonDeserializer<Date> {

    private static final Logger logger = LoggerFactory.getLogger(TimeJsonDeserializer.class);

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        if (jsonParser == null || StringUtils.isEmpty(jsonParser.getText())) {
            return null;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateUtils.DatePattern.STANDARD_PATTERN.toString());
            return sdf.parse(jsonParser.getValueAsString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}

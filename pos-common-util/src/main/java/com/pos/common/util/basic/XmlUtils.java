/*
 * Copyright (c) 2016 ywmj.com. All Rights Reserved.
 */
package com.pos.common.util.basic;

import com.pos.common.util.exception.IllegalParamException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * xml工具类
 *
 * @author wangbing
 * @version 1.0, 2017/03/16
 */
public class XmlUtils {

    private static Logger logger = LoggerFactory.getLogger(XmlUtils.class);

    /**
     * Map转XML
     *
     * @param param map对象
     * @return xml形式的字符串
     */
    public static String mapToXML(Map<String,String> param){
        StringBuilder sb = new StringBuilder();

        sb.append("<xml>");
        for (Map.Entry<String,String> entry : param.entrySet()) {
            sb.append("<"+ entry.getKey() +"><![CDATA[");
            sb.append(entry.getValue());
            sb.append("]]></"+ entry.getKey() +">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

    /**
     * XML转Map
     *
     * @param xmlStr 带转化的XML字符串
     * @return Map对象
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
        xmlStr = xmlStr.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if(null == xmlStr || "".equals(xmlStr)) {
            throw new IllegalParamException("xmlStr不能为空");
        }

        Map<String,String> m = new HashMap<>();
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List list = root.getChildren();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v;
                List children = e.getChildren();
                if(children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = getChildrenText(children);
                }

                m.put(k, v);
            }

            //关闭流

        } catch (Exception e) {
            throw new IllegalParamException("XML字符串解析错误");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("输入流关闭失败");
                }
            }
        }

        return m;
    }

    public static String setXML(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code><return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
    }

    public static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }
}

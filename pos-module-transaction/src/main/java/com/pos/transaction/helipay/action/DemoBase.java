package com.pos.transaction.helipay.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DemoBase {

	protected final Log logger = LogFactory.getLog(this.getClass());

//	@Resource
//	private GlobalConstants globalConstants;

	/*//商户号
	public static final String MERCHANT_NO ="C1800001401";

	//同人的请求地址
	public static final String REQUEST_URL_SAME_PERSON = "http://test.trx.helipay.com/trx/quickPayApi/interface.action";

	//请求参数签名时的MD5的密钥  即快捷产品的密钥
	public static final String SIGNKEY_SAME_PERSON = "6oRQOzdnCtQJ52hHHHPpjN0ukr3zMuCt";

	public static final String SPLIT = "&";

	public static final String REQUEST_URL_TRANSFER = "http://test.trx.helipay.com/trx/transfer/interface.action";

	//RSA私钥
	public static final String SIGNKEY_TRANSFER_RSA = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK9thtNWHy/cLxjyNDKmiewc+ftyCMdB9b+KmZ2XlLYCVhtY2cToDOYHnIqJLcY2PVei1k+CEqOYoRSwdWtxbIXOh9rGJjVjmJTHcXsXFCqEBQQdmEKKhsjGkQf4NL7J/dHgatKxmBUUkzkU9PbEVnYVuahI78upD1Jop0uB2GYfAgMBAAECgYB8M8+BUThDamDuCI1sTvzXbqyOme4dJVYYhsi8CtX/ByhvtDh6cNCxDDKI4xbFfyFvKpsRL8aCjU1+mHCJ4YQzC5wAQ596X1kPLBYK6MvlZFSshmQwaFmqkVFF9FGEEnTtZiDxKwRf+GgFaUKNKMSEoKfMo6Vr1EynI4UFgkWuAQJBAOGWb/9FdFzTmEw8lb/II7mASH4tTIS35oXvy4VZmGtIx4Y1t+mz+viP+r+yoalnm2i1vwWDiKAUiDlznttncMsCQQDHE/SqdFzAZQne7fjEohsBSEJb4gH1goJTA5+jztVPj/cisOpW9KMhnkSX86Sq5tXhYfCdiosaUrzakQU9J5l9AkAFAT9u3G2eeZtRZa602I3iWbRCCGNANoxIwG81gC1fg/fZRGvWJYYV6avYgPARQBk0k4OvbaGkW5BCJgyKNZtNAkBCuAFrjwv2vuYL/J0+6UU7rMfwm1IkwdSDlddOwubif1FIIxqmgd6aSbybYGBzlmFf478MTX5JGCmK5sdms3rRAkA7ABWuwCmsNu+ytVb7NiDGh+YjDAUMLugoyID9SBUZnVNggh2Q4RlUqkCd2sXrjujvjp618M0Z4Ulw9cYkdllE";

	//提现时要使用的MD5的密钥  即代付产品的密钥
    public static final String SIGNKEY_TRANSFER_MD5 = "6oRQOzdnCtQJ52hHHHPpjN0ukr3zMuCt";*/


//	public Map<String, Object> getPostJson(HttpServletRequest request) throws IOException {
//		java.io.ByteArrayOutputStream inBuffer = new java.io.ByteArrayOutputStream();
//		java.io.InputStream input = request.getInputStream();
//		byte[] tmp = new byte[1024];
//		int len = 0;
//		while ((len = input.read(tmp)) > 0) {
//			inBuffer.write(tmp, 0, len);
//		}
//		byte[] requestData = inBuffer.toByteArray();
//		String requestJsonStr = new String(requestData, "UTF-8");
//		logger.info(requestJsonStr);
//		JSONObject requestJson = JSON.parseObject(requestJsonStr);
//		logger.info(requestJson);
//		return parseJSON2Map(requestJson);
//	}
//
//	public Map<String, Object> parseJSON2Map(JSONObject json) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (json != null) {
//			for (Object k : json.keySet()) {
//				Object v = json.get(k);
//				// 如果内层还是数组的话，继续解析
//				if (v instanceof JSONArray) {
//					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//					Iterator<Object> it = ((JSONArray) v).iterator();
//					while (it.hasNext()) {
//						JSONObject json2 = (JSONObject) it.next();
//						list.add(parseJSON2Map(json2));
//					}
//					map.put(k.toString(), list);
//				} else {
//					map.put(k.toString(), v);
//				}
//			}
//		}
//		logger.info(map);
//		return map;
//	}
}

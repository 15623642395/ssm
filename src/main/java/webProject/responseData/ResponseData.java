package webProject.responseData;

import java.util.HashMap;
import java.util.Map;

/**
 * 前台调用后返回成功的json
 * 
 * @author 56525
 *
 */
public class ResponseData {

	private static final String SUCCESS_STATUS = "1";// 成功

	private static final String SUCCESS_MESSAGE = "交易成功";

	private static final String SUCCESS_CODE = "888888";

	private Map<String, Object> rspHead;

	private Map<String, Object> rspBody;

	public ResponseData() {
		Map<String, Object> map = new HashMap<>();
		map.put("status", SUCCESS_STATUS);
		map.put("code", SUCCESS_CODE);
		map.put("message", SUCCESS_MESSAGE);
		this.rspHead = map;
	}

	public Map<String, Object> getRspHead() {
		return rspHead;
	}

	public Map<String, Object> getRspBody() {
		return rspBody;
	}

	public void setDataMap(Map<String, Object> rspBody) {
		this.rspBody = rspBody;
	}

	@Override
	public String toString() {
		return "ResponseData [rspHead=" + rspHead + ", rspBody=" + rspBody + "]";
	}

}

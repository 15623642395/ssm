package webProject.responseData;

import java.util.Map;

/**
 * 用于接收前台的数据
 * 
 * @author 56525
 *
 */
public class RequestData {
	private Map<String, Object> reqHead;
	private Map<String, Object> reqBody;

	public Map<String, Object> getReqHead() {
		return reqHead;
	}

	public void setReqHead(Map<String, Object> reqHead) {
		this.reqHead = reqHead;
	}

	public Map<String, Object> getReqBody() {
		return reqBody;
	}

	public void setReqBody(Map<String, Object> reqBody) {
		this.reqBody = reqBody;
	}

	@Override
	public String toString() {
		return "RequestData [reqHead=" + reqHead + ", reqBody=" + reqBody + "]";
	}

}

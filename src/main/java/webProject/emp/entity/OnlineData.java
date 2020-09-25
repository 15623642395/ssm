package webProject.emp.entity;

import java.util.List;
import java.util.Map;

public class OnlineData {
	private String businessCode;
	private String systemId;
	private List<Map<String, Object>> params;

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public List<Map<String, Object>> getParams() {
		return params;
	}

	public void setParams(List<Map<String, Object>> params) {
		this.params = params;
	}

}

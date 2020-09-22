package javaProject.bankCall;

public enum CustomerType {
	COMMON, EXPRESS, VIP;
	// 重写toString，满足中国人将英文转换为中文
	public String toString() {
		String name = null;
		switch (this) {
		case COMMON:
			name = "普通";
			break;
		case EXPRESS:
			name = "快速";
			break;
		case VIP:
			name = name();
			break;
		}
		return name;
	}
}

package webProject.exception;

/**
 *	自定义异常,
 *		将由GlobalExceptionHandler全局异常处理器抛出给页面
 * @author 56525
 *
 */

@SuppressWarnings("serial")
public class CodeException extends Exception {
	private String code;
	private String message;

	public CodeException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

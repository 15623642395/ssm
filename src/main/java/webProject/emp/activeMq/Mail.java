package webProject.emp.activeMq;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class Mail implements Serializable {
	private static String fromHost = "smtp.qq.com"; // qq邮件发件服务器(163邮箱服务器为smtp.163.com)
	private static String port = "465";
	private static String from = "565253584@qq.com";// 发送方邮箱
	private static String password = "zozwgirdczulbfja";// 发送方邮箱对应的授权码(百度对应的邮箱授权码怎么获取)
	private String receive = "";// 接收方邮箱
	private String subject = "";// 主题名称
	private String message = "";// 邮件正文
	private String fileName = "";// 附件

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static String getFromHost() {
		return fromHost;
	}

	public static String getPort() {
		return port;
	}

	public static String getFrom() {
		return from;
	}

	public static String getPassword() {
		return password;
	}

}

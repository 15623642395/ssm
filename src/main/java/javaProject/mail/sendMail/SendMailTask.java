package javaProject.mail.sendMail;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.TimerTask;

/**
 * 定时器发送邮件
 * @author 56525
 *
 */
public class SendMailTask extends TimerTask {
	private String fromHost;
	private String port;
	private String from;
	private String password;
	private String receive;
	private String subject;
	private String message;
	private String fileName;

	public SendMailTask(String fromHost, String port, String from, String password, String receive, String subject,
			String message, String fileName) {
		this.fromHost = fromHost;
		this.port = port;
		this.from = from;
		this.password = password;
		this.receive = receive;
		this.subject = subject;
		this.message = message;
		this.fileName = fileName;
	}

	@Override
	public void run() {
		// 发送邮件
		try {
			System.out.println(SendUtil.sendMail(fromHost, port, from, password, receive, subject, message, fileName));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

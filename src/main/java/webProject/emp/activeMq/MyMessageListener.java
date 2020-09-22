package webProject.emp.activeMq;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;

import javaProject.mail.sendMail.SendUtil;

/**
 * 监听类
 * 	1、前台发送调用发送接口
 * 	2、该类监听到有一个消息会自动触发消息发送
 * @author 56525
 *
 */
public class MyMessageListener implements MessageListener {

	@Autowired
	private MyMessageConverter messageConverter;

	@SuppressWarnings({ "static-access" })
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			ObjectMessage objMessage = (ObjectMessage) message;
			try {
				Mail mail = (Mail) messageConverter.fromMessage(objMessage);
				try {
					SendUtil.sendMail(mail.getFromHost(), mail.getPort(), mail.getFrom(), mail.getPassword(),
							mail.getReceive(), mail.getSubject(), mail.getMessage(), mail.getFileName());
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (JMSException e) {
			}
		}
	}
}

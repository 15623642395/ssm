package javaProject.activemq.provider;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import javaProject.mail.sendMail.SendUtil;
/**
 * 浏览器中输入:http://192.168.10.12:8161/admin/
 * 				输入账户：admin
 * 				密码：admin进入
 * @author 56525
 *
 */
public class ActiveMqProvider {
	private static final String USERNAME = "admin";

	private static final String PASSWORD = "admin";

	private static final String BROKEN_URL = "tcp://192.168.10.12:61616";

	private ConnectionFactory connectionFactory;

	private Connection connection;

	private Session session;

	private Queue queue;

	private MessageProducer producer;

	public void init() {
		try {
			// 创建一个链接工厂
			connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
			// 从工厂中创建一个链接
			connection = connectionFactory.createConnection();
			// 启动链接,不启动不影响消息的发送，但影响消息的接收
			connection.start();
			// 创建一个事物session
			session = connection.createSession(true, Session.SESSION_TRANSACTED);
			// 获取消息发送的目的地，指消息发往那个地方
			queue = session.createQueue("sendMail");
			// 获取消息发送的生产者
			producer = session.createProducer(queue);
			// 发送消息
			SendUtil sendUtil = new SendUtil();
			ObjectMessage msg = session.createObjectMessage(sendUtil);
			producer.send(msg);
			session.commit();
			System.out.println("生产完成！");
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (session != null) {
					session.close();
				}
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ActiveMqProvider activeMqProvider = new ActiveMqProvider();
		activeMqProvider.init();
	}
}

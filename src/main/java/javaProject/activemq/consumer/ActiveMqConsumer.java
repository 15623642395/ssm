package javaProject.activemq.consumer;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javaProject.mail.sendMail.SendUtil;

public class ActiveMqConsumer {

	private static final String USERNAME = "admin";

	private static final String PASSWORD = "admin";

	private static final String BROKEN_URL = "tcp://192.168.10.12:61616";

	private ConnectionFactory connectionFactory;

	private ActiveMQConnection connection;

	private Session session;

	private Queue queue;

	private MessageConsumer consumer;

	@SuppressWarnings("static-access")
	public void init() throws GeneralSecurityException, IOException {
		try {
			// 创建一个链接工厂
			connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEN_URL);
			// 从工厂中创建一个链接
			connection = (ActiveMQConnection) connectionFactory.createConnection();

			// 启动链接
			connection.start();
			// 创建一个事物session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 获取消息接收的目的地，指从哪里接收消息
			queue = session.createQueue("sendMail");
			// 获取消息接收的消费者
			consumer = session.createConsumer(queue);
			System.out.println(consumer);
			// 打印接收的消息,并设置读取为5秒，队列中没有消息，消费者一直阻塞
			ObjectMessage msg = (ObjectMessage) consumer.receive(5*1000);
			//也可以设置为自动消费
//			consumer.setMessageListener(new MessageListener() {
//				
//				@Override
//				public void onMessage(Message message) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
			// 发送邮件参数
			String fromHost = "smtp.qq.com"; // qq邮件发件服务器(163邮箱服务器为smtp.163.com)
			String port = "465";
			String from = "565253584@qq.com";// 发送方邮箱
			String password = "zozwgirdczulbfja";// 发送方邮箱对应的授权码(百度对应的邮箱授权码怎么获取)
			String receive = "zh15623642395@163.com";// 接收方邮箱
			String subject = "新浪邮箱邮件发送";// 主题名称
			String message = "新浪邮箱邮件发送";// 邮件正文
			// 附件名称和附件路径
			String fileName = "C:" + File.separator + "Users" + File.separator + "56525" + File.separator + "Desktop"
					+ File.separator + "1.xls";
			// 邮件发送
			if (msg != null) {
				SendUtil sendUtil=(SendUtil)msg.getObject();
				System.out.println(sendUtil.sendMail(fromHost, port, from, password, receive, subject, message, fileName));

			}
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

	public static void main(String[] args) throws GeneralSecurityException, IOException {
		ActiveMqConsumer activeMqConsumer = new ActiveMqConsumer();
		activeMqConsumer.init();
	}
}
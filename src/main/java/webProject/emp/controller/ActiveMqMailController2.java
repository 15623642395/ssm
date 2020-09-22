package webProject.emp.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javaProject.mail.sendMail.SendUtil;
import javaProject.utils.HttpUtil;
import webProject.emp.activeMq.Mail;
import webProject.responseData.ResponseData;

/**
 * 使用activeMq生产者生产消息和消费者消费消息分开
 * 	1、前台调用发送方法之后
 * 	2、浏览器中输入:http://192.168.10.12:8161/admin/
 * 		输入账户：admin
 * 		密码：admin进入
 * 	查看消费情况
 *
 *		createSession(paramA,paramB);
 *			1、paramA是设置事务的，paramB设置acknowledgment mode
 *			2、paramA设置为false时：paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。 
    		3、paramA设置为true时：paramB的值忽略， acknowledgment mode被jms服务器设置为SESSION_TRANSACTED 。 
    		4、Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。 
    		5、Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会删除消息。 
    		6、DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效。		
 * @author 56525
 *
 */
@Controller
@RequestMapping("/activeMq")
public class ActiveMqMailController2 {
	private Logger logger = LogManager.getLogger(ActiveMqMailController1.class.getName());

	@Autowired
	private ActiveMQConnectionFactory activeMQConnectionFactory;

	/*
	 * 消费者手动消费消息
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/consumer.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData consumer() throws JmsException, GeneralSecurityException, IOException, JMSException {
		ResponseData responseData = new ResponseData();
		logger.info("进入手动消费消息");
		// 1、从工厂中创建一个链接
		ActiveMQConnection connection = (ActiveMQConnection) activeMQConnectionFactory.createConnection();
		// 2、启动链接
		connection.start();
		// 3、创建一个事物session，设置消息消费后的确认方式
		// 设置手动消费：Session.AUTO_ACKNOWLEDGE、或CLIENT_ACKNOWLEDGE
		// 设置为Session.AUTO_ACKNOWLEDGE，默认的消息确认机制，性能较慢
		// 设置为CLIENT_ACKNOWLEDGE需要消费完成之后调用Message的acknowledge()方法删除消费的消息
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		// 4、获取消息接收的目的地，指从哪里接收消息
		Queue queue = session.createQueue("mQEmail");
		// 5、获取消息接收的消费者
		MessageConsumer consumer = session.createConsumer(queue);
		// 6、获取消息，5秒内获取不到则停止获取，防止阻塞
		ObjectMessage objMessage = (ObjectMessage) consumer.receive(5 * 1000);
		try {
			// 7、将消息转换为需要发送的类型
			Mail mail = (Mail) objMessage.getObject();
			// 8、处理消息
			SendUtil.sendMail(mail.getFromHost(), mail.getPort(), mail.getFrom(), mail.getPassword(), mail.getReceive(),
					mail.getSubject(), mail.getMessage(), mail.getFileName());
			// 9、删除MQ中已经消费的消息
			objMessage.acknowledge();
		} catch (Exception e) {
			if (e.getMessage() == null) {
				logger.info("队列中没有可被消费的消息");
			} else {
				logger.info("消费消息错误,错误原因:" + e.getMessage());
			}
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
		logger.info("客户端消费消息完成！");
		return responseData;
	}

	public static void main(String[] args) {
		HttpUtil.post("http://localhost:8080/ssm/activeMq/consumer.do", "{ \"id\":\"12345\" }");
	}
}

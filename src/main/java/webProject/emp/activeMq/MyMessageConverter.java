package webProject.emp.activeMq;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * 消息的发送和接收处理类
 * 	:在mq.xml配置
 * @author 56525
 *
 */
public class MyMessageConverter implements MessageConverter {
	Logger logger = LogManager.getLogger(MyMessageConverter.class.getName());

	/*
	 * 接收前台推送的消息到消息队列中
	 */
	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		logger.info("我是MQ我接收前台的消息");
		return session.createObjectMessage((Serializable) object);
	}

	/*
	 * 通过监听器消费消息队列
	 */
	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		ObjectMessage objMessage = (ObjectMessage) message;
		//异步不能使用logger打印日志
		System.out.println("我是MQ我被监听后发送消息");
		return objMessage.getObject();
	}

}

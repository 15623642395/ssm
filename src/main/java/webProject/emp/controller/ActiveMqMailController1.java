package webProject.emp.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javaProject.utils.HttpUtil;
import webProject.emp.activeMq.Mail;
import webProject.responseData.ResponseData;

/**
 * activemq默认是自动确认消费机制，即消费者接收了此消息，此消息便从待消费队列中剔除，进入已消费队列。
 * 
 * 使用activeMq进行邮件发送
 * 	1、前台调用发送方法之后
 * 	2、浏览器中输入:http://192.168.10.12:8161/admin/
 * 		输入账户：admin
 * 		密码：admin进入
 * 
 * 	查看消费情况
 * 	3、将监听器注掉，可以通过ActiveMqMailController2手动消费消息，每个用户就是一个消费者
 * 
 * 注：该controller每个用户是一个生产者，消费者会自动消费消息,将配置文件中的监听器去掉即可以手动消费
 *
 * @author 56525
 *
 */
@Controller
@RequestMapping("/activeMq")
public class ActiveMqMailController1 {
	@Autowired
	private JmsTemplate jmsTemplate;
	private Logger logger = LogManager.getLogger(ActiveMqMailController1.class.getName());

	/*
	 * 消费者自动消费生产者生产的消息
	 */
	@RequestMapping(value = "/sendMail.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData sendMessage(@RequestBody Mail mail) throws JmsException, GeneralSecurityException, IOException {
		ResponseData responseData = new ResponseData();
		logger.info("向MQ[MyMessageConverter-->toMessage]中推送消息");
		jmsTemplate.convertAndSend(mail);
		return responseData;
	}
	
	public static void main(String[] args) {
		HttpUtil.post("http://localhost:8080/ssm/activeMq/sendMail.do", "{ \"receive\":\"565253584@qq.com\",\"subject\":\"activeMq邮件发送\",\"message\":\"activeMq邮件发送\"  }");
	}
}

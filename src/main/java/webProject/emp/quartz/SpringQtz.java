package webProject.emp.quartz;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import javaProject.utils.SendUtil;


/**
 * spring整合Quartz
 *   spring-webmvc包为4.3.14
 *   quartz包要在2.0以上
 *   spring整合quartz配置文件spring-quartz.xml
 *   定时发送邮件
 * @author 56525
 *
 */
@Component
public class SpringQtz {

	protected void execute() throws GeneralSecurityException, IOException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("定时任务执行:" + sdf.format(date));
		// 发送邮件参数
		String fromHost = "smtp.qq.com"; // qq邮件发件服务器(163邮箱服务器为smtp.163.com)
		String port = "465";
		String from = "565253584@qq.com";// 发送方邮箱
		String password = "zozwgirdczulbfja";// 发送方邮箱对应的授权码(百度对应的邮箱授权码怎么获取)
		String receive = "zh15623642395@163.com";// 接收方邮箱
		String subject = "新浪邮箱邮件发送并保存测试12345678999";// 主题名称
		String message = "新浪邮箱邮件发送并保存测试12345678999";// 邮件正文
		// 附件名称和附件路径
		String fileName = "";
		System.out.println(
				"执行返回值:" + SendUtil.sendMail(fromHost, port, from, password, receive, subject, message, fileName));
	}
}

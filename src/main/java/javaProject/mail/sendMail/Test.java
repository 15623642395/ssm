package javaProject.mail.sendMail;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 因为host，from，pass，receive，subject，msg，filename都能变化所以做成可变的 需要联网测试
 * 
 * @author 56525
 *
 */
public class Test {

	public static void main(String[] args) throws GeneralSecurityException, IOException {
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
		System.out.println(SendUtil.sendMail(fromHost, port, from, password, receive, subject, message, fileName));
		// 使用触发器发送邮件,第一次等待10秒开始触发，每10秒触发一次
		// Timer timer = new Timer();
		// timer.schedule(new SendMailTask(fromHost, port, from, password,
		// receive, subject, message, fileName), 10000,
		// 10000);
	}

}

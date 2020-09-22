package javaProject.mail.sendMail;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 发送给邮件，并保存到指定文件夹中 排除163邮箱，因为163邮箱回自动在已发送中有记录
 * 注：pop3协议只能获取收件箱文件间INBOX,Imap协议能获取所有文件夹，所以用IMAP协议
 * 
 * @author 56525
 *
 */
@SuppressWarnings("serial")
public class SendUtil implements Serializable{
	/*
	 * 收件人的邮箱、主题、内容、附件
	 */
	public static boolean sendMail(String host, String port, final String from, final String pass, String receive,
			String subject, String msg, String filename) throws GeneralSecurityException, IOException {
		Properties properties = new Properties();
		// 邮箱的发送服务器地址
		properties.put("mail.smtp.host", host);
		// 邮箱发送服务器端口,这里设置端口
		properties.put("mail.smtp.prot", port);
		properties.put("mail.smtp.auth", "true");// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
		// 465端口采用ss加密
		if ("465".equals(port)) {
			MailSSLSocketFactory sfFactory = null;
			sfFactory = new MailSSLSocketFactory();
			// 免安装安全证书，信任任何服务器
			sfFactory.setTrustAllHosts(true);
			properties.put("mail.smtp.ssl.trust", "*");
			// 开启SSL安全发送
			properties.put("mail.smtp.enable", "true");
			properties.put("mail.smtp.ssl.socketFactory", sfFactory);
			properties.setProperty("mail.smtp.socketFactory.fallback", "false");
			properties.put("mail.smtp.socketFactory.port", port);
		}
		// 超时时间也要设置,三个时间都设置
		properties.put("mail.smtp.timeout", "20000");
		properties.put("mail.smtp.connectiontimeout", "20000");
		properties.put("mail.smtp.writetimeout", "20000");
		// 获取默认session对象,不能使用该方式，使用下面的，该方式会存在缓存
		/*
		 * Session session = Session.getInstance(properties, new Authenticator()
		 * { public PasswordAuthentication getPasswordAuthentication() { // //
		 * qq邮箱服务器账户、第三方登录授权码 return new PasswordAuthentication(from, pass); //
		 * 发件人邮件用户名、密码 } });
		 */
		Session session = Session.getInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() { //
				// qq邮箱服务器账户、第三方登录授权码
				return new PasswordAuthentication(from, pass); // 发件人邮件用户名、密码
			}
		});
		// session.setDebug(true);
		try {
			// 创建默认的 MimeMessage 对象，用于发送邮件
			MimeMessage message = new MimeMessage(session);
			// Set From: 头部头字段
			message.setFrom(new InternetAddress(from, "祝浩", "utf-8"));
			// Set To: 头部头字段
			String[] receives = receive.split(",");
			for (int i = 0; i < receives.length; i++) {
				// 发送后显示收件人邮箱
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receives[i]));
			}
			// Set Subject: 主题文字
			message.setSubject(subject);
			// 创建消息部分，包括正文内容和附件
			BodyPart messageBodyPart = new MimeBodyPart();
			// 消息正文
			messageBodyPart.setText(msg);
			// 创建多重消息(用于组装邮件正文、附件部分)
			Multipart multipart = new MimeMultipart();
			// 设置文本消息部分
			multipart.addBodyPart(messageBodyPart);
			// 发送多个附件
			if (!"".equals(filename) && filename != null) {
				String[] filenames = filename.split(",");
				for (int i = 0; i < filenames.length; i++) {
					// 附件部分
					messageBodyPart = new MimeBodyPart();
					// 设置要发送附件的文件路径
					DataSource source = new FileDataSource(filenames[i]);
					messageBodyPart.setDataHandler(new DataHandler(source));
					// 处理附件名称中文（附带文件路径）乱码问题
					System.out.println("附件名称：" + filenames[i]);
					// 设置文件名称附件名称，例：C:\Users\56525\Desktop\测试2.xls变为测试2.xls
					// 这是window系统下的截取，转为linux系统需要修改
					int start = filenames[i].lastIndexOf(File.separator) + 1;
					String fileName = filenames[i].substring(start, filenames[i].length());
					System.out.println("最终附件名称：" + fileName);
					System.out.println("-----------------------------------------------------");
					messageBodyPart.setFileName(MimeUtility.encodeText(fileName));
					multipart.addBodyPart(messageBodyPart);
				}
			}
			// 发送完整消息
			message.setContent(multipart);
			// 保存上述所有配置
			message.saveChanges();
			// 发送消息send()显示收件人邮箱;sendMessage不显示收件人邮箱
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			System.out.println(e.toString());
			System.out.println("错误原因:" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取文件夹 注：INBOX为收件箱文件夹，Sent Messages为已发送文件夹(当然各个服务器的名称不一定一样比如qq和163)
	 * 
	 * @param from
	 * @param password
	 * @param protocol
	 * @param host
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public Folder getMail(String from, String password, String protocol, String host, String port)
			throws MessagingException, IOException {
		IMAPStore store = null;
		IMAPFolder folder = null;
		try {
			Properties prop = System.getProperties();
			prop.put("mail.store.protocol", protocol);
			prop.put("mail.imap.host", host);
			prop.put("mail.imap.port", port);
			Session session = Session.getInstance(prop);
			store = (IMAPStore) session.getStore(protocol); // 使用imap会话机制，连接服务器
			store.connect(from, password);

			/**
			 * 列出所有的默认文件夹，以后方放在指定文件夹中
			 */
			Folder defaultFolder = store.getDefaultFolder();
			Folder[] folders = defaultFolder.list();
			for (int i = 0; i < folders.length; i++) {
				System.out.println("默认文件夹：" + folders[i].getName());
			}
			folder = (IMAPFolder) store.getFolder("Sent Messages"); // 获取已发送文件夹
			// 判断文件夹是否存在,不存在则创建文件夹(很奇怪，新创建的该文件夹就是已发送文件夹，其实上述是获取不到该文件夹的)
			if (!folder.exists()) {
				folder.create(Folder.HOLDS_MESSAGES);
			}
			folder.open(Folder.READ_WRITE);
			System.out.println("获取的文件夹名称:" + folder.getFullName());
		} catch (Exception e) {
			System.out.println("错误消息：" + e.getMessage());
		} finally {
			// 释放资源
			if (folder != null) {
				folder.close(true);
			}
			if (store != null) {
				store.close();
			}
		}
		return folder;
	}

	/**
	 * 保存邮件到getMail()中指定的文件夹
	 * 
	 * @param message
	 *            邮件信息
	 */
	@SuppressWarnings("unused")
	private void saveEmailToSentMailFolder(Message message, String from, String imapPass, String imap, String imapHost,
			String imapPort) {
		Store store = null;
		Folder sentFolder = null;
		try {
			sentFolder = getMail(from, imapPass, imap, imapHost, imapPort);
			message.setFlag(Flag.SEEN, true); // 设置已读标志
			sentFolder.appendMessages(new Message[] { message });
			System.out.println("已保存到文件夹中");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 判断发件文件夹是否打开如果打开则将其关闭
			if (sentFolder != null && sentFolder.isOpen()) {
				try {
					sentFolder.close(true);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			// 判断邮箱存储是否打开如果打开则将其关闭
			if (store != null && store.isConnected()) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

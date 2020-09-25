package javaProject.mail.getMail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * 通过IMAP协议获取收件箱中所有文件的文件
 * 
 * @author 56525
 *
 */
public class GetMail {

	public boolean getMail(String from, String password, String protocol, String host, String port)
			throws MessagingException, IOException {
		IMAPStore store = null;
		IMAPFolder folder = null;
		try {
			Properties prop = System.getProperties();
			prop.put("mail.store.protocol", protocol);
			prop.put("mail.imap.host", host);
			prop.put("mail.imap.port", port);
			Session session = Session.getInstance(prop);
			int total = 0;
			store = (IMAPStore) session.getStore(protocol); // 使用imap会话机制，连接服务器
			store.connect(from, password);
			folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
			folder.open(Folder.READ_WRITE);
			// 获取总邮件数
			total = folder.getMessageCount();
			System.out.println("-----------------共有邮件：" + total + " 封--------------");
			// 得到收件箱文件夹信息，获取邮件列表
			System.out.println("未读邮件数：" + folder.getUnreadMessageCount());
			Message[] messages = folder.getMessages();
			int messageNumber = 0;
			for (Message message : messages) {
				System.out.println("发送时间：" + message.getSentDate());
				System.out.println("主题：" + message.getSubject());
				System.out.println("内容：" + message.getContent());
				Flags flags = message.getFlags();
				if (flags.contains(Flags.Flag.SEEN))
					System.out.println("这是一封已读邮件");
				else {
					System.out.println("未读邮件");
				}
				System.out.println("========================================================");
				System.out.println("========================================================");
				// 每封邮件都有一个MessageNumber，可以通过邮件的MessageNumber在收件箱里面取得该邮件
				messageNumber = message.getMessageNumber();
			}
			Message message = folder.getMessage(messageNumber);
			System.out.println(message.getContent() + message.getContentType());
		} catch (Exception e) {
			System.out.println("错误消息：" + e.getMessage());
			return false;
		} finally {
			// 释放资源
			if (folder != null)
				folder.close(true);
			if (store != null)
				store.close();
		}
		return true;
	}
}

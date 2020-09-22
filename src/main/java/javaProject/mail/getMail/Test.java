package javaProject.mail.getMail;

import java.io.IOException;

import javax.mail.MessagingException;

public class Test {
	public static void main(String[] args) throws MessagingException, IOException {
		String from = "565253584@qq.com";// 邮箱的用户名
		String password = "jxfiwexrheikbdhb"; // imap授权码
		String protocol = "imap";
		String host = "imap.qq.com";
		String port = "143";
		GetMail getMail = new GetMail();
		System.out.println(getMail.getMail(from, password, protocol, host, port));
	}
}

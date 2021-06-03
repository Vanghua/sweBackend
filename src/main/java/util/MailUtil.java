package util;

import java.util.*;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;

public class MailUtil{
	private String from;
	private String host;
	private String password;
	private Session session;
	
	public MailUtil(String _from, String _password) {
		from = _from;
		password = _password;
		if(Pattern.compile("^.*@qq.com$").matcher(from).matches()) {
			host = "smtp.qq.com";
		}else if(Pattern.compile("^.*@163.com$").matcher(from).matches()) {
			host = "smtp.163.com";
		}else if(Pattern.compile("^.*@126.com$").matcher(from).matches()) {
			host = "smtp.126.com";
		}else {
			JOptionPane.showConfirmDialog(null, "仅支持QQ/163/126邮箱作为发件人", "发件人错误", JOptionPane.CLOSED_OPTION);
		}
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		/*-启用465端口发送邮件，阿里云服务器禁用默认的25端口-*/
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.ssl.enable", "true");
		/*---*/
		properties.put("mail.smtp.auth", "true");
		session = Session.getDefaultInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
	}
	
	public void sendMessage(String to, String subject, String text, String attachment) {
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject(subject);

			Multipart multipart = new MimeMultipart();
			
			BodyPart textPart = new MimeBodyPart();
			textPart.setText(text);
			
			if(attachment != null) {
				BodyPart attachPart = new MimeBodyPart();
				FileDataSource source = new FileDataSource(attachment);
				attachPart.setDataHandler(new DataHandler(source));
				attachPart.setFileName(attachment);
				multipart.addBodyPart(attachPart);
			}
			
			multipart.addBodyPart(textPart);
			message.setContent(multipart);
			
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}

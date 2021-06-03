package controller;

import util.JdbcUtilV2;
import util.MailUtil;

public class Global {
	// database configuration
	public static String databaseDriver = "com.mysql.cj.jdbc.Driver";
	public static String databaseUrl = "jdbc:mysql://rm-uf69u28mol7no0lm8ko.mysql.rds.aliyuncs.com:3306/soft";
	public static String databaseUser = "soft_admin";
	public static String databasePasswd = "soft_admin";
	public static JdbcUtilV2 ju = new JdbcUtilV2(databaseDriver, databaseUrl, databaseUser, databasePasswd);
	
	// mail configuration
	public static String mailFrom = "2416116991@qq.com";
	public static String mailPassword = "bgzbueqduyqzdjgh";
	public static MailUtil mu = new MailUtil(mailFrom, mailPassword);	
	
}

package controller;

import java.util.HashMap;

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
	
	// account type dictionary
	public static HashMap<String, String> ChineseType = new HashMap<String, String>();
	static {
		ChineseType.put("all", "全局管理员");
		ChineseType.put("user", "普通用户");
		ChineseType.put("trans", "运输员");
		ChineseType.put("assign", "调度管理员");
		ChineseType.put("order", "订单管理员");
		ChineseType.put("people", "人事管理员");
		ChineseType.put("financial", "财务管理员");
		ChineseType.put("warehouse", "仓库管理员");
	}

	// good priority dictionary
	public static HashMap<String, Integer> goodPriorityDict = new HashMap<>();
	static{
		goodPriorityDict.put("日用品", 1);
		goodPriorityDict.put("食品", 2);
		goodPriorityDict.put("文件", 2);
		goodPriorityDict.put("衣物", 1);
		goodPriorityDict.put("数码产品", 3);
		goodPriorityDict.put("贵重物品", 5);
		goodPriorityDict.put("其它", 3);
	}
}

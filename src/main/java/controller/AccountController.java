package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import accountSystem.trans.EmailInfo;
import accountSystem.trans.LoginInfo;
import accountSystem.trans.RegisterInfo;

/*
 * 1. 用户输入：用户名、密码、验证邮箱
 * 2. 系统正则检测
 * 3. 用户点击发送验证码的请求：Post email -> 系统检测是否发送成功（如果已存在则发送失败）
 * 4. 用户点击注册按钮（这里分为普通用户的注册请求和员工用户的注册请求，对于普通用户，身份授权码部分为""）
 */


@RestController
public class AccountController {
	@PostMapping("api/account/register")
	public String register(@RequestBody RegisterInfo registerInfo) {
		if(Global.ju.exists("select * "
				+ "from account "
				+ "where account_name = ?", registerInfo.getAccountName())) {
			
			return "用户名已被使用";
		}else if(Global.ju.exists("select * "
				+ "from account "
				+ "where account_email = ?", registerInfo.getAccountEmail())) {
			
			return "邮箱已被使用";
		}else {
			if(Global.ju.exists("select * "
					+ "from validation "
					+ "where account_email = ? and validation_code = ?", 
					registerInfo.getAccountEmail(), 
					registerInfo.getValidationCode())) { // 检验验证码
				
				if(registerInfo.getAccountType().equals("user")) { // 普通用户
					Global.ju.execute("insert into account values(?,?,?,?)", 
							registerInfo.getAccountName(), 
							registerInfo.getAccountPassword(), 
							registerInfo.getAccountEmail(),
							registerInfo.getAccountType());	
					
					return registerInfo.getAccountType();
				}else { // 员工用户
					if(Global.ju.exists("select * "
							+ "from authority "
							+ "where authority_type = ? and authority_code = ?", 
							registerInfo.getAccountType(),
							registerInfo.getAuthorityCode())) { // 检验授权码是否能够注册该身份的员工
						
						Global.ju.execute("insert into account values(?,?,?,?)", 
								registerInfo.getAccountName(),
								registerInfo.getAccountPassword(),
								registerInfo.getAccountEmail(),
								registerInfo.getAccountType());
						
						return registerInfo.getAccountType();
					}else {
						return "授权码无效";
					}
				}
			}else {
				return "验证码无效";
			}
		}
	}
	
	@PostMapping("api/account/login")
	public String login(@RequestBody LoginInfo loginInfo) {
		ArrayList<HashMap<String, Object>> resultList = 
				Global.ju.query("select account_type "
						+ "from account "
						+ "where account_name = ? and account_password = ?", 
						loginInfo.getAccountName(), 
						loginInfo.getAccountPassword());
		
		if(!resultList.isEmpty()) {
			return (String) resultList.get(0).get("account_type");
		}else {
			return "错误";
		}
	}
	
	@PostMapping("api/account/getValidation")
	public String getValidation(@RequestBody EmailInfo emailInfo) {
		String sql = "select get_validation(?) as result";
		ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql, emailInfo.getEmail());
		String result = (String)resultList.get(0).get("result");
		if(result.equals("邮箱已存在!")) {
			return "已存在";
		}else {
			Global.mu.sendMessage(emailInfo.getEmail(), "注册验证码", result, null);
			return "发送成功";
		}
	}
}

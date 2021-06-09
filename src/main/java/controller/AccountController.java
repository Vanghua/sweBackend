package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import accountSystem.trans.AccountNameInfo;
import accountSystem.trans.EmailInfo;
import accountSystem.trans.ForgetPasswordInfo;
import accountSystem.trans.LoginInfo;
import accountSystem.trans.ModifyWithOldPasswordInfo;
import accountSystem.trans.ModifyWithoutOldPassword;
import accountSystem.trans.PersonalInfo;
import accountSystem.trans.RegisterInfo;
import accountSystem.trans.ResetEmailInfo;
import accountSystem.trans.ui.TotalAccountInfo;


@RestController
public class AccountController {
	
	@PostMapping("api/account/register")
	public String register(@RequestBody RegisterInfo registerInfo) { // 注册
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
					Global.ju.execute("insert into account(account_name, account_password, "
							+ "account_email, account_type) values(?,?,?,?)", 
							registerInfo.getAccountName(), 
							registerInfo.getAccountPassword(), 
							registerInfo.getAccountEmail(),
							registerInfo.getAccountType());	
					
					// 验证邮箱一次有效,使用后删除
					Global.ju.execute("delete from validation "
							+ "where account_email = ?", 
							registerInfo.getAccountEmail());
					
					return registerInfo.getAccountType();
				}else { // 员工用户
					if(Global.ju.exists("select * "
							+ "from authority "
							+ "where authority_type = ? and authority_code = ?", 
							registerInfo.getAccountType(),
							registerInfo.getAuthorityCode())) { // 检验授权码是否能够注册该身份的员工
						
						Global.ju.execute("insert into account(account_name, account_password, "
								+ "account_email, account_type) values(?,?,?,?)", 
								registerInfo.getAccountName(),
								registerInfo.getAccountPassword(),
								registerInfo.getAccountEmail(),
								registerInfo.getAccountType());
						
						// 验证邮箱一次有效,使用后删除
						Global.ju.execute("delete from validation "
								+ "where account_email = ?", 
								registerInfo.getAccountEmail());
						
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
	public String login(@RequestBody LoginInfo loginInfo) { // 登录
		String sql = "select account_type "
				+ "from account "
				+ "where account_name = ? and account_password = ?";
		
		ArrayList<HashMap<String, Object>> resultList = 
				Global.ju.query(sql, 
						loginInfo.getAccountName(), 
						loginInfo.getAccountPassword());
		
		if(!resultList.isEmpty()) {
			return (String) resultList.get(0).get("account_type");
		}else {
			return "错误";
		}
	}
	
	@PostMapping("api/account/getValidation")
	public String getValidation(@RequestBody EmailInfo emailInfo) { // 请求发送验证码
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

	@PostMapping("api/account/forgetPassword") 
	public String forgetPassword(@RequestBody ForgetPasswordInfo forgetPasswordInfo) { // 请求发送用于密码找回的验证码
		String sql = "select * from account where account_name = ? and account_email = ?";
		if(!Global.ju.exists(sql, 
				forgetPasswordInfo.getAccountName(), forgetPasswordInfo.getAccountEmail())) {
			
			return "用户名或验证邮箱错误";
		}else {
			sql = "select get_forgetpassword_validation(?) as result";
			ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql, 
					forgetPasswordInfo.getAccountName());
			String result = (String) resultList.get(0).get("result");
			Global.mu.sendMessage(forgetPasswordInfo.getAccountEmail(), "找回密码", result, null);
			return "验证码发送成功";
		}
	}
	
	@PostMapping("api/account/modifyPasswordWithOldPassword")
	public String modifyPasswordWithOldPassword(@RequestBody ModifyWithOldPasswordInfo modifyWithOldPasswordInfo) { // 修改密码
		String sql = "select * from account where account_name = ? and account_password = ?";
		if(Global.ju.exists(sql, modifyWithOldPasswordInfo.getAccountName(), modifyWithOldPasswordInfo.getOldPassword())) {
			
			Global.ju.execute("update account set account_password = ? where account_name = ?", 
					modifyWithOldPasswordInfo.getNewPassword(), modifyWithOldPasswordInfo.getAccountName());
			return "修改成功";
		}else {
			return "用户名或密码错误";
		}
	}
	
	@PostMapping("api/account/modifyPasswordWithoutOldPassword")
	public String modifyPasswordWithoutOldPassword(@RequestBody ModifyWithoutOldPassword modifyWithoutOldPassword) { // 忘记密码
		if(Global.ju.exists("select * from forget_password_validation where account_name = ? and validation_code = ?", 
				modifyWithoutOldPassword.getAccountName(),
				modifyWithoutOldPassword.getForgetValidation())) {
			
			String sql = "update account set account_password = ? where account_name = ?";
			Global.ju.execute(sql, modifyWithoutOldPassword.getNewPassword(), modifyWithoutOldPassword.getAccountName());
			return "修改成功";
		}else {
			return "用户名或验证码错误";
		}	
	}
	
	@PostMapping("api/account/updatePersonalInfo")
	public String updatePersonalInfo(@RequestBody PersonalInfo personalInfo) { // 更新个人信息
		String sql = "update account set true_name = ?, telephone = ? where account_name = ?";
		Global.ju.execute(sql, personalInfo.getTrueName(), personalInfo.getTelephone(), personalInfo.getAccountName());
		return "成功";
	}
	
	@PostMapping("api/account/getResetEmailValidation")
	public String getResetEmailValidation(@RequestBody EmailInfo emailInfo) { // 请求获取重置邮箱的验证码
		 ArrayList<HashMap<String, Object>> resultList = 
				 Global.ju.query("select get_reset_email_validation(?) as result", 
						 emailInfo.getEmail());
		 
		 String result = (String) resultList.get(0).get("result");
		 
		 if(result.equals("邮箱已存在!")) {
			 return "失败";
		 }else {
			 Global.mu.sendMessage(emailInfo.getEmail(), "更改验证邮箱" , result, null);
			 return "已发送";
		 }
	}
	
	@PostMapping("api/account/resetEmail")
	public String resetEmail(@RequestBody ResetEmailInfo resetEmailInfo) { // 输入验证码，更新验证邮箱
		String sql = "select * from reset_email_validation "
				+ "where account_email = ? and validation_code = ?";
		if(Global.ju.exists(sql, 
				resetEmailInfo.getAccountEmail(), resetEmailInfo.getValidationCode())) {
			
			Global.ju.execute("update account set account_email = ? where account_name = ?", 
					resetEmailInfo.getAccountEmail(),
					resetEmailInfo.getAccountName());
			
			// 一个邮箱仅一次,更改后删除
			Global.ju.execute("delete from reset_email_validation where account_email = ?", 
					resetEmailInfo.getAccountEmail());
			
			return "成功";
		}else {
			return "错误"; // 验证码错误
		}
	}
	
	@PostMapping("api/account/ui/updateTotalAccountInfo")
	public TotalAccountInfo updateTotalAccountInfo(@RequestBody AccountNameInfo accountNameInfo) { // 更新基本信息UI
		String sql = "select account_email, account_type, true_name, telephone "
				+ "from account "
				+ "where account_name = ?";
		
		ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql, 
				accountNameInfo.getAccountName());
		
		
		TotalAccountInfo result = new TotalAccountInfo();
		
		result.setAccountName(accountNameInfo.getAccountName());
		result.setAccountEmail((String) resultList.get(0).get("account_email"));
		result.setAccountType( Global.ChineseType.get((String) resultList.get(0).get("account_type")) );
		result.setTrueName((String) resultList.get(0).get("true_name"));
		result.setTelephone((String) resultList.get(0).get("telephone"));
		
		return result;
	}
	
}

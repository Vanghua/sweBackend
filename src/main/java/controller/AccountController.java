package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
//	@PostMapping("account/register")
//	public String register(@RequestBody ) {
//		
//	}
//	
//	@PostMapping("account/login")
//	public String login() {
//		
//	}
	
	@PostMapping("account/getValidation")
	public String getValidation(@RequestBody String email) {
		String sql = "select get_validation(?) as result";
		ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql, email);
		String result = (String)resultList.get(0).get("result");
		if(result.equals("邮箱已存在!")) {
			return "已存在";
		}else {
			return result;
		}
	}
}

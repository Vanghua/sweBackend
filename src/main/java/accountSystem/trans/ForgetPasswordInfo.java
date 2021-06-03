package accountSystem.trans;

public class ForgetPasswordInfo { // 未登录用户点击忘记密码时发送
	private String accountName;
	private String accountEmail;
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountEmail() {
		return accountEmail;
	}
	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}
	// ddd
}

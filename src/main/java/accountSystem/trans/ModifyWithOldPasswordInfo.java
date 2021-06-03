package accountSystem.trans;

public class ModifyWithOldPasswordInfo { // 已登录用户点击修改密码时发送
	private String accountName;
	private String oldPassword;
	private String newPassword;
	public ModifyWithOldPasswordInfo() {}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}

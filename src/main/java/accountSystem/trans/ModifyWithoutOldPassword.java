package accountSystem.trans;

public class ModifyWithoutOldPassword {
	private String accountName;
	private String newPassword;
	private String forgetValidation;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getForgetValidation() {
		return forgetValidation;
	}
	public void setForgetValidation(String forgetValidation) {
		this.forgetValidation = forgetValidation;
	}
	//ddw
}

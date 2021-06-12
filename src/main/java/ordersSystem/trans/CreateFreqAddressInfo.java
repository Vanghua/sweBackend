package ordersSystem.trans;

public class CreateFreqAddressInfo { // 创建 常用地址簿信息 的接口
	private String accountName;
	private String freqType; // 用户按下按钮即决定：是创建收件人还是创建发件人
	
	private String freqName;
	private String freqPhone;
	private String[] freqAddress;
	private String freqDetailAddress;
	
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFreqType() {
		return freqType;
	}
	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}
	public String getFreqName() {
		return freqName;
	}
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	public String getFreqPhone() {
		return freqPhone;
	}
	public void setFreqPhone(String freqPhone) {
		this.freqPhone = freqPhone;
	}
	public String[] getFreqAddress() {
		return freqAddress;
	}
	public void setFreqAddress(String[] freqAddress) {
		this.freqAddress = freqAddress;
	}
	public String getFreqDetailAddress() {
		return freqDetailAddress;
	}
	public void setFreqDetailAddress(String freqDetailAddress) {
		this.freqDetailAddress = freqDetailAddress;
	}
	
	
}

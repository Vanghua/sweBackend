package ordersSystem.trans;

public class CreateOrdersInfo {
	private String accountName;
	private String goodType;
	private String senderInfoId;
	private String receiverInfoId;
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getGoodType() {
		return goodType;
	}
	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}
	public String getSenderInfoId() {
		return senderInfoId;
	}
	public void setSenderInfoId(String senderInfoId) {
		this.senderInfoId = senderInfoId;
	}
	public String getReceiverInfoId() {
		return receiverInfoId;
	}
	public void setReceiverInfoId(String receiverInfoId) {
		this.receiverInfoId = receiverInfoId;
	}
}

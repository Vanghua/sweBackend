package ordersSystem.trans;

public class WaitCheckOrdersInfo {
	// 相对于 CreateOrdersInfo 多了一个订单号
	// 包含里一个待审核订单的全部信息
	
	private String ordersId; // 订单号
	private String accountName;  // 订单发起人
	
	// 发件人信息
	private String senderName;
	private String senderPhone;
	private String[] senderAddress; // 省 / 市 / (县/区)
	private String senderDetailAddress;
	
	// 收件人信息
	private String receiverName;
	private String receiverPhone;
	private String[] receiverAddress;
	private String receiverDetailAddress;
	
	// 物品种类
	private String goodType;
	
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public String getSenderDetailAddress() {
		return senderDetailAddress;
	}
	public void setSenderDetailAddress(String senderDetailAddress) {
		this.senderDetailAddress = senderDetailAddress;
	}
	public String[] getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String[] senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String[] getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String[] receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverDetailAddress() {
		return receiverDetailAddress;
	}
	public void setReceiverDetailAddress(String receiverDetailAddress) {
		this.receiverDetailAddress = receiverDetailAddress;
	}
	public String getGoodType() {
		return goodType;
	}
	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}
	public String getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}
}

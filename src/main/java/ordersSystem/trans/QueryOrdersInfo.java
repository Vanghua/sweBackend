package ordersSystem.trans;

public class QueryOrdersInfo { // 抽象类, 查询任何已创建订单的共有信息	
	// 订单号等信息
	private String ordersId;
	private String ordersName;
	private String ordersStatus;
	private String createTime; // 订单创建时间, 转化为字符串
	private String accountName; // 该订单的所属用户, 不一定是查询人
	private String userPriority; 
	// 发件人
	private String senderName;
	private String senderPhone;
	private String[] senderAddress;
	private String senderDetailAddress;
	private String senderLng;
	private String senderLat;
	
	// 收件人
	private String receiverName;
	private String receiverPhone;
	private String[] receiverAddress;
	private String receiverDetailAddress;
	private String receiverLng;
	private String receiverLat;
	
	public String getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}
	public String getOrdersName() {
		return ordersName;
	}
	public void setOrdersName(String ordersName) {
		this.ordersName = ordersName;
	}
	public String getOrdersStatus() {
		return ordersStatus;
	}
	public void setOrdersStatus(String ordersStatus) {
		this.ordersStatus = ordersStatus;
	}
	public String getReceiverDetailAddress() {
		return receiverDetailAddress;
	}
	public void setReceiverDetailAddress(String receiverDetailAddress) {
		this.receiverDetailAddress = receiverDetailAddress;
	}
	public String[] getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String[] receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getUserPriority() {
		return userPriority;
	}
	public void setUserPriority(String userPriority) {
		this.userPriority = userPriority;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSenderLng() {
		return senderLng;
	}
	public void setSenderLng(String senderLng) {
		this.senderLng = senderLng;
	}
	public String getSenderLat() {
		return senderLat;
	}
	public void setSenderLat(String senderLat) {
		this.senderLat = senderLat;
	}
	public String getReceiverLng() {
		return receiverLng;
	}
	public void setReceiverLng(String receiverLng) {
		this.receiverLng = receiverLng;
	}
	public String getReceiverLat() {
		return receiverLat;
	}
	public void setReceiverLat(String receiverLat) {
		this.receiverLat = receiverLat;
	}
}

package ordersSystem.trans;

public class CheckFailInfo {
	private String ordersId;
	private String orderManagerName;
	private String cancleReason; // 订单管理员填写, 为何审核不通过
	
	public String getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}
	public String getOrderManagerName() {
		return orderManagerName;
	}
	public void setOrderManagerName(String orderManagerName) {
		this.orderManagerName = orderManagerName;
	}
	public String getCancleReason() {
		return cancleReason;
	}
	public void setCancleReason(String cancleReason) {
		this.cancleReason = cancleReason;
	}
	
}

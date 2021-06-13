package ordersSystem.trans;

public class QueryCancleOrdersInfo extends QueryOrdersInfo{ // 取消的订单信息
	private String cancleTime; // 订单完成时间, 转化为字符串
	private String cancleReason; 
	
	private String checkDate;
	private String checkResult;
	private String orderManagerName;
	
	public String getCancleTime() {
		return cancleTime;
	}
	public void setCancleTime(String cancleTime) {
		this.cancleTime = cancleTime;
	}
	public String getCancleReason() {
		return cancleReason;
	}
	public void setCancleReason(String cancleReason) {
		this.cancleReason = cancleReason;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getOrderManagerName() {
		return orderManagerName;
	}
	public void setOrderManagerName(String orderManagerName) {
		this.orderManagerName = orderManagerName;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	
}

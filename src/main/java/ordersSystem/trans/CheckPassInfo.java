package ordersSystem.trans;

public class CheckPassInfo {
	private String ordersId;
	private String goodPriority;
	private String orderManagerName;
	private double goodWeight;
	private double ordersPrice;
	
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
	public double getGoodWeight() {
		return goodWeight;
	}
	public void setGoodWeight(double goodWeight) {
		this.goodWeight = goodWeight;
	}
	public double getOrdersPrice() {
		return ordersPrice;
	}
	public void setOrdersPrice(double ordersPrice) {
		this.ordersPrice = ordersPrice;
	}
	public String getGoodPriority() {
		return goodPriority;
	}
	public void setGoodPriority(String goodPriority) {
		this.goodPriority = goodPriority;
	}
}

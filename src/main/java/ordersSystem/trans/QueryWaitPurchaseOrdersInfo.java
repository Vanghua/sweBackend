package ordersSystem.trans;

public class QueryWaitPurchaseOrdersInfo extends QueryOrdersInfo{
	private String goodPriority;
	private Double goodWeight;
	private Double ordersPrice;

	public String getGoodPriority() {
		return goodPriority;
	}

	public void setGoodPriority(String goodPriority) {
		this.goodPriority = goodPriority;
	}

	public Double getGoodWeight() {
		return goodWeight;
	}

	public void setGoodWeight(Double goodWeight) {
		this.goodWeight = goodWeight;
	}

	public Double getOrdersPrice() {
		return ordersPrice;
	}

	public void setOrdersPrice(Double ordersPrice) {
		this.ordersPrice = ordersPrice;
	}
}

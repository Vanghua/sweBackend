package ordersSystem.trans;

public class QueryCurrentOrdersInfo extends QueryOrdersInfo{ // 进行中订单
	private String[] route;

	public String[] getRoute() {
		return route;
	}

	public void setRoute(String[] route) {
		this.route = route;
	}
}

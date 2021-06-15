package ordersSystem.trans;

public class QueryCurrentOrdersInfo extends QueryOrdersInfo{ // 进行中订单
	private String[] route;
	
	private Double[] routeLat;
	private Double[] routeLng;
	private String[] routeTime;
	
	public String[] getRoute() {
		return route;
	}

	public void setRoute(String[] route) {
		this.route = route;
	}

	public Double[] getRouteLat() {
		return routeLat;
	}

	public void setRouteLat(Double[] routeLat) {
		this.routeLat = routeLat;
	}

	public Double[] getRouteLng() {
		return routeLng;
	}

	public void setRouteLng(Double[] routeLng) {
		this.routeLng = routeLng;
	}

	public String[] getRouteTime() {
		return routeTime;
	}

	public void setRouteTime(String[] routeTime) {
		this.routeTime = routeTime;
	}
}

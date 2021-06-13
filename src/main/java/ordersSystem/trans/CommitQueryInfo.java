package ordersSystem.trans;

public class CommitQueryInfo {
	// 查询用户的用户名, 系统将根据用户名判断用户类别, 普通用户只能看到自己的订单, 管理员可以查看所有订单
	private String QueryAccountName;
	// 过滤器: 名称 or 订单编号
	private String QueryFilter; 
	// 过滤内容:
	private String QueryFilterContent;
	public String getQueryAccountName() {
		return QueryAccountName;
	}
	public void setQueryAccountName(String queryAccountName) {
		QueryAccountName = queryAccountName;
	}
	public String getQueryFilter() {
		return QueryFilter;
	}
	public void setQueryFilter(String queryFilter) {
		QueryFilter = queryFilter;
	}
	public String getQueryFilterContent() {
		return QueryFilterContent;
	}
	public void setQueryFilterContent(String queryFilterContent) {
		QueryFilterContent = queryFilterContent;
	}
}

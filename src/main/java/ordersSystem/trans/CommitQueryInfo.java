package ordersSystem.trans;

public class CommitQueryInfo {
	// 查询用户的用户名, 系统将根据用户名判断用户类别, 普通用户只能看到自己的订单, 管理员可以查看所有订单
	private String queryAccountName;
	// 过滤器: 名称 or 订单编号
	private String queryFilter;
	// 过滤内容:
	private String queryFilterContent;

	public String getQueryAccountName() {
		return queryAccountName;
	}

	public void setQueryAccountName(String queryAccountName) {
		this.queryAccountName = queryAccountName;
	}

	public String getQueryFilter() {
		return queryFilter;
	}

	public void setQueryFilter(String queryFilter) {
		this.queryFilter = queryFilter;
	}

	public String getQueryFilterContent() {
		return queryFilterContent;
	}

	public void setQueryFilterContent(String queryFilterContent) {
		this.queryFilterContent = queryFilterContent;
	}
}

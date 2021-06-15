package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import accountSystem.trans.AccountNameInfo;
import ordersSystem.trans.CheckFailInfo;
import ordersSystem.trans.CheckPassInfo;
import ordersSystem.trans.CommitQueryInfo;
import ordersSystem.trans.CreateFreqAddressInfo;
import ordersSystem.trans.CreateOrdersInfo;
import ordersSystem.trans.FreqIdAddress;
import ordersSystem.trans.ModifyFreqAddressInfo;
import ordersSystem.trans.OrdersIdInfo;
import ordersSystem.trans.OrdersPositionInfo;
import ordersSystem.trans.QueryCancleOrdersInfo;
import ordersSystem.trans.QueryCurrentOrdersInfo;
import ordersSystem.trans.QueryFreqAddressInfo;
import ordersSystem.trans.QueryOrdersInfo;
import ordersSystem.trans.QuerySuccessOrdersInfo;
import ordersSystem.trans.QueryWaitPurchaseOrdersInfo;
import warehouseSystem.trans.GoodInfo;
@RestController
public class OrdersController {

	@PostMapping("api/orders/createOrders")
	public String createOrders(@RequestBody CreateOrdersInfo createOrdersInfo) { // 客户发起待审核订单, 返回订单号


		String senderFormatAddress = "", receiverFormatAddress = "";
		for(int i = 0; i < 3; ++i){
			if(i!=0) {
				senderFormatAddress += "|";
				receiverFormatAddress += "|";
			}

			senderFormatAddress += createOrdersInfo.getSenderAddress()[i];
			receiverFormatAddress += createOrdersInfo.getReceiverAddress()[i];
		}

		ArrayList<HashMap<String, Object>> res =
				Global.ju.query(
						"select create_order" +
						"(?,?,?,?,?" +
						",?,?,?,?,?," +
						"?,?,?,?,?)",
			createOrdersInfo.getAccountName(), createOrdersInfo.getOrdersName(), createOrdersInfo.getUserPriority(),
			createOrdersInfo.getSenderName(), createOrdersInfo.getSenderPhone(),
			senderFormatAddress, createOrdersInfo.getSenderDetailAddress(),
			createOrdersInfo.getReceiverName(), createOrdersInfo.getReceiverPhone(),
			receiverFormatAddress, createOrdersInfo.getReceiverDetailAddress(),
			createOrdersInfo.getSenderLng(), createOrdersInfo.getSenderLat(),
			createOrdersInfo.getReceiverLng(), createOrdersInfo.getReceiverLat()
			);

		if(res.isEmpty())
		return "失败";
		else return "成功"; // 只要传的对象正确就可以返回成功
	}
	
	@PostMapping("api/orders/checkPass")
	public String checkOrders(@RequestBody CheckPassInfo checkPassInfo ) { // 订单审核通过

		Global.ju.execute("update orders set orders_status = ?, good_priority = ?, good_weight = ?, orders_price = ? where orders_id = ?", 
				"待支付",
				checkPassInfo.getGoodPriority(),
				checkPassInfo.getGoodWeight(),
				checkPassInfo.getOrdersPrice(),
				checkPassInfo.getOrdersId());
		
		Global.ju.execute("insert into check_result (orders_id, result, order_manager_name) values(?,?,?)", 
				checkPassInfo.getOrdersId(), 
				"通过", 
				checkPassInfo.getOrderManagerName());
		
		return "审核完成";
	}
	
	@PostMapping("api/orders/checkFail")
	public String checkFail(@RequestBody CheckFailInfo checkFailInfo) { // 订单审核不通过
		Global.ju.execute("update orders set orders_status = ? where orders_id = ?", 
				"取消", 
				checkFailInfo.getOrdersId());
		
		Global.ju.execute("insert into cancle_orders (orders_id, cancle_reason) values(?,?)", 
				checkFailInfo.getOrdersId(),
				checkFailInfo.getCancleReason());
		
		Global.ju.execute("insert into check_result (orders_id, result, order_manager_name) values(?,?,?)", 
				checkFailInfo.getOrdersId(), 
				"不通过",
				checkFailInfo.getOrderManagerName());
		

		return "审核完成";
	}

	@PostMapping("api/orders/createFreqAddress")
	public String createFreqAddress(@RequestBody CreateFreqAddressInfo createFreqAddressInfo) { // 添加常用地址簿
		String formatAddress = "";
		for(int i = 0; i < 3; ++i) {
			if(i != 0) formatAddress += "|";
			formatAddress += createFreqAddressInfo.getFreqAddress()[i];
		}
		Global.ju.query("select create_freq_address(?,?,?,?,?,?) as result", 
				createFreqAddressInfo.getAccountName(),
				createFreqAddressInfo.getFreqType(),
				createFreqAddressInfo.getFreqName(),
				createFreqAddressInfo.getFreqPhone(),
				formatAddress,
				createFreqAddressInfo.getFreqDetailAddress()
				);
		
		return "成功";
	}
	
	@PostMapping("api/orders/modifyFreqAddress")
	public String modifyFreqAddress(@RequestBody ModifyFreqAddressInfo modifyFreqAddressInfo) { // 修改常用地址簿
		String formatAddress = "";
		for(int i = 0; i < 3; ++i) {
			if(i != 0) formatAddress += "|";
			formatAddress += modifyFreqAddressInfo.getFreqAddress()[i];
		}
		
		
		Global.ju.execute("update freq_address set freq_name = ?, freq_phone = ?, freq_address = ?, freq_detail_address = ?"
				+ " where freq_id = ?", modifyFreqAddressInfo.getFreqName(), modifyFreqAddressInfo.getFreqPhone(),
				formatAddress, modifyFreqAddressInfo.getFreqDetailAddress(), modifyFreqAddressInfo.getFreqId());
		return "成功";
	}
	
	@PostMapping("api/orders/deleteFreqAddress")
	public String deleteFreqAddress(@RequestBody FreqIdAddress freqIdAddress) { // 删除常用地址簿
		Global.ju.execute("delete from freq_address where freq_id = ?", freqIdAddress.getFreqId());
		return "成功";
	}
	
	@PostMapping("api/orders/getFreqAddress")
	public QueryFreqAddressInfo[] getFreqAddress(@RequestBody AccountNameInfo accountNameInfo) {  // 查询常用地址信息
		// 获取某一个用户的所有地址簿信息。 其中的 freq_id 为地址簿编号（12位随机串）。不应该给用户显示，
		// 但在用户选定常用地址进行删除和修改操作时，这个编号将被传给服务器，用于进行表的修改
		ArrayList<HashMap<String, Object>> a = Global.ju.query
				("select freq_id, freq_name, freq_phone, freq_address, freq_detail_address, freq_type "
						+ "from freq_address "
						+ "where account_name = ?",
						accountNameInfo.getAccountName()
				);
		
		int len = a.size();
		QueryFreqAddressInfo[] res = new QueryFreqAddressInfo[len];
		
		for(int i = 0; i < len; ++i) {
			res[i] = new QueryFreqAddressInfo();
			res[i].setFreqId((String) a.get(i).get("freq_id"));
			res[i].setFreqName((String) a.get(i).get("freq_name"));
			res[i].setFreqPhone((String) a.get(i).get("freq_phone"));
			String formatAddress = (String) a.get(i).get("freq_address");
			res[i].setFreqAddress(formatAddress.split("\\|"));
			res[i].setFreqDetailAddress((String) a.get(i).get("freq_detail_address"));
			res[i].setFreqType((String) a.get(i).get("freq_type"));
		}
		
		return res;
	}
	
	@PostMapping("api/orders/queryCancleOrders")
	public QueryCancleOrdersInfo[] QueryCancleOrders(@RequestBody CommitQueryInfo commitQueryInfo) { // 查询已取消的订单的信息
		String sql = "select "
				// 订单相关信息你
				+ " orders.orders_id, orders_name, orders_status, cast(create_time as char) as create_time, "
				+ " account_name, user_priority, "
				+ " sender_name, sender_phone, sender_address, sender_detail_address, "
				+ " receiver_name, receiver_phone, receiver_address, receiver_detail_address, "
				// 取消相关信息
				+ " cast(cancle_time as char) as cancle_time, cancle_reason, "
				// 审核相关信息
				+ " result as check_result, order_manager_name, cast(check_date as char) as check_date"
				
				+ " from cancle_orders "
				+ " left join orders on cancle_orders.orders_id = orders.orders_id "
				+ " left join check_result on cancle_orders.orders_id = check_result.orders_id "; 
		
		// 订单表右连接 取消订单表, 保证查询得到的一定是 [已取消订单] 的信息
		if(commitQueryInfo.getQueryFilter().equals("name")) { // 按照名称模糊查询
			sql += " where orders_name like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}else if(commitQueryInfo.getQueryFilter().equals("id")){ // 按订单号模糊查询
			sql += " where orders.orders_id like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}else {
			sql += " where 1=1 ";
		}
		
		String queryAccountType = 
				(String) Global.ju.query("select account_type from account where account_name = ?", 
						commitQueryInfo.getQueryAccountName()).get(0).get("account_type");
		
		if(queryAccountType.equals("user")) { // 如果是普通用户, 我们限制只能查到自己的订单信息
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName() + "'";
		}
		
		sql += " order by orders.create_time desc";
		
		ArrayList<HashMap<String, Object>> resList = Global.ju.query(sql);
		
		int len = resList.size();
		
		QueryCancleOrdersInfo[] res = new QueryCancleOrdersInfo[len];
		
		for(int i = 0; i < len; ++i) {
			res[i] = new QueryCancleOrdersInfo();
			HashMap<String, Object> cur = resList.get(i);
			
			res[i].setOrdersId((String) cur.get("orders_id"));
			res[i].setOrdersName((String) cur.get("orders_name"));
			res[i].setOrdersStatus((String) cur.get("orders_status"));
			res[i].setCreateTime((String) cur.get("create_time"));
			res[i].setAccountName((String) cur.get("account_name"));
			res[i].setUserPriority((String) cur.get("user_priority"));
			
			res[i].setSenderName((String) cur.get("sender_name"));
			res[i].setSenderPhone((String) cur.get("sender_phone"));
			res[i].setSenderAddress( ((String) cur.get("sender_address")).split("\\|") );
			res[i].setSenderDetailAddress((String) cur.get("sender_detail_address"));
			
			res[i].setReceiverName((String) cur.get("receiver_name"));
			res[i].setReceiverPhone((String) cur.get("receiver_phone"));
			res[i].setReceiverAddress(((String) cur.get("receiver_address")).split("\\|"));
			res[i].setReceiverDetailAddress((String) cur.get("receiver_detail_address"));
			
			res[i].setCancleTime((String) cur.get("cancle_time"));
			res[i].setCancleReason((String) cur.get("cancle_reason"));
		
			res[i].setCheckResult((String) cur.get("check_result"));
			res[i].setOrderManagerName((String) cur.get("order_manager_name"));
			res[i].setCheckDate((String) cur.get("check_date"));
		}
		
		return res;
	}
	
	
	@PostMapping("api/orders/queryWaitCheckOrders")
	public QueryOrdersInfo[] QueryWaitCheckOrders(@RequestBody CommitQueryInfo commitQueryInfo) { // 查询待审核的订单的信息
		String sql = "select "
				// 订单相关信息你
				+ " orders.orders_id, orders_name, orders_status, cast(create_time as char) as create_time, "
				+ " account_name, user_priority, "
				+ " sender_name, sender_phone, sender_address, sender_detail_address, sender_lng, sender_lat, "
				+ " receiver_name, receiver_phone, receiver_address, receiver_detail_address, receiver_lng, receiver_lat "
				+ " from orders "
				+ " where orders_status = '待审核' ";

		
		
		if(commitQueryInfo.getQueryFilter().equals("name")) { // 按照名称模糊查询
			sql += " and orders_name like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}else if(commitQueryInfo.getQueryFilter().equals("id")){ // 按订单号模糊查询
			sql += " and orders.orders_id like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}

		String queryAccountType =
				(String) Global.ju.query("select account_type from account where account_name = ?",
						commitQueryInfo.getQueryAccountName()).get(0).get("account_type");

		if(queryAccountType.equals("user")) { // 如果是普通用户, 我们限制只能查到自己的订单信息
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName() + "'";
		}

		sql += " order by orders.create_time asc";
		ArrayList<HashMap<String, Object>> resList = Global.ju.query(sql);

		int len = resList.size();

		QueryOrdersInfo[] res = new QueryOrdersInfo[len];

		for(int i = 0; i < len; ++i) {
			res[i] = new QueryOrdersInfo();
			HashMap<String, Object> cur = resList.get(i);

			res[i].setOrdersId((String) cur.get("orders_id"));
			res[i].setOrdersName((String) cur.get("orders_name"));
			res[i].setOrdersStatus((String) cur.get("orders_status"));
			res[i].setCreateTime((String) cur.get("create_time"));
			res[i].setAccountName((String) cur.get("account_name"));
			res[i].setUserPriority((String) cur.get("user_priority"));

			res[i].setSenderName((String) cur.get("sender_name"));
			res[i].setSenderPhone((String) cur.get("sender_phone"));
			res[i].setSenderAddress( ((String) cur.get("sender_address")).split("\\|") );
			res[i].setSenderDetailAddress((String) cur.get("sender_detail_address"));
			res[i].setSenderLng((String) cur.get("sender_lng"));
			res[i].setSenderLat((String) cur.get("sender_lat"));
			
			res[i].setReceiverName((String) cur.get("receiver_name"));
			res[i].setReceiverPhone((String) cur.get("receiver_phone"));
			res[i].setReceiverAddress(((String) cur.get("receiver_address")).split("\\|"));
			res[i].setReceiverDetailAddress((String) cur.get("receiver_detail_address"));
			res[i].setReceiverLng((String) cur.get("receiver_lng"));
			res[i].setReceiverLat((String) cur.get("receiver_lat"));
		}

		return res;
	}
	
	@PostMapping("api/orders/queryWaitPurchaseOrders")
	public QueryWaitPurchaseOrdersInfo[] QueryWaitPurchaseOrders(@RequestBody CommitQueryInfo commitQueryInfo) { // 查询待支付的订单的信息
		String sql = "select "
				+ " orders.orders_id, orders_name, orders_status, cast(create_time as char) as create_time, "
				+ " account_name, user_priority, good_priority, good_weight, orders_price, "
				+ " sender_name, sender_phone, sender_address, sender_detail_address, "
				+ " receiver_name, receiver_phone, receiver_address, receiver_detail_address "
				+ " from orders "
				+ " where orders_status = '待支付' ";
		
		if(commitQueryInfo.getQueryFilter().equals("name")) { // 按照名称模糊查询
			sql += " and orders_name like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}else if(commitQueryInfo.getQueryFilter().equals("id")){ // 按订单号模糊查询
			sql += " and orders.orders_id like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}
		
		String queryAccountType = 
				(String) Global.ju.query("select account_type from account where account_name = ?", 
						commitQueryInfo.getQueryAccountName()).get(0).get("account_type");
		
		if(queryAccountType.equals("user")) { // 如果是普通用户, 我们限制只能查到自己的订单信息
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName() + "'";
		}
		
		sql += " order by orders.create_time asc";
		
		ArrayList<HashMap<String, Object>> resList = Global.ju.query(sql);
		
		int len = resList.size();
		
		QueryWaitPurchaseOrdersInfo[] res = new QueryWaitPurchaseOrdersInfo[len];
		
		for(int i = 0; i < len; ++i) {
			res[i] = new QueryWaitPurchaseOrdersInfo();
			HashMap<String, Object> cur = resList.get(i);
			
			res[i].setOrdersId((String) cur.get("orders_id"));
			res[i].setOrdersName((String) cur.get("orders_name"));
			res[i].setOrdersStatus((String) cur.get("orders_status"));
			res[i].setCreateTime((String) cur.get("create_time"));
			res[i].setAccountName((String) cur.get("account_name"));
			res[i].setUserPriority((String) cur.get("user_priority"));
			
			res[i].setSenderName((String) cur.get("sender_name"));
			res[i].setSenderPhone((String) cur.get("sender_phone"));
			res[i].setSenderAddress( ((String) cur.get("sender_address")).split("\\|") );
			res[i].setSenderDetailAddress((String) cur.get("sender_detail_address"));

			res[i].setReceiverName((String) cur.get("receiver_name"));
			res[i].setReceiverPhone((String) cur.get("receiver_phone"));
			res[i].setReceiverAddress(((String) cur.get("receiver_address")).split("\\|"));
			res[i].setReceiverDetailAddress((String) cur.get("receiver_detail_address"));
		
			res[i].setGoodPriority((String) cur.get("good_priority"));
			res[i].setGoodWeight((Double) cur.get("good_weight"));;
			res[i].setOrdersPrice((Double) cur.get("orders_price"));
		}
		
		return res;
	}
	
	@PostMapping("api/orders/queryOrdersPosition")
	public OrdersPositionInfo QueryOrdersPosition(@RequestBody OrdersIdInfo ordersIdInfo) {
		ArrayList<HashMap<String, Object>> resList = 
				Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lat "
				+ " from orders_position "
				+ " where orders_id = ?", ordersIdInfo.getOrdersId());
		
		OrdersPositionInfo res = new OrdersPositionInfo();
		
		res.setWarehouseAddress((String) resList.get(0).get("warehouse_address"));
		res.setWarehouseLng((String) resList.get(0).get("warehouse_lng"));
		res.setWarehouseLat((String) resList.get(0).get("warehouse_lat"));
		
		return res;
	}
	
	
	@PostMapping("api/orders/purchaseOrders")
	public String purchaseOrders(@RequestBody OrdersIdInfo ordersIdInfo) { // 支付按钮
		
		Global.ju.execute("update orders set orders_status = '进行中' where orders_id = ?", ordersIdInfo.getOrdersId());

		ArrayList<HashMap<String, Object>> resList =
				Global.ju.query("select orders_name, user_priority, good_priority, orders_price " +
				" from orders " +
				" where orders_id = ?",
				ordersIdInfo.getOrdersId());

		String ordersName = (String) resList.get(0).get("orders_name");
		int userPriority = Integer.valueOf((String) resList.get(0).get("user_priority"));
		int goodPriority = Global.goodPriorityDict.get((String) resList.get(0).get("good_priority"));

		double ordersPrice = (double) resList.get(0).get("orders_price");

		// cal priority
		int finalPriority = userPriority * 10 + goodPriority;

		Global.ju.execute("insert into good values(default,?,1,?,?)",
				ordersName,
				finalPriority,
				ordersIdInfo.getOrdersId());

		Global.ju.execute("insert into financialbill values(default, ?, ?, default, ?)",
				ordersIdInfo.getOrdersId(),
				ordersPrice,
				"订单支付"
				);

		return "成功";
	}

	
	@PostMapping("api/orders/getSuccessOrders")
	public QuerySuccessOrdersInfo[] getSuccessOrders(@RequestBody CommitQueryInfo commitQueryInfo) { // 获取用户所有的已完成信息,同样包括普通用户和管理员查询
		String sql = "select "
				+ " orders.orders_id, orders_name, orders_status, cast(create_time as char) as create_time, "
				+ " account_name, user_priority, good_priority, good_weight, orders_price, "
				+ " sender_name, sender_phone, sender_address, sender_detail_address, "
				+ " receiver_name, receiver_phone, receiver_address, receiver_detail_address, "
				+ " cast(success_time as char) as success_time "
				+ " from success_orders left join orders on success_orders.orders_id = orders.orders_id ";
		
		if(commitQueryInfo.getQueryFilter().equals("name")) { // 按照名称模糊查询
			sql += " and orders_name like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}else if(commitQueryInfo.getQueryFilter().equals("id")){ // 按订单号模糊查询
			sql += " and orders.orders_id like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}
		
		String queryAccountType = 
				(String) Global.ju.query("select account_type from account where account_name = ?", 
						commitQueryInfo.getQueryAccountName()).get(0).get("account_type");
		
		if(queryAccountType.equals("user")) { // 如果是普通用户, 我们限制只能查到自己的订单信息
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName() + "'";
		}
		
		sql += " order by success_orders.success_time desc";
		
		ArrayList<HashMap<String, Object>> resList = Global.ju.query(sql);
		
		int len = resList.size();
		
		QuerySuccessOrdersInfo [] res = new QuerySuccessOrdersInfo[len];
		
		for(int i = 0; i < len; ++i) {
			res[i] = new QuerySuccessOrdersInfo();
			
			HashMap<String, Object> cur = resList.get(i);
			
			res[i].setOrdersId((String) cur.get("orders_id"));
			res[i].setOrdersName((String) cur.get("orders_name"));
			res[i].setOrdersStatus((String) cur.get("orders_status"));
			res[i].setCreateTime((String) cur.get("create_time"));
			res[i].setAccountName((String) cur.get("account_name"));
			res[i].setUserPriority((String) cur.get("user_priority"));
			
			res[i].setSenderName((String) cur.get("sender_name"));
			res[i].setSenderPhone((String) cur.get("sender_phone"));
			res[i].setSenderAddress( ((String) cur.get("sender_address")).split("\\|") );
			res[i].setSenderDetailAddress((String) cur.get("sender_detail_address"));

			res[i].setReceiverName((String) cur.get("receiver_name"));
			res[i].setReceiverPhone((String) cur.get("receiver_phone"));
			res[i].setReceiverAddress(((String) cur.get("receiver_address")).split("\\|"));
			res[i].setReceiverDetailAddress((String) cur.get("receiver_detail_address"));
			
			res[i].setSuccessTime((String) cur.get("success_time"));
		}
		
		return res;
	}
	
	
	@PostMapping("api/orders/getCurrentOrders")
	public QueryCurrentOrdersInfo[] getCurrentOrders(@RequestBody CommitQueryInfo commitQueryInfo) { // 获取用户所有的进行中信息,同样包括普通用户和管理员查询
		String sql = "select "
				+ " orders.orders_id, orders_name, orders_status, cast(create_time as char) as create_time, "
				+ " account_name, user_priority, good_priority, good_weight, orders_price, "
				+ " sender_name, sender_phone, sender_address, sender_detail_address, "
				+ " receiver_name, receiver_phone, receiver_address, receiver_detail_address, "
				+ " route "
				+ " from orders left join orders_route on orders.orders_id = orders_route.orders_id "
				+ " where orders.orders_status = '进行中'";
		
		if(commitQueryInfo.getQueryFilter().equals("name")) { // 按照名称模糊查询
			sql += " and orders_name like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}else if(commitQueryInfo.getQueryFilter().equals("id")){ // 按订单号模糊查询
			sql += " and orders.orders_id like '" + commitQueryInfo.getQueryFilterContent() + "%'";
		}
		
		String queryAccountType = 
				(String) Global.ju.query("select account_type from account where account_name = ?", 
						commitQueryInfo.getQueryAccountName()).get(0).get("account_type");
		
		if(queryAccountType.equals("user")) { // 如果是普通用户, 我们限制只能查到自己的订单信息
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName() + "'";
		}
		
		sql += " order by orders.create_time asc";
		
		ArrayList<HashMap<String, Object>> resList = Global.ju.query(sql);
		
		int len = resList.size();
		
		QueryCurrentOrdersInfo [] res = new QueryCurrentOrdersInfo[len];
		
		for(int i = 0; i < len; ++i) {
			res[i] = new QueryCurrentOrdersInfo();

			HashMap<String, Object> cur = resList.get(i);

			res[i].setOrdersId((String) cur.get("orders_id"));
			res[i].setOrdersName((String) cur.get("orders_name"));
			res[i].setOrdersStatus((String) cur.get("orders_status"));
			res[i].setCreateTime((String) cur.get("create_time"));
			res[i].setAccountName((String) cur.get("account_name"));
			res[i].setUserPriority((String) cur.get("user_priority"));

			res[i].setSenderName((String) cur.get("sender_name"));
			res[i].setSenderPhone((String) cur.get("sender_phone"));
			res[i].setSenderAddress(((String) cur.get("sender_address")).split("\\|"));
			res[i].setSenderDetailAddress((String) cur.get("sender_detail_address"));

			res[i].setReceiverName((String) cur.get("receiver_name"));
			res[i].setReceiverPhone((String) cur.get("receiver_phone"));
			res[i].setReceiverAddress(((String) cur.get("receiver_address")).split("\\|"));
			res[i].setReceiverDetailAddress((String) cur.get("receiver_detail_address"));

			res[i].setRoute(((String) cur.get("route")).split("\\|"));


			// route lat lng
			Double[] routeLat = new Double[res[i].getRoute().length];
			Double[] routeLng = new Double[res[i].getRoute().length];
			String[] routeTime = new String[res[i].getRoute().length];


			if (!res[i].getRoute()[0].equals("")) {
				for (int j = 0; j < res[i].getRoute().length; ++j) {
					// System.out.println("订单 " + res[i].getOrdersId() + ", " + res[i].getOrdersName() + ", " + res[i].getRoute()[j]);

					ArrayList<HashMap<String, Object>> tmp = Global.ju.query("select "
							+ "cast(warehouse_lat as double) as lat ,"
							+ "cast(warehouse_lng as double) as lng "
							+ " from warehouse "
							+ " where warehouse_address = ?", res[i].getRoute()[j]);

					routeLat[j] = (Double) tmp.get(0).get("lat");
					routeLng[j] = (Double) tmp.get(0).get("lng");
				}
				// reach time
				for (int j = 0; j < res[i].getRoute().length; ++j) {
					ArrayList<HashMap<String, Object>> tmp = Global.ju.query("select cast(`warehouselist`.`list_warehouseTime` as char) as result"
									+ " from good "
									+ " left join storage on storage_goodId = good_id "
									+ " left join warehouselist on list_storageId = storage_id "
									+ " left join warehouse on warehouse_id = storage_warehouseId "
									+ " where orders_id = ? and warehouse_address = ?",
							res[i].getOrdersId(), res[i].getRoute()[j]);

					if (!tmp.isEmpty()) {
						routeTime[j] = (String) tmp.get(0).get("result");
					} else {
						routeTime[j] = "";
					}
				}
			}
			res[i].setRouteLat(routeLat);
			res[i].setRouteLng(routeLng);
			res[i].setRouteTime(routeTime);
		}
		return res;
	}
	
	
	@PostMapping("api/orders/cancleOrders")
	public String CancleOrders(@RequestBody CancleOrdersInfo cancleOrdersInfo) {
		ArrayList<HashMap<String, Object>> resList = 
				Global.ju.query("select orders_status "
				+ " from orders "
				+ " where orders_id = ?", cancleOrdersInfo.getOrdersId());
		
		if(resList.isEmpty()) {
			return "不存在";
		}else {
			String ordersStatus = (String) resList.get(0).get("orders_status");
			
			if(ordersStatus.equals("待支付")) {
				Global.ju.execute("update orders "
						+ "set orders_status = ? "
						+ "where orders_id = ?", 
						"取消", 
						cancleOrdersInfo.getOrdersId());
				
				Global.ju.execute("insert into "
						+ " cancle_orders (orders_id, cancle_reason) "
						+ "values(?,?) ", 
						cancleOrdersInfo.getOrdersId(), 
						cancleOrdersInfo.getCancleReason());
				
				return "成功";
			
			}else if(ordersStatus.equals("进行中")){
				String[] totalAddress = 
						((String) Global.ju.query("select route "
								+ " from orders_route "
								+ "where orders_id = ?", 
								cancleOrdersInfo.getOrdersId()).get(0).get("route")
						).split("\\|");
				
				if(totalAddress[0].equals("")) {
					// 退款
					Double returnMoney = 
							(Double) 
							Global.ju.query("select orders_price "
									+ " from orders "
									+ " where orders_id = ?", 
									cancleOrdersInfo.getOrdersId()).get(0).get("orders_price");
					
					Global.ju.execute("update orders "
							+ "set orders_status = ? "
							+ "where orders_id = ?", 
							"取消", 
							cancleOrdersInfo.getOrdersId());
					
					Global.ju.execute("insert into "
							+ " cancle_orders (orders_id, cancle_reason) "
							+ "values(?,?) ", 
							cancleOrdersInfo.getOrdersId(), 
							cancleOrdersInfo.getCancleReason());
					
					Global.ju.execute("delete from orders_route where orders_id = ?", cancleOrdersInfo.getOrdersId());
					
					return String.valueOf(0.9 * returnMoney);
				}else {
					
					String currentAddress = (String) Global.ju.query("select warehouse_address "
							+ " from orders_position "
							+ " where orders_id = ?", 
							cancleOrdersInfo.getOrdersId()).get(0).get("warehouse_address");
					
					if(currentAddress.equals(totalAddress[0])) {
						// 出库
						String managerId = (String)Global.ju.query("select warehouse_managerId as result "
								+ "from warehouse "
								+ "where warehouse_address = ?",
								currentAddress).get(0).get("result");
						
						GoodInfo goodInfo = new GoodInfo();
						goodInfo.setOrderId(cancleOrdersInfo.getOrdersId());
						goodInfo.setManagerId(managerId);
						
						WarehouseController.tmpFunction(goodInfo);
						
						// 退款
						Double returnMoney = 
								(Double) 
								Global.ju.query("select orders_price "
										+ " from orders "
										+ " where orders_id = ?", 
										cancleOrdersInfo.getOrdersId()).get(0).get("orders_price");
						
						Global.ju.execute("update orders "
								+ "set orders_status = ? "
								+ "where orders_id = ?", 
								"取消", 
								cancleOrdersInfo.getOrdersId());
						
						Global.ju.execute("insert into "
								+ " cancle_orders (orders_id, cancle_reason) "
								+ "values(?,?) ", 
								cancleOrdersInfo.getOrdersId(), 
								cancleOrdersInfo.getCancleReason());
						
						
						Global.ju.execute("delete from orders_route where orders_id = ?", cancleOrdersInfo.getOrdersId());
						
						
						return String.valueOf(0.8 * returnMoney);
					}else {
						return "禁止";
					}
				}
			}else {
				return "禁止";
			}
		}
		

	}
}
	
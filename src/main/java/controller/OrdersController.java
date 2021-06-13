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
import ordersSystem.trans.OrdersPositionInfo;
import ordersSystem.trans.QueryCancleOrdersInfo;
import ordersSystem.trans.QueryFreqAddressInfo;
import ordersSystem.trans.QueryOrdersInfo;
import ordersSystem.trans.QueryWaitPurchaseOrdersInfo;

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
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName();
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
				+ " receiver_name, receiver_phone, receiver_address, receiver_detail_address, receiver_lng, receiver_lat, "
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
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName();
		}
		
		sql += " order by orders.create_time ansc";
		
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
			sql += " and orders.account_name = '" + commitQueryInfo.getQueryAccountName();
		}
		
		sql += " order by orders.create_time ansc";
		
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
			res[i].setOrdersPrice((Integer) cur.get("orders_price"));
		}
		
		return res;
	}
	
//	@PostMapping("api/orders/queryOrdersPosition")
//	public OrdersPositionInfo QueryOrdersPosition(@RequestBody ) {
//		
//	}
}
	
package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ordersSystem.trans.CheckFailInfo;
import ordersSystem.trans.CheckPassInfo;
import ordersSystem.trans.CreateOrdersInfo;
import ordersSystem.trans.SearchWaitCheckOrdersInfo;

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
		
		Global.ju.execute("insert into cancle_orders (orders_id) values(?)", 
				checkFailInfo.getOrdersId());
		
		Global.ju.execute("insert into check_result (orders_id, result, order_manager_name) values(?,?,?)", 
				checkFailInfo.getOrdersId(), 
				"不通过",
				checkFailInfo.getOrderManagerName());
		

		return "审核完成";
	}
	
	@PostMapping("api/orders/searchWaitCheckOrders")
	public SearchWaitCheckOrdersInfo[] searchWaitCheckOrders(@RequestBody SearchWaitCheckOrdersInfo searchOrdersInfo) { // 检索待审核订单, 模糊匹配
		ArrayList<HashMap<String, Object>> res = 
				Global.ju.query("select orders_id from orders where orders_status = '待审核' and "
						+ "orders_id like '"+ searchOrdersInfo.getOrdersId() + "%' order by create_time desc");
		
		SearchWaitCheckOrdersInfo[] final_ans = new SearchWaitCheckOrdersInfo[res.size()];
		
		int x = res.size();
		
		for(int i = 0; i < x; ++i) {
			final_ans[i].setOrdersId((String) res.get(i).get("orders_id"));
		}
		
		return final_ans;
	}
	
//	@PostMapping("api/orders/searchCurrentOrders")
//	public 
}
	
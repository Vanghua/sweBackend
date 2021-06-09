package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ordersSystem.trans.CheckFailInfo;
import ordersSystem.trans.CheckPassInfo;
import ordersSystem.trans.CreateOrdersInfo;

@RestController
public class OrdersController {

	@PostMapping("api/orders/createOrders")
	public String createOrders(@RequestBody CreateOrdersInfo createOrdersInfo) { // 客户发起待审核订单, 返回订单号
		ArrayList<HashMap<String, Object>> res = Global.ju.query("select create_order(?,?,?,?,?,?,?,?,?,?)",
			createOrdersInfo.getAccountName(), createOrdersInfo.getGoodType(),
			createOrdersInfo.getSenderName(), createOrdersInfo.getSenderPhone(), 
			createOrdersInfo.getSenderAddress(), createOrdersInfo.getSenderDetailAddress(),
			createOrdersInfo.getReceiverName(), createOrdersInfo.getReceiverPhone(), 
			createOrdersInfo.getSenderAddress(), createOrdersInfo.getReceiverDetailAddress()
			);
		
		if(res.isEmpty())
		return "失败";
		else return "成功"; // 只要传的对象正确就可以返回成功
	}
	
	@PostMapping("api/orders/checkPass")
	public String checkOrders(@RequestBody CheckPassInfo checkPassInfo ) { // 订单审核通过
		Global.ju.execute("update orders set orders_status = ?, good_weight = ?, orders_price = ? where orders_id = ?", 
				"待支付",
				checkPassInfo.getGoodWeight(),
				checkPassInfo.getOrdersPrice(),
				checkPassInfo.getOrdersId());
		
		Global.ju.execute("insert into check_result values(?,?,?)", 
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
		
		Global.ju.execute("insert into check_result values(?,?,?)", 
				checkFailInfo.getOrdersId(), 
				"不通过",
				checkFailInfo.getDescription());
		
		Global.ju.execute("insert into end_orders values(?,?)", 
				checkFailInfo.getOrdersId(), 
				checkFailInfo.getDescription());
		
		return "审核完成";
	}
}

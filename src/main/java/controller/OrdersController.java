package controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import accountSystem.trans.AccountNameInfo;
import ordersSystem.trans.CheckFailInfo;
import ordersSystem.trans.CheckPassInfo;
import ordersSystem.trans.CreateFreqAddressInfo;
import ordersSystem.trans.CreateOrdersInfo;
import ordersSystem.trans.FreqIdAddress;
import ordersSystem.trans.ModifyFreqAddressInfo;
import ordersSystem.trans.QueryFreqAddressInfo;

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
	public QueryFreqAddressInfo[] getFreqAddress(@RequestBody AccountNameInfo accountNameInfo) { 
		// 获取某一个用户的所有地址簿信息。 其中的 freq_id 为地址簿编号（12位随机串）。不应该给用户显示，
		// 但在用户选定常用地址进行删除和修改操作时，这个编号将被传给服务器，用于进行表的修改
		ArrayList<HashMap<String, Object>> a = Global.ju.query
				("select freq_id, freq_name, freq_phone, freq_address, freq_detail_address "
						+ "from freq_address "
						+ "where freq_name = ?", 
						accountNameInfo.getAccountName()
				);
		
		int len = a.size();
		QueryFreqAddressInfo[] res = new QueryFreqAddressInfo[len];
		
		for(int i = 0; i < len; ++i) {
			res[i].setFreqId((String) a.get(i).get("freq_id"));
			res[i].setFreqName((String) a.get(i).get("freq_name"));
			res[i].setFreqPhone((String) a.get(i).get("freq_phone"));
			String formatAddress = (String) a.get(i).get("freq_address");
			res[i].setFreqAddress(formatAddress.split("|"));
			res[i].setFreqDetailAddress((String) a.get(i).get("freq_detail_address"));
		}
		
		return res;
	}
	
}
	
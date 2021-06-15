package controller;

import assignSystem.trans.Trans;
import ordersSystem.trans.OrdersIdInfo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;


@RestController
public class AssignController {

    // 运输员修改所属仓库信息
    @PostMapping("/api/assign/transEdit")
    public String transEdit(@RequestBody Trans trans) {
        // 判断仓库是否存在
        String sql = "select * from warehouse where warehouse_id = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query(sql, trans.getWarehouseId());
        if(resultList.size() == 0)
            return "仓库不存在";

        // 判断运输员是否已经存在，如果已经存在那么更换仓库
        sql = "select * from trans where account_name = ?";
        resultList = Global.ju.query(sql, trans.getAccountName());
        if(resultList.size() != 0) {
            sql = "update trans set warehouse_id = ? where account_name = ?";
            Global.ju.execute(sql, trans.getWarehouseId(), trans.getAccountName());
            return "成功";
        }

        // 运输员不存在则插入运输员表
        sql = "insert into trans values(?,?)";
        Global.ju.execute(sql, trans.getAccountName(), trans.getWarehouseId());
        return "成功";
    }

    // 运输员信息获取
    @PostMapping("/api/assign/getTransInfo")
    public int getTransInfo(@RequestBody Trans trans) {
        // 查询运输员是否存在
        String sql = "select * from trans where account_name = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query("select * from trans where account_name = ?", trans.getAccountName());
        if(resultList.size() == 0)
            return 0;
        else
            return (int)resultList.get(0).get("warehouse_id");
    }
    
    // 物品运输路线规划：当物品第一次入库时调用此接口：传入订单号
    @PostMapping("api/assign/getAssign")
    public String getAssign(@RequestBody OrdersIdInfo ordersIdInfo) {
    	ArrayList<HashMap<String, Object>> resList = 
    			Global.ju.query("select sender_address, receiver_address "
    					+ " from orders "
    					+ " where orders_id = ?", ordersIdInfo.getOrdersId());

    	String[] formatSenderAddress = ((String) resList.get(0).get("sender_address")).split("\\|");
    	String[] formatReceiverAddress = ((String) resList.get(0).get("receiver_address")).split("\\|");
    	
    	Double fromLng = 0.0, fromLat= 0.0, toLng = 0.0, toLat = 0.0;
    	
    	// 物品初始所在接收站的经纬度
    	ArrayList<HashMap<String, Object>> currentPositionList = 
    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lat "
    					+ "from orders_position "
    					+ "where orders_id = ?", ordersIdInfo.getOrdersId());
    	// result
    	String fromWarehouseAddress = (String) currentPositionList.get(0).get("warehouse_address");
    	fromLng = (Double) currentPositionList.get(0).get("warehouse_lng");
    	fromLat = (Double) currentPositionList.get(0).get("warehouse_lat");
    	
    	// 找到收件人的接收站仓库, 演示时注意建立对应区的接收站
    	ArrayList<HashMap<String, Object>> totalPossibleWareHouse = 
    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
    					+ " from warehouse "
    					+ " where warehouse_district = ?", formatReceiverAddress[2]);
    	
    	int targetIndex = 0;
    	double minDistance = 1000000000.0;
    	
    	for(int i = 0; i < totalPossibleWareHouse.size(); ++i) {
    		toLng = (Double) totalPossibleWareHouse.get(i).get("warehouse_lng");
    		toLat = (Double) totalPossibleWareHouse.get(i).get("warehouse_lat");
    		
    		double curDis = Global.getDistance(fromLat, fromLng, toLat, toLng);
    		
    		if(curDis < minDistance) {
    			targetIndex = i;
    			minDistance = curDis;
    		}
    	}
    	
    	// result
    	String toWarehouseAddress = (String) totalPossibleWareHouse.get(targetIndex).get("warehouse_address");
    	toLng = (Double) totalPossibleWareHouse.get(targetIndex).get("warehouse_lng");
		toLat = (Double) totalPossibleWareHouse.get(targetIndex).get("warehouse_lat");
    	
		
    	if(formatSenderAddress[0].equals(formatReceiverAddress[0])) { 
    		if(formatSenderAddress[1].equals(formatReceiverAddress[1])) { // 市内运输, 1-2-1类别
    			ArrayList<HashMap<String, Object>> totalPossibleWareHouse_LEVEL2 = 
    	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
    	    					+ " from warehouse "
    	    					+ " where warehouse_city = ? and warehouse_type = 2", formatReceiverAddress[1]);
    			
    			
    			int targetLEVEL2Index = 0;
    			
    			for(int i = 0; i < totalPossibleWareHouse_LEVEL2.size(); ++i) {
    				minDistance = 1000000000.0;
    				Double curLng = (Double) totalPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
    				Double curLat = (Double) totalPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");
    				
    				double curDistance = Global.getDistance(fromLat, fromLng, curLat, curLng) + 
    									 Global.getDistance(toLat, toLng, curLat, curLng);
    				
    				if(curDistance < minDistance) {
    					targetLEVEL2Index = i; 
    					minDistance = curDistance;
    				}
    			}
    			
    			String LEVEL2WarehouseAddress = (String) totalPossibleWareHouse_LEVEL2.
    					get(targetLEVEL2Index).get("warehouse_address");
    			// 路线安排完毕
    			String resultRoute = fromWarehouseAddress + "|" + LEVEL2WarehouseAddress + "|" + toWarehouseAddress;
    			
    			Global.ju.execute("insert into orders_route(?,?) ", ordersIdInfo.getOrdersId(), resultRoute);
    			
    		}else { // 省内跨市运输 1-2-3-2-1\
    			Double From2Lng = 0.0, From2Lat = 0.0, To2Lng = 0.0, To2Lat = 0.0;
    			// 1-2
    			ArrayList<HashMap<String, Object>> totalFromPossibleWareHouse_LEVEL2 = 
    	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
    	    					+ " from warehouse "
    	    					+ " where warehouse_city = ? and warehouse_type = 2", formatSenderAddress[1]);
    			
    			
    			int targetFromLEVEL2Index = 0;
    			
    			for(int i = 0; i < totalFromPossibleWareHouse_LEVEL2.size(); ++i) {
    				minDistance = 1000000000.0;
    				Double curLng = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
    				Double curLat = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");
    				
    				// 计算二级仓库与发件接收站距离
    				double curDistance = Global.getDistance(fromLat, fromLng, curLat, curLng);
    				
    				if(curDistance < minDistance) {
    					targetFromLEVEL2Index = i; 
    					minDistance = curDistance;
    					
    					From2Lng = curLng;
    					From2Lat = curLat;
    				}
    			}
    			
    			String FromLevel2WarehouseAddress = (String) totalFromPossibleWareHouse_LEVEL2.get(targetFromLEVEL2Index).
    					get("warehouse_address");
    			
    			// 2-1 
    			
    			ArrayList<HashMap<String, Object>> totalToPossibleWareHouse_LEVEL2 = 
    	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
    	    					+ " from warehouse "
    	    					+ " where warehouse_city = ? and warehouse_type = 2", formatSenderAddress[1]);
    			
    			
    			int targetToLEVEL2Index = 0;
    			
    			for(int i = 0; i < totalToPossibleWareHouse_LEVEL2.size(); ++i) {
    				minDistance = 1000000000.0;
    				Double curLng = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
    				Double curLat = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");
    				
    				// 计算收件仓库与二级接收站距离
    				double curDistance = Global.getDistance(toLat, toLng, curLat, curLng);
    				
    				if(curDistance < minDistance) {
    					targetToLEVEL2Index = i; 
    					minDistance = curDistance;
    					
    					To2Lng = curLng;
    					To2Lat = curLat;
    				}
    			}
    			
    			String ToLevel2WarehouseAddress = (String) totalToPossibleWareHouse_LEVEL2.get(targetToLEVEL2Index).
    					get("warehouse_address");
    			
    			
    			// 2-3-2
    			
    			ArrayList<HashMap<String, Object>> totalFromPossibleWareHouse_LEVEL3 = 
    	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
    	    					+ " from warehouse where warehouse_type = 3");
    			
    			
    			int targetLEVEL3Index = 0;
    			
    			for(int i = 0; i < totalFromPossibleWareHouse_LEVEL3.size(); ++i) {
    				minDistance = 1000000000.0;
    				Double curLng = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lng");
    				Double curLat = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lat");
    				
    				double curDistance = Global.getDistance(From2Lat, From2Lng, curLat, curLng) + 
    									Global.getDistance(To2Lat, To2Lng, curLat, curLng);
    				
    				if(curDistance < minDistance) {
    					targetLEVEL3Index = i; 
    					minDistance = curDistance;
    				}
    			}
    			
    			String Level3WarehouseAddress = (String) totalFromPossibleWareHouse_LEVEL3.get(targetLEVEL3Index).
    					get("warehouse_address");
    			
    			// 路线安排完毕
    			String resultRoute = 
    					fromWarehouseAddress + "|" + FromLevel2WarehouseAddress + "|" + 
    					Level3WarehouseAddress + "|" + 
    					ToLevel2WarehouseAddress + "|"+ toWarehouseAddress;
    			
    			Global.ju.execute("insert into orders_route(?,?) ", ordersIdInfo.getOrdersId(), resultRoute);
    		}
    	}else { // 跨省运输 1-2-3-3-2-1
			Double From2Lng = 0.0, From2Lat = 0.0, To2Lng = 0.0, To2Lat = 0.0;
    		@SuppressWarnings("unused")
			Double From3Lng = 0.0, From3Lat = 0.0, To3Lng = 0.0, To3Lat = 0.0;
    		
			// 1-2
			ArrayList<HashMap<String, Object>> totalFromPossibleWareHouse_LEVEL2 = 
	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
	    					+ " from warehouse "
	    					+ " where warehouse_city = ? and warehouse_type = 2", formatSenderAddress[1]);
			
			
			int targetFromLEVEL2Index = 0;
			
			for(int i = 0; i < totalFromPossibleWareHouse_LEVEL2.size(); ++i) {
				minDistance = 1000000000.0;
				Double curLng = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
				Double curLat = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");
				
				// 计算二级仓库与发件接收站举例
				double curDistance = Global.getDistance(fromLat, fromLng, curLat, curLng);
				
				if(curDistance < minDistance) {
					targetFromLEVEL2Index = i; 
					minDistance = curDistance;
					
					From2Lng = curLng;
					From2Lat = curLat;
				}
			}
			
			String FromLevel2WarehouseAddress = (String) totalFromPossibleWareHouse_LEVEL2.get(targetFromLEVEL2Index).
					get("warehouse_address");
			
			// 2-1 
			
			ArrayList<HashMap<String, Object>> totalToPossibleWareHouse_LEVEL2 = 
	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
	    					+ " from warehouse "
	    					+ " where warehouse_city = ? and warehouse_type = 2", formatSenderAddress[1]);
			
			
			int targetToLEVEL2Index = 0;
			
			for(int i = 0; i < totalToPossibleWareHouse_LEVEL2.size(); ++i) {
				minDistance = 1000000000.0;
				Double curLng = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
				Double curLat = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");
				
				// 计算收件仓库与二级接收站距离
				double curDistance = Global.getDistance(toLat, toLng, curLat, curLng);
				
				if(curDistance < minDistance) {
					targetToLEVEL2Index = i; 
					minDistance = curDistance;
					
					To2Lng = curLng;
					To2Lat = curLat;
				}
			}
			
			String ToLevel2WarehouseAddress = (String) totalToPossibleWareHouse_LEVEL2.get(targetToLEVEL2Index).
					get("warehouse_address");
			
			// 2-3 转省内3级仓库
			
			ArrayList<HashMap<String, Object>> totalFromPossibleWareHouse_LEVEL3 = 
	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
	    					+ " from warehouse "
	    					+ " where warehouse_province = ? and warehouse_type = 3", formatSenderAddress[0]);
			
			
			int targetFromLEVEL3Index = 0;
			
			for(int i = 0; i < totalFromPossibleWareHouse_LEVEL3.size(); ++i) {
				minDistance = 1000000000.0;
				Double curLng = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lng");
				Double curLat = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lat");
				
				// 计算二级仓库与三级仓库距离
				double curDistance = Global.getDistance(From2Lat, From2Lng, curLat, curLng);
				
				if(curDistance < minDistance) {
					targetFromLEVEL3Index = i; 
					minDistance = curDistance;
					
					From3Lng = curLng;
					From3Lat = curLat;
				}
			}
			
			String FromLevel3WarehouseAddress = (String) totalFromPossibleWareHouse_LEVEL3.get(targetFromLEVEL3Index).
					get("warehouse_address");
			
			// 3-2 转省内3级仓库
			ArrayList<HashMap<String, Object>> totalToPossibleWareHouse_LEVEL3 = 
	    			Global.ju.query("select warehouse_address, warehouse_lng, warehouse_lnt "
	    					+ " from warehouse "
	    					+ " where warehouse_province = ? and warehouse_type = 3", formatReceiverAddress[0]);
			
			
			int targetToLEVEL3Index = 0;
			
			for(int i = 0; i < totalToPossibleWareHouse_LEVEL3.size(); ++i) {
				minDistance = 1000000000.0;
				Double curLng = (Double) totalToPossibleWareHouse_LEVEL3.get(i).get("warehouse_lng");
				Double curLat = (Double) totalToPossibleWareHouse_LEVEL3.get(i).get("warehouse_lat");
				
				// 计算二级仓库与三级仓库距离
				double curDistance = Global.getDistance(To2Lat, To2Lng, curLat, curLng);
				
				if(curDistance < minDistance) {
					targetFromLEVEL3Index = i; 
					minDistance = curDistance;
					
					To3Lng = curLng;
					To3Lat = curLat;
				}
			}
			
			String ToLevel3WarehouseAddress = (String) totalToPossibleWareHouse_LEVEL3.get(targetToLEVEL3Index).
					get("warehouse_address");
			
			
			// 3级别仓库内部disjktra
			
			String Level3Route = Global.AssignLevel3Route(FromLevel3WarehouseAddress, ToLevel3WarehouseAddress);
			
			String resultRoute = fromWarehouseAddress + "|" + FromLevel2WarehouseAddress + "|" + 
							Level3Route + "|" + 
							ToLevel2WarehouseAddress + "|" + toWarehouseAddress;
    	
			Global.ju.execute("insert into orders_route(?,?) ", ordersIdInfo.getOrdersId(), resultRoute);
    	}
    	
    	return "成功";
    }
}

package controller;

import com.mysql.cj.util.StringUtils;
import ordersSystem.trans.OrdersIdInfo;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import warehouseSystem.trans.GoodInfo;
import warehouseSystem.trans.QueryInfo;
import warehouseSystem.trans.ShelfInfo;
import warehouseSystem.trans.WarehouseInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WarehouseController {
    // 货物添加
    @PostMapping("/api/warehouse/addGood")
    public String addGood(@RequestBody GoodInfo goodInfo) {
        if (Global.ju.exists("select * from good where orders_id = ?", goodInfo.getOrderId())) {
            return "货物已存在";
        } else {
            Global.ju.execute("insert into good values(default,?,?,?,?)",
                    goodInfo.getGoodName(),
                    goodInfo.getGoodNum(),
                    goodInfo.getPriority(),
                    goodInfo.getGoodId());
            return "货物已成功录入";
        }
    }

    // 删除货物
    @PostMapping("/api/warehouse/goodDelete")
    public String goodDelete(@RequestBody String goodId) {
        if (!Global.ju.exists("select * from good where good_id", goodId)) {
            return "货物不存在";
        } else {
            if (Global.ju.exists("select * from storage where storage_goodId = ?", goodId)) {
                return "货物仍在存储";
            } else {
                Global.ju.execute("delete from good where good_id = ?", goodId);
                return "货物已删除";
            }
        }
    }

    // 货物存位查询
    @PostMapping("/api/warehouse/goodQuery")
    public ArrayList<HashMap<String, Object>> goodQuery(@RequestBody GoodInfo goodInfo) {
        String sql = "select * from storage where storage_goodId = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query(sql, goodInfo.getGoodId());
        return resultList;
    }

    // 仓库添加
    @PostMapping("/api/warehouse/addWarehouse")
    public String addwarehouse(@RequestBody WarehouseInfo warehouseInfo) {
        ArrayList<HashMap<String, Object>> resultlist = Global.ju.query("select account_type as result from account where account_name = ?", warehouseInfo.getWarehouseManager());
        if (Global.ju.exists("select * from warehouse where warehouse_lng = ? and warehouse_lat = ?", warehouseInfo.getWarehouseLng(), warehouseInfo.getWarehouseLat())) {
            return "仓库已存在";
        } else if (resultlist.isEmpty()) {
            return "管理员不存在";
        } else {
            Global.ju.execute("insert into warehouse values(default,?,?,?,?,?,default,?,?,?,?,?)",
                    warehouseInfo.getWarehouseType(),
                    warehouseInfo.getWarehhouseStoragenum(),
                    warehouseInfo.getWarehouseAddress(),
                    warehouseInfo.getWarehouseManager(),
                    warehouseInfo.getWarehouseManagerTel(),
                    warehouseInfo.getWarehouseLng(),
                    warehouseInfo.getWarehouseLat(),
                    warehouseInfo.getWarehouseProvince(),
                    warehouseInfo.getWarehouseCity(),
                    warehouseInfo.getWarehouseDistrict());
            return "仓库已成功添加";
        }
    }

    // 仓库删除
    @PostMapping("/api/warehouse/warehouseDelete")
    public String warehouseDelete(@RequestBody String warehouseId) {
        int storageNum = -1;
        if (!Global.ju.exists("select * from warehouse where warehouse_id = ?", warehouseId)) {
            return "仓库不存在";
        } else {
            if (Global.ju.exists("select * from storage where storage_warehouseId = ?", warehouseId)) {
                return "此仓库内仍有货物";
            } else {
                Global.ju.execute("delete from shelf where shelf_warehouseId = ?", warehouseId);
                Global.ju.execute("delete from warehouse where warehouse_id = ?", warehouseId);
                return "仓库已删除";
            }
        }
    }

    // 返回仓库总数量
    @PostMapping("/api/warehouse/warehouseNumQuery")
    public int warehouseNum() {
        String sql = "select * from warehouse";
        ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql);
        return resultList.size();
    }

    // 全部仓库查询
    @PostMapping("/api/warehouse/warehouseQueryAll")
    public ArrayList<HashMap<String, Object>> warehouseQueryAll(@RequestBody QueryInfo queryInfo) {
        String sql = "select * from warehouse";
        ArrayList<HashMap<String, Object>> resultList;
        ArrayList<ArrayList<HashMap<String, Object>>> listAll = new ArrayList<>();
        resultList = Global.ju.query(sql);
        int size = resultList.size();
        int count = queryInfo.getPageCount();
        int absInt = -1;
        if (size > count) {
            absInt = Math.abs(size / count);
            if (size - absInt * count > 0) {
                listAll.add(Lists.newArrayList(resultList.subList(absInt * count, size)));
            }
            for (int i = 1; i < absInt + 1; ++i) {
                listAll.add(Lists.newArrayList(resultList.subList((i - 1) * count, i * count)));
            }
        } else {
            listAll.add(resultList);
        }
        Collections.reverse(listAll);
        return listAll.get(queryInfo.getPageNum() - 1);
    }

    // 特定仓库查询
    @PostMapping("/api/warehouse/warehouseQuery")
    public ArrayList<HashMap<String, Object>> warehouseQuery(@RequestBody WarehouseInfo warehouseInfo) {
        String sql = "select * from warehouse where warehouse_id = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query(sql, warehouseInfo.getWarehouseId());
        return resultList;
    }

    // 货架添加
    @PostMapping("/api/warehouse/addShelf")
    public String addShelf(@RequestBody ShelfInfo shelfInfo) {
        if (Global.ju.exists("select * from shelf where shelf_id = ? and shelf_warehouseId = ?", shelfInfo.getShelfId(), shelfInfo.getShelfWarehouseId())) {
            return "货架已存在";
        } else if (!Global.ju.exists("select * from warehouse where warehouse_id = ?", shelfInfo.getShelfWarehouseId())) {
            return "仓库不存在";
        } else {
            Global.ju.execute("insert into shelf values(?,?,?)",
                    shelfInfo.getShelfId(),
                    shelfInfo.getShelfWarehouseId(),
                    shelfInfo.getShelfStorageNum());

            Global.ju.execute("update warehouse set warehouse_storagenum = warehouse_storagenum + ? where warehouse_id = ?",
                    shelfInfo.getShelfStorageNum(), shelfInfo.getShelfWarehouseId());
            return "货架已成功添加";
        }
    }

    // 货架删除
    @PostMapping("/api/warehouse/shelfDelete")
    public String shelfDelete(@RequestBody ShelfInfo shelfInfo) {
        int storageNum = -1;
        if (!Global.ju.exists("select * from shelf where shelf_id = ? and shelf_warehouseId = ?", shelfInfo.getShelfId(), shelfInfo.getShelfWarehouseId())) {
            return "此仓库内货架不存在";
        } else {
            if (Global.ju.exists("select * from storage where storage_shelfId = ? and storage_warehouseId = ?", shelfInfo.getShelfId(), shelfInfo.getShelfWarehouseId())) {
                return "此货架内仍有货物";
            } else {
                storageNum = (int) Global.ju.query("select * from shelf where shelf_id = ?", shelfInfo.getShelfId()).get(0).get("shelf_storageNum");
                Global.ju.execute("delete from shelf where shelf_id = ?", shelfInfo.getShelfId());
                Global.ju.execute("update warehouse set warehouse_storagenum = warehouse_storagenum - ? where warehouse_id = ?", storageNum, shelfInfo.getShelfWarehouseId());
                return "删除成功";
            }
        }
    }

    //返回货架总数目
    @PostMapping("/api/warehouse/shelfNumQuery")
    public int shelfNum(@RequestBody QueryInfo queryInfo) {
        String sql = "select * from shelf where shelf_warehouseId = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query(sql, queryInfo.getWarehouseInfo().getWarehouseId());
        return resultList.size();
    }

    //全部货架查询
    @PostMapping("/api/warehouse/shelfQueryAll")
    public ArrayList<HashMap<String, Object>> shelfQueryAll(@RequestBody QueryInfo queryInfo) {
        String sql = "select * from shelf where shelf_warehouseId = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query(sql, queryInfo.getWarehouseInfo().getWarehouseId());
        return resultList;
    }

    // 特定货架查询
    @PostMapping("/api/warehouse/shelfQuery")
    public ArrayList<HashMap<String, Object>> shelfQuery(@RequestBody ShelfInfo shelfInfo) {
        String sql = "select * from shelf where shelf_id = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query(sql, shelfInfo.getShelfId());
        return resultList;
    }

    // 入库办理
    @PostMapping("/api/warehouse/warehouing")
    public String warehousing(@RequestBody GoodInfo goodInfo) {
        int warehouse_result = goodInfo.getWarehouseInfo().getWarehouseId();
        String warehouse_address = Global.ju.query("select * from warehouse where warehouse_id = ?",warehouse_result).get(0).get("warehouse_address").toString();
        int shelf_res = -1;
        int num = -1;
        int storage_id = -1;
        String sql = "";
        String id = "";
        ArrayList<HashMap<String, Object>> resultList;
        sql = "select good_id from good where orders_id = ?";
        ArrayList<HashMap<String, Object>> res = Global.ju.query(sql, goodInfo.getOrderId());
        if (res.size() > 0) {
            String good_id = res.get(0).get("good_id").toString();
            if (Global.ju.exists("select * from storage where storage_goodId = ?", good_id)) {
                return "货物已入库";
            } else if (!Global.ju.exists("select * from warehouse where warehouse_storagenum >= ? and warehouse_id = ?", goodInfo.getGoodNum(), warehouse_result)) {
                return "当前存位不足,无法办理入库";
            } else {
                sql = "select shelf_id from shelf where shelf_storageNum >= ? and shelf_warehouseId = ?";
                resultList = Global.ju.query(sql, goodInfo.getGoodNum(), warehouse_result); // 查出当前可以直接存入货物的货架
                String shelf_id;
                num = goodInfo.getGoodNum();
                if (resultList.size() > 0) { // 找到可以直接存入货物的货架
                    shelf_id = (String) resultList.get(0).get("shelf_id");
                    Global.ju.execute("update shelf set shelf_storageNum = shelf_storageNum - ? where shelf_id = ? and shelf_warehouseId = ?", goodInfo.getGoodNum(), shelf_id, warehouse_result);
                    Global.ju.execute("insert into storage values(default,?,?,?,?)", warehouse_result, good_id, shelf_id, goodInfo.getGoodNum());
                } else {
                    sql = "select shelf_id,shelf_storageNum from shelf where shelf_warehouseId = ?";
                    resultList = Global.ju.query(sql, warehouse_result);
                    HashMap<String, Integer> idList = new HashMap<>();
                    for (HashMap<String, Object> o : resultList) {
                        shelf_id = (String) o.get("shelf_id");
                        shelf_res = (int) o.get("shelf_storageNum");
                        if (shelf_res >= num) {
                            idList.put(shelf_id, num);
                            break;
                        } else {
                            num -= shelf_res;
                            idList.put(shelf_id, shelf_res);
                        }
                    }
                    for (Map.Entry<String, Integer> entry : idList.entrySet()) {
                        id = entry.getKey();
                        num = entry.getValue();
                        Global.ju.execute("update shelf set shelf_storageNum = shelf_storageNum - ? where shelf_id = ?", num, id);
                        Global.ju.execute("insert into storage values(default,?,?,?,?)", warehouse_result, good_id, id, num);
                    }
                }
                Global.ju.execute("update warehouse set warehouse_storagenum = warehouse_storagenum - ? where warehouse_id = ?", goodInfo.getGoodNum(), warehouse_result);
                sql = "select storage_id from storage where storage_goodId = ?";
                resultList = Global.ju.query(sql, good_id);
                storage_id = (int) resultList.get(0).get("storage_id");
                Global.ju.execute("insert into warehouselist values(default,?,default,?)", storage_id, goodInfo.getManagerId());
                Global.ju.execute("insert into in_table values(?,?,default)",goodInfo.getOrderId(),warehouse_address);
                OrdersIdInfo idInfo = new OrdersIdInfo();
                idInfo.setOrdersId(goodInfo.getOrderId());
                if (Global.ju.query(
                        "select route " +
                                " from orders_route " +
                                "where orders_id = ?", goodInfo.getOrderId())
                        .get(0).get("route").equals("")) {
                    getAssign(idInfo);
                }

                // 发送短信
                
                String totalRoute[] = ((String) 
                		Global.ju.query("select route "
                				+ " from orders_route "
                				+ " where orders_id = ?", 
                				goodInfo.getOrderId()).get(0).get("route")).split("\\|");
                
                if(warehouse_address.equals(totalRoute[totalRoute.length-1])){ // 到达最后一战
                	
                	HashMap<String, Object> resultMap = 
                			Global.ju.query("select receiver_name, receiver_phone, orders_name from orders where orders_id = ?",
                			goodInfo.getOrderId()).get(0);
                	
                	Global.su.sendSMS(
                			(String) resultMap.get("receiver_phone"), 
                			(String)resultMap.get("receiver_name"), 
                			(String) resultMap.get("orders_name"), 
                			warehouse_address);
                }
                return "入库办理完成";
            }
        } else {
            return "订单信息输入错误";
        }
    }

    // 出库办理
    @PostMapping("/api/warehouse/exwarehousing")
    public String exwarehousing(@RequestBody GoodInfo goodInfo) {
        //写入出库记录，删除存储细节，更新仓库、货架存位
        return tmpFunction(goodInfo);
    }
    
    // 不是@Request
    public static String tmpFunction(GoodInfo goodInfo) {
    	int warehouse_id = -1;
        String shelf_id = "";
        int num;
        String sql = "";
        String warehouse_sql = "";
        String shelf_sql = "";
        ArrayList<HashMap<String, Object>> resultList;
        sql = "select good_id from good where orders_id = ?";
        ArrayList<HashMap<String, Object>> res = Global.ju.query(sql, goodInfo.getOrderId());
        if (res.size() > 0) {
            String good_id = res.get(0).get("good_id").toString();
            sql = "select storage_warehouseId,storage_shelfId,sotrage_num from storage where storage_goodId = ?";
            resultList = Global.ju.query(sql, good_id);
            warehouse_sql = "update warehouse set warehouse_storagenum = warehouse_storagenum + ? where warehouse_id = ?";
            shelf_sql = "update shelf set shelf_storageNum = shelf_storageNum + ? where shelf_id = ?";
            for (HashMap<String, Object> o : resultList) {
                warehouse_id = (int) o.get("storage_warehouseId");
                shelf_id = (String) o.get("storage_shelfId");
                num = (int) o.get("sotrage_num");
                Global.ju.execute(warehouse_sql, num, warehouse_id);
                Global.ju.execute(shelf_sql, num, shelf_id);
            }
            // 删除存储细节
            sql = "delete from storage where storage_goodId = ?";
            Global.ju.execute(sql, good_id);
            // 写入出库记录
            sql = "insert into ex_warehouselist values(default,?,default,?)";
            Global.ju.execute(sql, good_id, goodInfo.getManagerId());
            return "出库办理完成";
        } else {
            return "订单信息输入错误";
        }
    }
    // 按照地址查找仓库
    @PostMapping("/api/warehouse/warehouseQueryAddress")
    public ArrayList<HashMap<String, Object>> warehouseQueryAddress(@RequestBody WarehouseInfo warehouseInfo) {
        String sql = "select * from warehouse where warehouse_address like ?";
        String s = '%' + warehouseInfo.getWarehouseAddress() + '%';
        if (Global.ju.exists("select * from warehouse")) {
            return Global.ju.query(sql, s);
        }
        return null;
    }

    // 查找该货架存储货物
    @PostMapping("/api/warehouse/goodQueryByShelf")
    public ArrayList<HashMap<String, Object>> goodQueryByShelf(@RequestBody ShelfInfo shelfInfo) {
        String sql = "select good.* " +
                "from storage left join good on storage_goodId = good_id " +
                "where storage_shelfId = ? and storage_warehouseId = ?";
        return Global.ju.query(sql, shelfInfo.getShelfId(), shelfInfo.getShelfWarehouseId());
    }

    // 出库顺序表
    @PostMapping("/api/warehouse/exwarehouseSheet")
    public ArrayList<HashMap<String, Object>> exwarehouseSheet(@RequestBody WarehouseInfo warehouseInfo) {
        ArrayList<HashMap<String, Object>> totalIdList = Global.ju.query(
                "select orders_id " +
                        "from orders_position " +
                        "where warehouse_address = ?",
                warehouseInfo.getWarehouseAddress());
        ArrayList<String> answer = new ArrayList<>();

        for (HashMap<String, Object> stringObjectHashMap : totalIdList) {
            String currentId = (String) stringObjectHashMap.get("orders_id");

            String[] totalWarehouseAddress = ((String) Global.ju.query("select route " +
                    "from orders_route " +
                    "where orders_id = ?", currentId).get(0).get("route")).split("\\|");

            int totalWarehouseNum = totalWarehouseAddress.length;

            if(warehouseInfo.getWarehouseToAddress().equals("!")){
                for (int j = 0; j < totalWarehouseNum; ++j) {
                    answer.add(currentId);
                }
            }else{
                for (int j = 0; j < totalWarehouseNum; ++j) {
                    if (totalWarehouseAddress[j].equals(warehouseInfo.getWarehouseAddress()) &&
                            totalWarehouseAddress[j + 1].equals(warehouseInfo.getWarehouseToAddress())
                    ){
                        // warning: 如果是最后一站，会越界
                        answer.add(currentId);
                        break;
                    }
                }
            }
        }

        // 待优化
        String sql = "SELECT `good`.* " +
                "FROM `storage` LEFT JOIN `warehouselist`on `warehouselist`.`list_storageId` = `storage_id` " +
                "LEFT JOIN `good`on  `good`.`good_id` = `storage_goodId` " +
                "ORDER BY ( `good`.`priority`*0.5 + datediff(now(), `list_warehouseTime` )*0.5) desc";

        ArrayList<HashMap<String, Object>> list = Global.ju.query(sql);
        ArrayList<HashMap<String,Object>> res = new ArrayList<>();

        for(int i = 0; i < list.size(); ++ i){
            for(int j = 0;j < answer.size(); ++ j) {
                if((list.get(i).get("orders_id").toString().equals(answer.get(j)))){
                    res.add(list.get(i));
                    break;
                }
            }
        }
        return res;
    }
    // 根据省份查询仓库
    @PostMapping("/api/warehouse/warehouseProvince")
    public ArrayList<HashMap<String,Object>> warehouseProvince(@RequestBody WarehouseInfo warehouseInfo){
        String sql = "select * from warehouse where warehouse_province like ?";
        String province = '%' + warehouseInfo.getWarehouseProvince() + '%';
        ArrayList<HashMap<String,Object>> list = Global.ju.query(sql,province);
        return list;
    }
    // 根据城市查询仓库
    @PostMapping("/api/warehouse/warehouseCity")
    public ArrayList<HashMap<String,Object>> warehouseCity(@RequestBody WarehouseInfo warehouseInfo){
        String sql = "select * from warehouse where warehouse_city like ?";
        String city = '%' + warehouseInfo.getWarehouseCity() + '%';
        ArrayList<HashMap<String,Object>> list = Global.ju.query(sql,city);
        return list;
    }

    // 根据地区查询仓库
    @PostMapping("/api/warehouse/warehouseDistrict")
    public ArrayList<HashMap<String,Object>> warehouseDistrict(@RequestBody WarehouseInfo warehouseInfo){
        String sql = "select * from warehouse where warehouse_district like ?";
        String district = '%' + warehouseInfo.getWarehouseDistrict() + '%';
        ArrayList<HashMap<String,Object>> list = Global.ju.query(sql,district);
        return list;
    }
    // 修改货架信息
    @PostMapping("/api/warehouse/shelfUpdate")
    public String shelfUpdate(@RequestBody ShelfInfo shelfInfo){
        String sql = "";
        if(!StringUtils.isNullOrEmpty(shelfInfo.getNewShelfId())&&StringUtils.isNullOrEmpty(shelfInfo.getNewShelfStorageNum())){
            // 货架号不为空
            Global.ju.execute("update shelf set shelf_id = ? where shelf_id = ? and shelf_warehouseId = ?",shelfInfo.getNewShelfId(),shelfInfo.getShelfId(),shelfInfo.getShelfWarehouseId());
            Global.ju.execute("update storage set storage_shelfId = ? where storage_shelfId = ?",shelfInfo.getNewShelfId(),shelfInfo.getNewShelfId());
            return "已更新货架号";
        }else if(StringUtils.isNullOrEmpty(shelfInfo.getNewShelfId())&&!StringUtils.isNullOrEmpty(shelfInfo.getNewShelfStorageNum())){
            // 货架存位不为空
            int deltaNum = Integer.parseInt(Global.ju.query("select shelf_storageNum from shelf where shelf_id = ? and shelf_warehouseId = ?",shelfInfo.getShelfId(),shelfInfo.getShelfWarehouseId()).get(0).get("shelf_storageNum").toString()) - Integer.parseInt(shelfInfo.getNewShelfStorageNum());
            Global.ju.execute("update shelf set shelf_storageNum = ? where shelf_id = ? and shelf_warehouseId = ?",shelfInfo.getNewShelfStorageNum(),shelfInfo.getShelfId(),shelfInfo.getShelfWarehouseId());
            sql = "update warehouse set warehouse_storagenum = warehouse_storagenum - ? where warehouse_id = ?";
            Global.ju.execute(sql,deltaNum,shelfInfo.getShelfWarehouseId());
            return "已更新货架存位";
        }else if(!StringUtils.isNullOrEmpty(shelfInfo.getNewShelfId())&&!StringUtils.isNullOrEmpty(shelfInfo.getNewShelfStorageNum())) {
            // 都不空
            int deltaNum = Integer.parseInt(Global.ju.query("select shelf_storageNum from shelf where shelf_id = ? and shelf_warehouseId = ?",
                    shelfInfo.getShelfId(),shelfInfo.getShelfWarehouseId()).get(0).get("shelf_storageNum").toString()) - Integer.parseInt(shelfInfo.getNewShelfStorageNum());
            Global.ju.execute("update storage set storage_shelfId = ? where storage_shelfId = ?",shelfInfo.getNewShelfId(),shelfInfo.getNewShelfId());
            Global.ju.execute("update shelf set shelf_id = ?,shelf_storageNum = ? where shelf_id = ? and shelf_warehouseId = ?", shelfInfo.getNewShelfId(), shelfInfo.getNewShelfStorageNum(),shelfInfo.getShelfId(),shelfInfo.getShelfWarehouseId());
            sql = "update warehouse set warehouse_storagenum = warehouse_storagenum - ? where warehouse_id = ?";
            Global.ju.execute(sql, deltaNum, shelfInfo.getShelfWarehouseId());
            return "已更新货架号与货架存位";
        }
        return "请确认修改项";
    }

    // 最优路径
    public void getAssign(OrdersIdInfo ordersIdInfo) {
        ArrayList<HashMap<String, Object>> resList =
                Global.ju.query("select sender_address, receiver_address, " +
                        " cast(receiver_lat as double) as receiver_lat," +
                        " cast(receiver_lng as double) as receiver_lng "
                        + " from orders "
                        + " where orders_id = ?", ordersIdInfo.getOrdersId());

        String[] formatSenderAddress = ((String) resList.get(0).get("sender_address")).split("\\|");
        String[] formatReceiverAddress = ((String) resList.get(0).get("receiver_address")).split("\\|");

        // 获取收件人的经纬度
        Double receiverLat = (Double) resList.get(0).get("receiver_lat");
        Double receiverLng = (Double) resList.get(0).get("receiver_lng");


        Double fromLng = 0.0, fromLat = 0.0, toLng = 0.0, toLat = 0.0;

        // 物品初始所在接收站的经纬度
        ArrayList<HashMap<String, Object>> currentPositionList =
                Global.ju.query("select warehouse_address, " +
                        " cast(warehouse_lng as double) as warehouse_lng, " +
                        " cast(warehouse_lat as double) as warehouse_lat "
                        + "from orders_position "
                        + "where orders_id = ?", ordersIdInfo.getOrdersId());
        // result
        String fromWarehouseAddress = (String) currentPositionList.get(0).get("warehouse_address");
        fromLng = (Double) currentPositionList.get(0).get("warehouse_lng");
        fromLat = (Double) currentPositionList.get(0).get("warehouse_lat");

        // 找到收件人的所在的市内 离 收件地址最近的接收站
        // 查询接收站时应该限定仓库级别为1--樊华修改
        ArrayList<HashMap<String, Object>> totalPossibleWareHouse =
                Global.ju.query("select warehouse_address, " +
                        " cast(warehouse_lng as double) as warehouse_lng, " +
                        " cast(warehouse_lat as double) as warehouse_lat "
                        + " from warehouse "
                        + " where warehouse_city = ? and warehouse_type = 1", formatReceiverAddress[1]);

        int targetIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < totalPossibleWareHouse.size(); ++i) {
            toLng = (Double) totalPossibleWareHouse.get(i).get("warehouse_lng");
            toLat = (Double) totalPossibleWareHouse.get(i).get("warehouse_lat");

            double curDis = Global.getDistance(receiverLat, receiverLng, toLat, toLng);
            if (curDis < minDistance) {
                targetIndex = i;
                minDistance = curDis;
            }
        }

        // result
        String toWarehouseAddress = (String) totalPossibleWareHouse.get(targetIndex).get("warehouse_address");
        toLng = (Double) totalPossibleWareHouse.get(targetIndex).get("warehouse_lng");
        toLat = (Double) totalPossibleWareHouse.get(targetIndex).get("warehouse_lat");


        if (formatSenderAddress[0].equals(formatReceiverAddress[0])) {
            if (formatSenderAddress[1].equals(formatReceiverAddress[1])) { // 市内运输, 1-2-1类别
                ArrayList<HashMap<String, Object>> totalPossibleWareHouse_LEVEL2 =
                        Global.ju.query("select warehouse_address, " +
                                " cast(warehouse_lng as double) as warehouse_lng, " +
                                " cast(warehouse_lat as double) as warehouse_lat "
                                + " from warehouse "
                                + " where warehouse_city = ? and warehouse_type = 2", formatReceiverAddress[1]);


                int targetLEVEL2Index = 0;

                minDistance = 1000000000.0;
                for (int i = 0; i < totalPossibleWareHouse_LEVEL2.size(); ++i) {
                    Double curLng = (Double) totalPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
                    Double curLat = (Double) totalPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");

                    double curDistance = Global.getDistance(fromLat, fromLng, curLat, curLng) +
                            Global.getDistance(toLat, toLng, curLat, curLng);

                    if (curDistance < minDistance) {
                        targetLEVEL2Index = i;
                        minDistance = curDistance;
                    }
                }

                String LEVEL2WarehouseAddress = (String) totalPossibleWareHouse_LEVEL2.
                        get(targetLEVEL2Index).get("warehouse_address");
                // 路线安排完毕
                String resultRoute = fromWarehouseAddress + "|" + LEVEL2WarehouseAddress + "|" + toWarehouseAddress;

                Global.ju.execute("update orders_route set route = ? where orders_id = ? ", resultRoute, ordersIdInfo.getOrdersId());

            } else { // 省内跨市运输 1-2-3-2-1\
                Double From2Lng = 0.0, From2Lat = 0.0, To2Lng = 0.0, To2Lat = 0.0;
                // 1-2
                ArrayList<HashMap<String, Object>> totalFromPossibleWareHouse_LEVEL2 =
                        Global.ju.query("select warehouse_address, " +
                                " cast(warehouse_lng as double) as warehouse_lng, " +
                                " cast(warehouse_lat as double) as warehouse_lat"
                                + " from warehouse "
                                + " where warehouse_city = ? and warehouse_type = 2", formatSenderAddress[1]);


                int targetFromLEVEL2Index = 0;
                minDistance = Double.MAX_VALUE;
                for (int i = 0; i < totalFromPossibleWareHouse_LEVEL2.size(); ++i) {
                    Double curLng = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
                    Double curLat = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");

                    // 计算二级仓库与发件接收站距离
                    double curDistance = Global.getDistance(fromLat, fromLng, curLat, curLng);

                    if (curDistance < minDistance) {
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
                        Global.ju.query("select warehouse_address, " +
                                " cast(warehouse_lng as double) as warehouse_lng, " +
                                " cast(warehouse_lat as double) as warehouse_lat "
                                + " from warehouse "
                                + " where warehouse_city = ? and warehouse_type = 2", formatReceiverAddress[1]);

                int targetToLEVEL2Index = 0;
                minDistance = Double.MAX_VALUE;
                for (int i = 0; i < totalToPossibleWareHouse_LEVEL2.size(); ++i) {
                    Double curLng = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
                    Double curLat = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");

                    // 计算收件仓库与二级接收站距离
                    double curDistance = Global.getDistance(toLat, toLng, curLat, curLng);

                    if (curDistance < minDistance) {
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
                        Global.ju.query("select warehouse_address, " +
                                " cast(warehouse_lng as double) as warehouse_lng, " +
                                " cast(warehouse_lat as double) as warehouse_lat "
                                + " from warehouse where warehouse_type = 3");


                int targetLEVEL3Index = 0;
                minDistance = 1000000000.0;
                for (int i = 0; i < totalFromPossibleWareHouse_LEVEL3.size(); ++i) {
                    Double curLng = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lng");
                    Double curLat = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lat");

                    double curDistance = Global.getDistance(From2Lat, From2Lng, curLat, curLng) +
                            Global.getDistance(To2Lat, To2Lng, curLat, curLng);

                    if (curDistance < minDistance) {
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
                                ToLevel2WarehouseAddress + "|" + toWarehouseAddress;

                Global.ju.execute("update orders_route set route = ? where orders_id = ? ", resultRoute, ordersIdInfo.getOrdersId());
            }
        } else { // 跨省运输 1-2-3-3-2-1
            Double From2Lng = 0.0, From2Lat = 0.0, To2Lng = 0.0, To2Lat = 0.0;
            @SuppressWarnings("unused")
            Double From3Lng = 0.0, From3Lat = 0.0, To3Lng = 0.0, To3Lat = 0.0;

            // 1-2
            ArrayList<HashMap<String, Object>> totalFromPossibleWareHouse_LEVEL2 =
                    Global.ju.query("select warehouse_address, " +
                            " cast(warehouse_lng as double) as warehouse_lng, " +
                            " cast(warehouse_lat as double) as warehouse_lat "
                            + " from warehouse "
                            + " where warehouse_city = ? and warehouse_type = 2", formatSenderAddress[1]);


            int targetFromLEVEL2Index = 0;
            minDistance = Double.MAX_VALUE;
            for (int i = 0; i < totalFromPossibleWareHouse_LEVEL2.size(); ++i) {
                Double curLng = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
                Double curLat = (Double) totalFromPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");

                // 计算二级仓库与发件接收站举例
                double curDistance = Global.getDistance(fromLat, fromLng, curLat, curLng);

                if (curDistance < minDistance) {
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
                    Global.ju.query("select warehouse_address, " +
                            " cast(warehouse_lng as double) as warehouse_lng, " +
                            " cast(warehouse_lat as double) as warehouse_lat "
                            + " from warehouse "
                            + " where warehouse_city = ? and warehouse_type = 2", formatReceiverAddress[1]);


            int targetToLEVEL2Index = 0;
            minDistance = Double.MAX_VALUE;
            for (int i = 0; i < totalToPossibleWareHouse_LEVEL2.size(); ++i) {
                Double curLng = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lng");
                Double curLat = (Double) totalToPossibleWareHouse_LEVEL2.get(i).get("warehouse_lat");

                // 计算收件仓库与二级接收站距离
                double curDistance = Global.getDistance(toLat, toLng, curLat, curLng);
                if (curDistance < minDistance) {
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
                    Global.ju.query("select warehouse_address, " +
                            " cast(warehouse_lng as double) as warehouse_lng, " +
                            " cast(warehouse_lat as double) as warehouse_lat "
                            + " from warehouse "
                            + " where warehouse_province = ? and warehouse_type = 3", formatSenderAddress[0]);


            int targetFromLEVEL3Index = 0;

            minDistance = 1000000000.0;
            for (int i = 0; i < totalFromPossibleWareHouse_LEVEL3.size(); ++i) {
                Double curLng = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lng");
                Double curLat = (Double) totalFromPossibleWareHouse_LEVEL3.get(i).get("warehouse_lat");

                // 计算二级仓库与三级仓库距离
                double curDistance = Global.getDistance(From2Lat, From2Lng, curLat, curLng);

                if (curDistance < minDistance) {
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
                    Global.ju.query("select warehouse_address, " +
                            " cast(warehouse_lng as double) as warehouse_lng, " +
                            " cast(warehouse_lat as double) as warehouse_lat "
                            + " from warehouse "
                            + " where warehouse_province = ? and warehouse_type = 3", formatReceiverAddress[0]);


            int targetToLEVEL3Index = 0;

            minDistance = 1000000000.0;
            for (int i = 0; i < totalToPossibleWareHouse_LEVEL3.size(); ++i) {
                Double curLng = (Double) totalToPossibleWareHouse_LEVEL3.get(i).get("warehouse_lng");
                Double curLat = (Double) totalToPossibleWareHouse_LEVEL3.get(i).get("warehouse_lat");

                // 计算二级仓库与三级仓库距离
                double curDistance = Global.getDistance(To2Lat, To2Lng, curLat, curLng);

                if (curDistance < minDistance) {
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
            Global.ju.execute("update orders_route set route = ? where orders_id = ? ", resultRoute, ordersIdInfo.getOrdersId());
        }
    }

    @PostMapping("/api/warehouse/goodGet")
    public String goodGet(@RequestBody GoodInfo goodInfo){
        String sql = "select warehouse_address " +
                "from storage left join good on storage_goodId = good_id " +
                "left join warehouse_id = storage_warehouseId " +
                "where orders_id = ?";
        ArrayList<HashMap<String,Object>> list = Global.ju.query(sql,goodInfo.getOrderId());
        if(list.size() > 0){
            if(list.get(0).get("warehouse_address").equals(goodInfo.getWarehouseInfo().getWarehouseAddress())){
                tmpFunction(goodInfo);
                return "取件成功";
            }else {
                return "货物未到目的驿站";
            }
        }else{
            return "请确认订单号";
        }
    }
}
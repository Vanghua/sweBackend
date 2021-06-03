package controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import warehouseSystem.trans.GoodInfo;
import warehouseSystem.trans.QueryInfo;
import warehouseSystem.trans.ShelfInfo;
import warehouseSystem.trans.WarehouseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    1.货物添加
    2.仓库添加
    3.货架添加
    4.入库办理
    5.出库办理
    6.货物存储位置查询
*/
@RestController
public class WarehouseController {
    // 货物添加
    @PostMapping("/api/warehouse/addGood")
    public String addGood(@RequestBody GoodInfo goodInfo) {
        if (Global.ju.exists("select * from good where good_TsegmentCode = ?", goodInfo.getGoodTsegmentcode())) {
            return "货物已存在";
        } else {
            Global.ju.execute("insert into good values(default,?,?,?,?)",
                    goodInfo.getGoodName(),
                    goodInfo.getGoodNum(),
                    goodInfo.getGoodTsegmentcode(),
                    goodInfo.getGoodType());
            return "货物已成功录入";
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
        if (Global.ju.exists("select * from warehouse where warehouse_lng = ? and warehouse_lat = ?", warehouseInfo.getWarehouseLng(), warehouseInfo.getWarehouseLat())) {
            return "仓库已存在";
        } else {
            Global.ju.execute("insert into warehouse values(default,?,?,?,?,?,default,?,?)",
                    warehouseInfo.getWarehouseType(),
                    warehouseInfo.getWarehhouseStoragenum(),
                    warehouseInfo.getWarehouseAddress(),
                    warehouseInfo.getWarehouseManager(),
                    warehouseInfo.getWarehouseManagerTel(),
                    warehouseInfo.getWarehouseLng(),
                    warehouseInfo.getWarehouseLat());
            return "仓库已成功添加";
        }
    }

    // 全部仓库查询
    @PostMapping("/api/warehouse/warehousQueryAll")
    public ArrayList<HashMap<String, Object>> warehouseQueryAll(@RequestBody QueryInfo queryInfo) {
        String sql = "select * from warehouse";
        ArrayList<HashMap<String, Object>> resultList;
        ArrayList<ArrayList<HashMap<String, Object>>> listAll = new ArrayList<>();
        resultList = Global.ju.query(sql);
        int size = resultList.size();
        int count = queryInfo.getPageCount();
        if (size > count) {
            int absInt = Math.abs(size / count);
            if (size - absInt * count > 0) {
                listAll.add((ArrayList<HashMap<String, Object>>) resultList.subList(absInt * count, size));
            }
            for (int i = 1; i < absInt + 1; ++i) {
                listAll.add((ArrayList<HashMap<String, Object>>) resultList.subList((i - 1) * count, i * count));
            }
        } else {
            listAll.add(resultList);
        }
        return listAll.get(queryInfo.getPageNum());
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
        if (Global.ju.exists("select * from shelf where shelf_id = ?", shelfInfo.getShelfId())) {
            return "货架已存在";
        } else if (!Global.ju.exists("select * from warehouse where warehouse_id = ?", shelfInfo.getShelfWarehouseId())) {
            return "仓库不存在";
        } else {
            Global.ju.execute("insert into shelf values(?,?,?)",
                    shelfInfo.getShelfId(),
                    shelfInfo.getShelfWarehouseId(),
                    shelfInfo.getShelfStorageNum());
            return "货架已成功添加";
        }
    }

    //全部货架查询
    @PostMapping("/api/warehouse/shelfQueryAll")
    public ArrayList<HashMap<String, Object>> shelfQueryAll(@RequestBody QueryInfo queryInfo) {
        String sql = "select * from shelf where shelf_warehouseId = ?";
        ArrayList<HashMap<String, Object>> resultList;
        ArrayList<ArrayList<HashMap<String, Object>>> listAll = new ArrayList<>();
        resultList = Global.ju.query(sql, queryInfo.getWarehouseInfo().getWarehouseId());
        int size = resultList.size();
        int count = queryInfo.getPageCount();
        if (size > count) {
            int absInt = Math.abs(size / count);
            if (size - absInt * count > 0) {
                listAll.add((ArrayList<HashMap<String, Object>>) resultList.subList(absInt * count, size));
            }
            for (int i = 1; i < absInt + 1; ++i) {
                listAll.add((ArrayList<HashMap<String, Object>>) resultList.subList((i - 1) * count, i * count));
            }
        } else {
            listAll.add(resultList);
        }
        return listAll.get(queryInfo.getPageNum());
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
        int warehouse_result = -1;
        int shelf_res = -1;
        int num = -1;
        int storage_id = -1;
        String sql = "";
        String id = "";
        ArrayList<HashMap<String, Object>> resultList;
        if (Global.ju.exists("select * from storage where storage_goodId = ?", goodInfo.getGoodId())) {
            return "货物已入库";
        } else if (!Global.ju.exists("select * from warehouse where warehouse_storagenum >= ?", goodInfo.getGoodNum())) {
            return "当前存位不足,无法办理入库";
        } else {
            sql = "select warehouse_id from warehouse where warehouse_storagenum >= ?";
            resultList = Global.ju.query(sql, goodInfo.getGoodNum());
            warehouse_result = (int) resultList.get(0).get("warehouse_id");
            sql = "select shelf_id from shelf where shelf_storageNum >= ? and shelf_warehouseId = ?";
            resultList = Global.ju.query(sql, goodInfo.getGoodNum(), warehouse_result); // 查出可以直接存入货物的货架
            String shelf_id;
            num = goodInfo.getGoodNum();
            if (resultList.size() > 0) { // 找到可以直接存入货物的货架
                shelf_id = (String) resultList.get(0).get("shelf_id");
                Global.ju.execute("update shelf set shelf_storageNum =shelf_storageNum - ? where shelf_id = ?", goodInfo.getGoodNum(), shelf_id);
                Global.ju.execute("insert into storage values(default,?,?,?,?)", warehouse_result, goodInfo.getGoodId(), shelf_id, goodInfo.getGoodNum());
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
                    Global.ju.execute("insert into storage values(NULL,?,?,?,?)", warehouse_result, goodInfo.getGoodId(), id, num);
                }
            }
            Global.ju.execute("update warehouse set warehouse_storagenum = warehouse_storagenum - ? where warehouse_id = ?", goodInfo.getGoodNum(), warehouse_result);
            sql = "select storage_id from storage where storage_goodId = ?";
            resultList = Global.ju.query(sql, goodInfo.getGoodId());
            storage_id = (int) resultList.get(0).get("storage_id");
            Global.ju.execute("insert into warehouselist values(default,?,default,?)", storage_id, goodInfo.getManagerId());
            return "入库办理完成";
        }
    }

    // 出库办理
    @PostMapping("/api/warehouse/exwarehousing")
    public String exwarehousing(@RequestBody GoodInfo goodInfo) {
        //写入出库记录，删除存储细节，更新仓库、货架存位
        int warehouse_id = -1;
        String shelf_id = "";
        int num;
        String sql = "";
        String warehouse_sql = "";
        String shelf_sql = "";
        ArrayList<HashMap<String, Object>> resultList;
        if (!Global.ju.exists("select * from storage where storage_goodId = ?", goodInfo.getGoodId())) {
            return "货物不存在";
        } else {
            sql = "select storage_warehouseId,storage_shelfId,sotrage_num from storage where storage_goodId = ?";
            resultList = Global.ju.query(sql, goodInfo.getGoodId());
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
            Global.ju.execute(sql, goodInfo.getGoodId());
            // 写入出库记录
            sql = "insert into ex_warehouselist values(default,?,default,?)";
            Global.ju.execute(sql, goodInfo.getGoodId(), goodInfo.getManagerId());
            return "出库办理完成";
        }
    }
}

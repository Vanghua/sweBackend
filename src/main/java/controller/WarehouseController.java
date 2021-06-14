package controller;

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
        System.out.println(warehouseInfo.getWarehouseId());
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
                Global.ju.execute("update warehouse set warehouse_storagenum = warehouse_storagenum - ?", storageNum);
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
        int shelf_res = -1;
        int num = -1;
        int storage_id = -1;
        String sql = "";
        String id = "";
        ArrayList<HashMap<String, Object>> resultList;
        if (Global.ju.exists("select * from storage where storage_goodId = ?", goodInfo.getGoodId())) {
            return "货物已入库";
        } else if (!Global.ju.exists("select * from warehouse where warehouse_storagenum >= ? and warehouse_id = ?", goodInfo.getGoodNum(),warehouse_result)) {
            return "当前存位不足,无法办理入库";
        } else {
            sql = "select shelf_id from shelf where shelf_storageNum >= ? and shelf_warehouseId = ?";
            resultList = Global.ju.query(sql, goodInfo.getGoodNum(),warehouse_result); // 查出当前可以直接存入货物的货架
            String shelf_id;
            num = goodInfo.getGoodNum();
            if (resultList.size() > 0) { // 找到可以直接存入货物的货架
                shelf_id = (String) resultList.get(0).get("shelf_id");
                Global.ju.execute("update shelf set shelf_storageNum = shelf_storageNum - ? where shelf_id = ? and shelf_warehouseId = ?", goodInfo.getGoodNum(), shelf_id,warehouse_result);
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
                    Global.ju.execute("insert into storage values(default,?,?,?,?)", warehouse_result, goodInfo.getGoodId(), id, num);
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

    // 按照地址查找仓库
    @PostMapping("/api/warehouse/warehouseQueryAddress")
    public ArrayList<HashMap<String, Object>> warehouseQueryAddress(@RequestBody WarehouseInfo warehouseInfo) {
        String sql = "select * from warehouse where warehouse_address like ?";
        String s = '%'+warehouseInfo.getWarehouseAddress()+'%';
        if(Global.ju.exists("select * from warehouse")){
            return Global.ju.query(sql,s);
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
    public ArrayList<HashMap<String, Object>> exwarehouseSheet() {
        String sql = "select good.good_id,good.good_name " +
                "from good left join storage on good.orders_id = orders.orders_id " +
                "lefet join warehouselist on storage_id = list_storageId " +
                "order by (good.priority * 0.5 + datediff(now(),list_warehouseTime)*0.5) desc";
        return Global.ju.query(sql);
    }
}

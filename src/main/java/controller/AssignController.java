package controller;

import assignSystem.trans.Trans;
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
            sql = "update trans set warehouse_id = ?, trans_weight = ? where account_name = ?";
            Global.ju.execute(sql, trans.getWarehouseId(), trans.getTransWeight(), trans.getAccountName());
            return "成功";
        }

        // 运输员不存在则插入运输员表
        sql = "insert into trans values(?,?)";
        Global.ju.execute(sql, trans.getAccountName(), trans.getWarehouseId());
        return "成功";
    }

    // 运输员信息获取
    @PostMapping("/api/assign/getTransInfo")
    public HashMap<String, Object> getTransInfo(@RequestBody Trans trans) {
        // 查询运输员是否存在
        String sql = "select * from trans where account_name = ?";
        ArrayList<HashMap<String, Object>> resultList;
        resultList = Global.ju.query("select * from trans where account_name = ?", trans.getAccountName());
        if(resultList.size() == 0)
            return null;
        else {
            return resultList.get(0);
        }
    }
}

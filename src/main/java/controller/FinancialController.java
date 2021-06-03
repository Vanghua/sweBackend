package controller;

import FinancialSystem.trans.Financialnfo;
import FinancialSystem.trans.GoodsInfo;
import FinancialSystem.trans.PurchaseInfo;
import FinancialSystem.trans.SalaryInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

@RestController
//实现财务账目的查询，返回ArrayList
public class FinancialController {
    @PostMapping("api/Financial/query")
    public ArrayList<Financialnfo> getFinancialInfo(@RequestBody Financialnfo Fin) {
        String sql = "select * from financialbill where type=? ";
        ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql, Fin.getType());

        ArrayList<Financialnfo> far = new ArrayList<Financialnfo>();
        for(int i=0;i<resultList.size();i++){
            Financialnfo info = new Financialnfo((int)resultList.get(i).get("billid"),(int) resultList.get(i).get("money"),(String) resultList.get(i).get("time"),(String) resultList.get(i).get("type"));
            far.add(info);
        }
        return far;
    }
//实现添加货物，返回添加操作是否成功
    @PostMapping("api/Financial/addgood")
    public String addgood(@RequestBody GoodsInfo good) {
        String sql = "insert into buything(name, type,price) value(?,?,?)";
        boolean status = Global.ju.execute(sql, good.getName(), good.getType(), good.getPrice());
        if (status == true)
            return "成功";
        else
            return "错误";
    }
//实现采购物品的购买记录
    @PostMapping("api/Finacial/purchase")
    public String purchase(@RequestBody PurchaseInfo pur){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        boolean sta_purchase_1 = Global.ju.execute("insert into buybill(buydate,money) value(?,?)",pur.getTime(),pur.getMoney());
        if(sta_purchase_1 == true) {
            boolean sta_purchase_2 = Global.ju.execute("insert into buydatail(buybillid,buythingid,num) value((select max(id) from buybill),(select id from buything where name=?),?) ", pur.getName(), pur.getNum());
            if(sta_purchase_2 == true){
                boolean sta_purchase_3 = Global.ju.execute("insert into financialbill(money,time,type) value(?,?,?)",pur.getMoney(),calendar.getTime(),"采购物品");
                if(sta_purchase_3 == true)
                    return "生成购买记录成功";
                else
                    return "生成购买记录失败";
            }
            else
                return "生成购买记录失败";
        }
        else
            return "生成购买记录失败";


    }
//实现发放工资，按照员工类型发放，也可一给某个员工发放,也可调整发放的金额，
//添加属性列received判断是否给员工已经发放工资，发放工资根据签到天数和接单数决定           ！！！！注意 根据接单数还没有实现，等待樊华创建接单属性列之后
    @PostMapping("api/Financial/salsry")
    public String relesesalary(@RequestBody SalaryInfo salary){
        Boolean status = true;
        if(salary.getName().isEmpty()) {
            String sql_type = "update payoff set time = ?, money = (select money from salary where type=?)*(select day_num from checkingin where type=?),received='是' " +
                    "where payname=(select workername from salary where type=?)";
            boolean sta_type = Global.ju.execute(sql_type, salary.getTime(), salary.getType(), salary.getType(), salary.getType());
            if (sta_type == true) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                boolean sta_1 = Global.ju.execute("insert into financialbill(money, time,type) value((SELECT SUM( `money`)  FROM `payoff` WHERE `payname` = (select workername from salary where type=?) ),?,?)",salary.getType(),calendar.getTime(),"工资发放" );
                if(sta_1 == true)
                    return "工资发放成功";
                else return "工资发放失败";

            } else
                return "工资发放失败";
        }
        else if(!salary.getName().isEmpty()){
            return "不能同时选择两项";
        }
        else {
            String sql_name = "update payoff set time = ?,money = (select money from salary where workername = ?)*(select day_num from checking where name = ?),received='是' "+
                    "where payname = ?";
            boolean sta_name = Global.ju.execute(sql_name,salary.getTime(),salary.getName(),salary.getName(),salary.getName());
            if(sta_name == true){
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                boolean sta_2 = Global.ju.execute("update financialbill(money,time,type) value((select money from payoff where payname = ?),?,?)",
                salary.getName(),calendar.getTime(),"工资");
                if(sta_2 == true)
                    return "工资发放成功";
                else return "工资发放失败";
            }
            else return "工资发放失败";
        }

    }

}

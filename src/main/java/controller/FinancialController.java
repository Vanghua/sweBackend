package controller;

import FinancialSystem.trans.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RestController
/**
 *
 */
//实现财务账目的查询，返回ArrayList
public class FinancialController {
    @PostMapping("api/Financial/query")
    public ArrayList<Financialnfo> getFinancialInfo(@RequestBody Financialnfo Fin) {
        ArrayList<Financialnfo> far = new ArrayList<Financialnfo>();
        //同时按照时间，金额，类型进行账单查询
        if(!Fin.getMoney().isEmpty() && !Fin.getTime_start().isEmpty() && !Fin.getTime_end().isEmpty() && !Fin.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from "+
                    "financialbill where money=? and date(time) between ? and ? and type = ?",
                    Integer.parseInt(Fin.getMoney()),Fin.getTime_start(),Fin.getTime_end(),Fin.getType());
            for(int i=0;i<resultList.size();i++){
                Financialnfo info = new Financialnfo((int) resultList.get(i).get("financeid"),(int)resultList.get(i).get("billid"),resultList.get(i).get("money").toString(),(Date) resultList.get(i).get("time"),(String) resultList.get(i).get("type"));
                far.add(info);
            }
            return far;

        }
        //按照时间和账单类型进行账单查询
        else if(Fin.getMoney().isEmpty() && !Fin.getTime_start().isEmpty() && !Fin.getTime_end().isEmpty() && !Fin.getType().isEmpty()) {
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from financialbill " +
                            "where date(time) between ? and ? and type = ?",
                    Fin.getTime_start(), Fin.getTime_end(), Fin.getType());
            for (int i = 0; i < resultList.size(); i++) {
                Financialnfo info = new Financialnfo((int) resultList.get(i).get("financeid"),(int) resultList.get(i).get("billid"), resultList.get(i).get("money").toString(), (Date) resultList.get(i).get("time"), (String) resultList.get(i).get("type"));
                far.add(info);
            }
            return far;
        }
        //按照账单类型进行查询
        else if(Fin.getMoney().isEmpty() && Fin.getTime_start().isEmpty() && Fin.getTime_end().isEmpty() && !Fin.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from financialbill " +
                            "where type = ?", Fin.getType());
            for (int i = 0; i < resultList.size(); i++) {
                Financialnfo info = new Financialnfo((int) resultList.get(i).get("financeid"),(int) resultList.get(i).get("billid"), resultList.get(i).get("money").toString(), (Date) resultList.get(i).get("time"), (String) resultList.get(i).get("type"));
                far.add(info);
            }
            return far;
        }
        //按照金额和账单类型进行查询
        else if(!Fin.getMoney().isEmpty() && Fin.getTime_start().isEmpty() && Fin.getTime_end().isEmpty() && !Fin.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from financialbill " +
                            "where money = ? and type = ?",
                    Integer.parseInt(Fin.getMoney()), Fin.getType());
            for (int i = 0; i < resultList.size(); i++) {
                Financialnfo info = new Financialnfo((int) resultList.get(i).get("financeid"),(int) resultList.get(i).get("billid"), resultList.get(i).get("money").toString(), (Date) resultList.get(i).get("time"), (String) resultList.get(i).get("type"));
                far.add(info);
            }
            return far;

        }
        //按照账单金额和时间进行查询
        else if(!Fin.getMoney().isEmpty() && !Fin.getTime_start().isEmpty() && !Fin.getTime_end().isEmpty() && Fin.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from financialbill " +
                            "where money = ? and date(time) between ? and ?",
                    Integer.parseInt(Fin.getMoney()), Fin.getTime_start(),Fin.getTime_end());
            for (int i = 0; i < resultList.size(); i++) {
                Financialnfo info = new Financialnfo((int) resultList.get(i).get("financeid"),(int) resultList.get(i).get("billid"), resultList.get(i).get("money").toString(), (Date) resultList.get(i).get("time"), (String) resultList.get(i).get("type"));
                far.add(info);
            }
            return far;

        }
        //按照账单金额进行账单查询
        else if(!Fin.getMoney().isEmpty() && Fin.getTime_start().isEmpty() && Fin.getTime_end().isEmpty() && Fin.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from financialbill " +
                            "where money = ? ",
                    Integer.parseInt(Fin.getMoney()));
            for (int i = 0; i < resultList.size(); i++) {
                Financialnfo info = new Financialnfo((int) resultList.get(i).get("financeid"),(int) resultList.get(i).get("billid"), resultList.get(i).get("money").toString(), (Date) resultList.get(i).get("time"), (String) resultList.get(i).get("type"));
                far.add(info);
            }
            return far;

        }
        //没有同时输入开始时间和截止时间
        else if((Fin.getTime_start().isEmpty() && !Fin.getTime_end().isEmpty()) || (!Fin.getTime_start().isEmpty() && Fin.getTime_end().isEmpty())){
            Financialnfo Info = new Financialnfo("请同时输入开始时间和截止时间");
            far.add(Info);
            return far;
        }
        //没有输入信息
        else {
            Financialnfo Info = new Financialnfo("请输入查询信息");
            far.add(Info);
            return far;

        }
//        String sql = "select * from financialbill where type=? ";
//        ArrayList<HashMap<String, Object>> resultList = Global.ju.query(sql, Fin.getType());
//
//
//        for(int i=0;i<resultList.size();i++){
//            Financialnfo info = new Financialnfo((int)resultList.get(i).get("billid"),resultList.get(i).get("money").toString(),(String) resultList.get(i).get("time"),(String) resultList.get(i).get("type"));
//            far.add(info);
//        }
//        return far;
    }
//实现添加员工工资信息
    @PostMapping("api/Financial/addsalary")
    public String addsalary(@RequestBody SalaryInfo salary){
        boolean sta_salary = Global.ju.execute("insert into salary value(?,?,?,?,?)",salary.getName(),salary.getWorkerid(),salary.getBanknum(),salary.getMoney(),salary.getType());
        boolean sta = Global.ju.execute("insert into payoff(payname_id,payname,time,money,received) value(?,?,0,0,default) ",salary.getWorkerid(),salary.getName());
        boolean sta_check = Global.ju.execute("insert into checkingin(id,name,day_num,type) value(?,?,default,?)",salary.getWorkerid(),salary.getName(),salary.getType());
        if(sta_salary == true && sta == true && sta_check == true){
            return "录入工资信息成功";
        }
        else
            return "录入工资信息失败";
    }
//实现报销，添加报销记录到报销单数据库，同时在账单中添加该记录
    @PostMapping("api/Financial/expenses")
    public String expenses(@RequestBody ExpensesInfo expenses){
        boolean sta_exp = Global.ju.execute("insert into expense(invoice_num,proposer,proposer_id,assessor,money,time) value(?,?,?,?,?,?)",expenses.getInvoice_num(),expenses.getProposer(),expenses.getProposer_id(),expenses.getAssessor(),expenses.getMoney(),expenses.getTime());
        boolean sta_bill = Global.ju.execute("insert into financialbill(billid,money,time,type) value((select max(expense_id) from expense),?,CURRENT_DATE,?)",expenses.getMoney(),"报销");
        if(sta_exp == true && sta_bill == true)
            return "报销成功";
        else
            return "报销失败";



    }
//实现添加采购物品，返回添加操作是否成功

    /**
     *
     * @param good
     * @return
     */
    @PostMapping("api/Financial/addgood")
    public String addgood(@RequestBody GoodsInfo good) {
        String sql = "insert into buything(name, type,price) value(?,?,?)";
        boolean status = Global.ju.execute(sql, good.getName(), good.getType(), good.getPrice());
        if (status == true)
            return "成功";
        else
            return "错误";
    }
//实现采购物品的购买记录，同时添加该记录到账单中
    @PostMapping("api/Finacial/purchase")
    public String purchase(@RequestBody PurchaseInfo pur){
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        boolean sta_purchase_1 = Global.ju.execute("insert into buybill(buydate,money) value(?,?)",pur.getTime(),pur.getMoney());
        if(sta_purchase_1 == true) {
            boolean sta_purchase_2 = Global.ju.execute("insert into buydatail(buybillid,buythingid,num) value((select max(id) from buybill),(select id from buything where name=?),?) ", pur.getName(), pur.getNum());
            if(sta_purchase_2 == true){
                boolean sta_purchase_3 = Global.ju.execute("insert into financialbill(money,time,type) value(?,CURRENT_DATE,?)",pur.getMoney(),"采购物品");
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
//有时间可以重新写一下，感觉逻辑有点乱，并且注意要将各个表对接使用员工id
    @PostMapping("api/Financial/salsry")
    public String relesesalary(@RequestBody PaysalaryInfo pay){
        Boolean status = true;
        //按照员工类型和时间发放工资
        if(!pay.getType().isEmpty() && pay.getName().isEmpty() && pay.getWorker_id().isEmpty() && !pay.getTime().isEmpty()){
            boolean sta_type = Global.ju.execute("update payoff set time = ?,money = (select money from salary where type = ?)*"+
                    "(select day_num from checkingin where type = ?), received = '是' "+
                    "where payname_id = (select workerid from salary where type = ?)",pay.getTime(),pay.getType(),pay.getType(),pay.getType());
            boolean sta_bill = Global.ju.execute("insert into financialbill(billid,money,time,type) value(-1,(select sum(money) from payoff where payname_id = (select workerid from salary where type = ?)),CURRENT_DATE,?)",pay.getType(),"工资发放");
            if(sta_type == true && sta_bill == true)
                return "工资发放成功";
            else
                return "工资发放失败";

        }
        //根据员工姓名ID和时间发放工资
        else if(pay.getType().isEmpty() && !pay.getName().isEmpty() && !pay.getTime().isEmpty()){
            boolean sta_time = Global.ju.execute("update payoff set time = ?,money = (select money from salary where workerid = ?)*"+
                    "(select day_num from checkingin where id = ?), received = '是' "+
                    "where payname_id = ?",pay.getTime(),pay.getWorker_id(),pay.getWorker_id(),pay.getWorker_id());
            boolean sta_bill = Global.ju.execute("insert into financialbill(billid,money,time,type) value(-1,(select money from payoff where payname_id = ?),CURRENT_DATE,?)",
                    pay.getWorker_id(),"工资发放");
            if(sta_bill == true && sta_time == true)
                return "工资发放成功";
            else
                return "工资发放失败";
        }
        else if(pay.getWorker_id().isEmpty()||pay.getName().isEmpty()){
            return "请同时输入员工姓名和员工编号";
        }
        else
            return "请按要求输入信息";


    }

}

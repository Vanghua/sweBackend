package controller;


import Humanresource.trans.HumanChangeInfo;
import Humanresource.trans.HumanDeleteInfo;
import Humanresource.trans.HumanInfo;
import Humanresource.trans.VacateInfo;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@RestController
public class HumanController {
//按照人员姓名，员工id ， 员工类型，查询
    @PostMapping("api/humanresource/query")
    public ArrayList<HumanInfo> humanQuery(@RequestBody HumanInfo human){
        ArrayList<HumanInfo> ar = new ArrayList<HumanInfo>();
        //按照员工类型查询
        if(human.getTrue_name().isEmpty() && human.getAccount_name().isEmpty() && !human.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account where account_type = ?",human.getType());
            for(int i=0;i<resultList.size();i++){
                HumanInfo hum = new HumanInfo((String) resultList.get(i).get("account_name"),(String) resultList.get(i).get("true_name"),(String) resultList.get(i).get("account_email"),(String) resultList.get(i).get("telephone"),(String) resultList.get(i).get("type"));
                ar.add(hum);
            }
            return ar;

        }
        //按照员工姓名id查询
        else if(!human.getTrue_name().isEmpty() && !human.getAccount_name().isEmpty() && human.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account where account_name = ?",human.getAccount_name());
            for(int i=0;i<resultList.size();i++){
                HumanInfo hum = new HumanInfo((String) resultList.get(i).get("account_name"),(String) resultList.get(i).get("true_name"),(String) resultList.get(i).get("account_email"),(String) resultList.get(i).get("telephone"),(String) resultList.get(i).get("type"));
                ar.add(hum);
            }
            return ar;
        }
        else if(!human.getTrue_name().isEmpty() && !human.getAccount_name().isEmpty() && human.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account where account_name = ? and account_type = ?",human.getAccount_name(),human.getType());
            for(int i=0;i<resultList.size();i++){
                HumanInfo hum = new HumanInfo((String) resultList.get(i).get("account_name"),(String) resultList.get(i).get("true_name"),(String) resultList.get(i).get("account_email"),(String) resultList.get(i).get("telephone"),(String) resultList.get(i).get("type"));
                ar.add(hum);
            }
            return ar;
        }
        //查询全部成员
        else{
            ar = null;
            return ar;
        }


    }
    @PostMapping("api/humanresource/queryAll")
    public ArrayList<HashMap<String, Object>> queryAll(@RequestBody HumanInfo human){
        ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account");
        ArrayList<ArrayList<HashMap<String, Object>>> listAll = new ArrayList<>();
        int size = resultList.size();
        int count = human.getNumber();
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
        return listAll.get(human.getCurrent_page() - 1);
    }
    @PostMapping("api/humanresourse/vacate")
    public String vacate(@RequestBody VacateInfo vacate){
        ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select day_num from checking where id = ?",vacate.getAccount_num());
        int day = (int)resultList.get(0).get("day_num");
        boolean sta_1 = Global.ju.execute("update checkingin set day_start = ? , day_num = ? where id = ?",vacate.getDay_start(),day-vacate.getDay_length(),vacate.getAccount_num());
        if(sta_1 == true)
            return "请假成功";
        else
            return "请假失败";
    }
    @PostMapping("api/humanresource/delete")
    public String delete(@RequestBody HumanDeleteInfo delete){
    //人员删除
        boolean sta_1 = Global.ju.execute("delete from account where account_name = ?",delete.getAccount_name());
        boolean sta_2 = Global.ju.execute("delete from checkingin where id = ?",delete.getAccount_name());
        boolean sta_3 = Global.ju.execute("delete from payoff where payname_id = ?",delete.getAccount_name());
        boolean sta_4 = Global.ju.execute("delete from salary where workerid = ?",delete.getAccount_name());
        if(sta_1 == true&& sta_3 == true&& sta_2 == true && sta_4 == true)
            return "人员已经成功删除";
        else
            return "人员删除失败";
    }
    @PostMapping("api/humanresource/change")
    public String change(@RequestBody HumanChangeInfo change){
        //修改邮箱
        //同时输入员工账号和员工姓名
        if(change.getAccount_name().isEmpty()|| change.getTrue_name().isEmpty())
            return "请输入人员信息";
        //修改员工邮箱信息
        else if(!change.getAccount_email().isEmpty()&&change.getAccount_type().isEmpty()&&change.getTelephone().isEmpty()) {
            boolean sta_1 = Global.ju.execute("update from account set account_email = ?", change.getAccount_email());
            if(sta_1 == true)
                return "修改成功";
            else
                return "修改失败";
        }
        else if(change.getAccount_name().isEmpty()&&change.getAccount_type().isEmpty()&&!change.getTelephone().isEmpty()){
            boolean sta_2 = Global.ju.execute("update from account set telephone = ? where account_name = ?",change.getTelephone(),change.getAccount_name());
            if(sta_2 == true)
                return "修改成功";
            else
                return "修改失败";

        }
        else if(change.getAccount_name().isEmpty()&&!change.getAccount_type().isEmpty()&&change.getTelephone().isEmpty()){
            boolean sta_3 = Global.ju.execute("update from account set account_type = ? where account-name = ?",change.getAccount_type(),change.getAccount_name());
            if(sta_3 == true)
                return "修改成功";
            else
                return "修改失败";
        }
        else if(!change.getAccount_name().isEmpty()&&!change.getAccount_type().isEmpty()&&!change.getTelephone().isEmpty()){
            boolean sta_4 = Global.ju.execute("update from account set account_email = ?,telephone = ?,account_type = ? where account_name = ?",
                    change.getAccount_email(),change.getTelephone(), change.getAccount_type(),change.getAccount_name());
            if(sta_4 == true)
                return "修改成功";
            else
                return "修改失败";
        }
        else
            return "请输入信息";
        //修改电话
        //修改人员类型
    }

}

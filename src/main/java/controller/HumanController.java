package controller;


import Humanresource.trans.HumanInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class HumanController {
//按照人员姓名，员工id ， 员工类型，查询
    @PostMapping("api/huamnresource/query")
    public ArrayList<HumanInfo> humanQuery(@RequestBody HumanInfo human){
        ArrayList<HumanInfo> ar = new ArrayList<HumanInfo>();
        //按照员工类型查询
        if(human.getTrue_name().isEmpty() && human.getAccount_name().isEmpty() && !human.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account where account_type = ?",human.getType());
            for(int i=0;i<resultList.size();i++){
                HumanInfo hum = new HumanInfo((String) resultList.get(i).get("account_name"),(String) resultList.get(i).get("true_name"),(String) resultList.get(i).get("email"),(String) resultList.get(i).get("telephone"),(String) resultList.get(i).get("type"));
                ar.add(hum);
            }
            return ar;

        }
        //按照员工姓名id查询
        else if(!human.getTrue_name().isEmpty() && !human.getAccount_name().isEmpty() && human.getType().isEmpty()){
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account where account_name = ?",human.getAccount_name());
            for(int i=0;i<resultList.size();i++){
                HumanInfo hum = new HumanInfo((String) resultList.get(i).get("account_name"),(String) resultList.get(i).get("true_name"),(String) resultList.get(i).get("email"),(String) resultList.get(i).get("telephone"),(String) resultList.get(i).get("type"));
                ar.add(hum);
            }
            return ar;
        }
        //查询全部成员
        else {
            ArrayList<HashMap<String, Object>> resultList = Global.ju.query("select * from account where account_name = ? and  account_type=?",human.getAccount_name(),human.getTrue_name());
            for(int i=0;i<resultList.size();i++){
                HumanInfo hum = new HumanInfo((String) resultList.get(i).get("account_name"),(String) resultList.get(i).get("true_name"),(String) resultList.get(i).get("email"),(String) resultList.get(i).get("telephone"),(String) resultList.get(i).get("type"));
                ar.add(hum);
            }
            return ar;
        }


    }

}

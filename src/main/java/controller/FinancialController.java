package controller;

import FinancialSystem.trans.Financialnfo;
import FinancialSystem.trans.GoodsInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
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

    @PostMapping("api/Financial/purchase")
    public void purchase(@RequestBody GoodsInfo good){



    }

}

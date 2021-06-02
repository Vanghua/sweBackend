package FinancialSystem.trans;

public class Financialnfo {
    private int billid;
    private int money;
    private String time;
    private String type;
    public Financialnfo(){

    }
    public Financialnfo(int billid,int money,String time,String type){
        this.billid = billid;
        this.money = money;
        this.time = time;
        this.type = type;

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getBillid() {
        return billid;
    }

    public void setBillid(int billid) {
        this.billid = billid;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public void setTima(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }
}

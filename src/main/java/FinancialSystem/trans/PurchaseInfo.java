package FinancialSystem.trans;

public class PurchaseInfo {
    private String name;
    private int num;
    private String time;
    private int money;
    public PurchaseInfo(){

    }
    public PurchaseInfo(String name,int num,String time,int money){
        this.name = name;
        this.num = num;
        this.time = time;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

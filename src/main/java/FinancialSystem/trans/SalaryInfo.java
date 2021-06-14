package FinancialSystem.trans;

public class SalaryInfo {
    private String name;
    private String workerid;
    private String banknum;
    private double money;
    private String type;
    public SalaryInfo(){

    }
    public SalaryInfo(String name,String workerid,String banknum,double money,String type){
        this.name = name;
        this.workerid = workerid;
        this.banknum = banknum;
        this.money = money;
        this.type = type;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

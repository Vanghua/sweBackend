package FinancialSystem.trans;

import java.util.Date;

public class Financialnfo {
    private int financeid;
    private int billid;
    private String money;
    private String time_start;
    private String time_end;
    private Date time;
    private String type;
    private String result;
    private int number;
    private int current_page;
    public Financialnfo(){

    }
    public Financialnfo(int financeid,int billid,String money,Date time,String type){
        this.financeid = financeid;
        this.billid = billid;
        this.money = money;
        this.time = time;
        this.type = type;

    }

    public int getFinanceid() {
        return financeid;
    }

    public void setFinanceid(int financeid) {
        this.financeid = financeid;
    }

    public Financialnfo(String result){
        this.result = result;
    }

    public int getBillid() {
        return billid;
    }

    public void setBillid(int billid) {
        this.billid = billid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }
}

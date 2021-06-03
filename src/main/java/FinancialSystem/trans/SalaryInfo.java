package FinancialSystem.trans;

public class SalaryInfo {
    private String type;
    private String time;
    private String name;
    public SalaryInfo(){

    }
    public SalaryInfo(String type, String time, String name){
        this.type = type;
        this.time = time;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

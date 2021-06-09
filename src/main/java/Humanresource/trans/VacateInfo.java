package Humanresource.trans;

public class VacateInfo {
    private String account_num;
    private String true_name;
    private String day_start;
    private int day_length;
    public VacateInfo(){

    }
    public VacateInfo(String account_num,String true_name,String day_start,int day_length){
        this.account_num = account_num;
        this.true_name = true_name;
        this.day_start = day_start;
        this.day_length = day_length;


    }

    public String getAccount_num() {
        return account_num;
    }

    public void setAccount_num(String account_num) {
        this.account_num = account_num;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getDay_start() {
        return day_start;
    }

    public void setDay_start(String day_start) {
        this.day_start = day_start;
    }

    public int getDay_length() {
        return day_length;
    }

    public void setDay_length(int day_length) {
        this.day_length = day_length;
    }
}

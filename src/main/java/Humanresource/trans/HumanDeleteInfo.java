package Humanresource.trans;

public class HumanDeleteInfo {
    private String account_name;
    private String true_name;
    public HumanDeleteInfo(){

    }
    public HumanDeleteInfo(String account_name,String true_name){
        this.account_name = account_name;
        this.true_name = true_name;

    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }
}

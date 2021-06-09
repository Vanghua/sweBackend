package Humanresource.trans;

public class HumanChangeInfo {
    private String account_name;
    private String true_name;
    private String account_email;
    private String account_type;
    private String telephone;
    public HumanChangeInfo(){

    }
    public HumanChangeInfo(String account_email,String account_type,String telephone){
        this.account_email = account_email;
        this.account_type = account_type;
        this.telephone = telephone;
    }

    public String getAccount_email() {
        return account_email;
    }

    public void setAccount_email(String account_email) {
        this.account_email = account_email;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

package Humanresource.trans;

public class HumanInfo {
    private String account_name;
    private String true_name;
    private String email;
    private String telephone;
    private String type;
    public HumanInfo(){

    }

    public HumanInfo(String account_name,String true_name,String email,String telephone,String type){
        this.account_name = account_name;
        this.true_name = true_name;
        this.email = email;
        this.telephone = telephone;
        this.type = type;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package FinancialSystem.trans;

public class ExpensesInfo {
    private String invoice_num;
    private String proposer;//报销申请人
    private String proposer_id;//报销申请人id
    private String assessor;//报销审核员
    private String money;
    private String time;
    public ExpensesInfo(){

    }
    public ExpensesInfo(String invoice_num,String proposer,String assessor,String money,String time){
        this.invoice_num = invoice_num;
        this.proposer = proposer;
        this.assessor = assessor;
        this.money = money;
        this.time = time;

    }

    public String getInvoice_num() {
        return invoice_num;
    }

    public void setInvoice_num(String invoice_num) {
        this.invoice_num = invoice_num;
    }

    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getAssessor() {
        return assessor;
    }

    public void setAssessor(String assessor) {
        this.assessor = assessor;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProposer_id() {
        return proposer_id;
    }

    public void setProposer_id(String proposer_id) {
        this.proposer_id = proposer_id;
    }
}

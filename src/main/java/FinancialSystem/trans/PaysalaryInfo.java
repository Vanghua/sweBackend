package FinancialSystem.trans;

public class PaysalaryInfo {
    private String type;
    private String time;
    private String name;
    private String worker_id;
    public PaysalaryInfo(){

    }
    public PaysalaryInfo(String type, String time, String name){
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

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }
}

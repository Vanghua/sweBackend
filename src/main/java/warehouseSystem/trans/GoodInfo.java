package warehouseSystem.trans;

public class GoodInfo {
    /*
        货物号
        货物名
        货物数量
        货物三段码信息
        货物种类（与优先状态相匹配）
     */
    private int goodId;
    private String goodName;
    private int goodNum;
    private int priority;
    private String managerId;
    private String orderId;

    public GoodInfo() {
    }

    public GoodInfo(int goodId, String goodName, int goodNum, int priority, String managerId, String orderId) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.goodNum = goodNum;
        this.priority = priority;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

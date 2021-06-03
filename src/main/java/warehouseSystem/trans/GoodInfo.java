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
    private String goodTsegmentcode;
    private int goodType;
    private String managerId;

    public GoodInfo(){}

    public GoodInfo(int goodId, String goodName, int goodNum, String goodTsegmentcode, int goodType) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.goodNum = goodNum;
        this.goodTsegmentcode = goodTsegmentcode;
        this.goodType = goodType;
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

    public String getGoodTsegmentcode() {
        return goodTsegmentcode;
    }

    public void setGoodTsegmentcode(String goodTsegmentcode) {
        this.goodTsegmentcode = goodTsegmentcode;
    }

    public int getGoodType() {
        return goodType;
    }

    public void setGoodType(int goodType) {
        this.goodType = goodType;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}

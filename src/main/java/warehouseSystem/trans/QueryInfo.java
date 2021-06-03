package warehouseSystem.trans;

public class QueryInfo {
    private GoodInfo goodInfo;
    private WarehouseInfo warehouseInfo;
    private int pageNum; // 页码
    private int pageCount; // 一页分割数目

    public QueryInfo() {
    }

    public QueryInfo(GoodInfo goodInfo, WarehouseInfo warehouseInfo, int pageNum, int pageCount) {
        this.goodInfo = goodInfo;
        this.warehouseInfo = warehouseInfo;
        this.pageNum = pageNum;
        this.pageCount = pageCount;
    }

    public GoodInfo getGoodInfo() {
        return goodInfo;
    }

    public void setGoodInfo(GoodInfo goodInfo) {
        this.goodInfo = goodInfo;
    }

    public WarehouseInfo getWarehouseInfo() {
        return warehouseInfo;
    }

    public void setWarehouseInfo(WarehouseInfo warehouseInfo) {
        this.warehouseInfo = warehouseInfo;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}

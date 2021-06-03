package warehouseSystem.trans;

public class ExWarehouseListInfo {
    private int exlistId;
    private int exlistGoodId;
    private String exlistOutTime;
    private int exlistManagerId;

    public ExWarehouseListInfo() {
    }

    public ExWarehouseListInfo(int exlistId, int exlistGoodId, String exlistOutTime, int exlistManagerId) {
        this.exlistId = exlistId;
        this.exlistGoodId = exlistGoodId;
        this.exlistOutTime = exlistOutTime;
        this.exlistManagerId = exlistManagerId;
    }

    public int getExlistId() {
        return exlistId;
    }

    public void setExlistId(int exlistId) {
        this.exlistId = exlistId;
    }

    public int getExlistGoodId() {
        return exlistGoodId;
    }

    public void setExlistGoodId(int exlistGoodId) {
        this.exlistGoodId = exlistGoodId;
    }

    public String getExlistOutTime() {
        return exlistOutTime;
    }

    public void setExlistOutTime(String exlistOutTime) {
        this.exlistOutTime = exlistOutTime;
    }

    public int getExlistManagerId() {
        return exlistManagerId;
    }

    public void setExlistManagerId(int exlistManagerId) {
        this.exlistManagerId = exlistManagerId;
    }
}

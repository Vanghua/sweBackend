package warehouseSystem.trans;

public class ShelfInfo {
    /*
        货架编号、货架容量、货架所属仓库编号、层号
     */
    private String shelfId;
    private int shelfWarehouseId;
    private int shelfStorageNum;
    private String newShelfId;
    private int newShelfStorageNum;

    public ShelfInfo() {
    }

    public ShelfInfo(String shelfId, int shelfWarehouseId, int shelfStorageNum) {
        this.shelfId = shelfId;
        this.shelfWarehouseId = shelfWarehouseId;
        this.shelfStorageNum = shelfStorageNum;
    }

    public String getShelfId() {
        return shelfId;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public int getShelfWarehouseId() {
        return shelfWarehouseId;
    }

    public void setShelfWarehouseId(int shelfWarehouseId) {
        this.shelfWarehouseId = shelfWarehouseId;
    }

    public int getShelfStorageNum() {
        return shelfStorageNum;
    }

    public void setShelfStorageNum(int shelfStorageNum) {
        this.shelfStorageNum = shelfStorageNum;
    }

    public String getNewShelfId() {
        return newShelfId;
    }

    public void setNewShelfId(String newShelfId) {
        this.newShelfId = newShelfId;
    }

    public int getNewShelfStorageNum() {
        return newShelfStorageNum;
    }

    public void setNewShelfStorageNum(int newShelfStorageNum) {
        this.newShelfStorageNum = newShelfStorageNum;
    }
}

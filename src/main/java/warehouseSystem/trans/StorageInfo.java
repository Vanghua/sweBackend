package warehouseSystem.trans;

public class StorageInfo {
    /*
        存储细节编号
        存储仓库编号
        存储货物号
        存储货架号
     */
    private int storageId;
    private int storageWarehouseId;
    private int storageGoodId;
    private int storageShelfId;
    private int storageNum;

    public StorageInfo() {
    }

    public StorageInfo(int storageId, int storageWarehouseId, int storageGoodId, int storageShelfId, int storageNum) {
        this.storageId = storageId;
        this.storageWarehouseId = storageWarehouseId;
        this.storageGoodId = storageGoodId;
        this.storageShelfId = storageShelfId;
        this.storageNum = storageNum;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public int getStorageWarehouseId() {
        return storageWarehouseId;
    }

    public void setStorageWarehouseId(int storageWarehouseId) {
        this.storageWarehouseId = storageWarehouseId;
    }

    public int getStorageGoodId() {
        return storageGoodId;
    }

    public void setStorageGoodId(int storageGoodId) {
        this.storageGoodId = storageGoodId;
    }

    public int getStorageShelfId() {
        return storageShelfId;
    }

    public void setStorageShelfId(int storageShelfId) {
        this.storageShelfId = storageShelfId;
    }

    public int getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(int storageNum) {
        this.storageNum = storageNum;
    }
}

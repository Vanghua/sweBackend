package warehouseSystem.trans;

public class WarehouseListInfo {
    private int listId;
    private int listStorageId;
    private String listWarehouseTime;
    private int listManagerId;

    public WarehouseListInfo() {
    }

    public WarehouseListInfo(int listId, int listStorageId, String listWarehouseTime, int listManagerId) {
        this.listId = listId;
        this.listStorageId = listStorageId;
        this.listWarehouseTime = listWarehouseTime;
        this.listManagerId = listManagerId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getListStorageId() {
        return listStorageId;
    }

    public void setListStorageId(int listStorageId) {
        this.listStorageId = listStorageId;
    }

    public String getListWarehouseTime() {
        return listWarehouseTime;
    }

    public void setListWarehouseTime(String listWarehouseTime) {
        this.listWarehouseTime = listWarehouseTime;
    }

    public int getListManagerId() {
        return listManagerId;
    }

    public void setListManagerId(int listManagerId) {
        this.listManagerId = listManagerId;
    }
}

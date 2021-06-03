package warehouseSystem.trans;

public class WarehouseInfo {
    /*
        仓库编号、仓库类型、仓库容量
        仓库位置、仓库负责人（仓库管理员）
        联系方式、仓库创建时间
    */
    private int warehouseId;
    private int warehouseType;
    private int warehhouseStoragenum;
    private String warehouseAddress;
    private String warehouseManager;
    private String warehouseManagerTel;
    private String warehouseCreationtime;
    private String warehouseLng;
    private String warehouseLat;

    public WarehouseInfo(){}

    public WarehouseInfo(int warehouseId, int warehouseType, int warehhouseStoragenum, String warehouseAddress, String warehouseManager, String warehouseManagerTel, String warehouseCreationtime, String warehouseLng, String warehouseLat) {
        this.warehouseId = warehouseId;
        this.warehouseType = warehouseType;
        this.warehhouseStoragenum = warehhouseStoragenum;
        this.warehouseAddress = warehouseAddress;
        this.warehouseManager = warehouseManager;
        this.warehouseManagerTel = warehouseManagerTel;
        this.warehouseCreationtime = warehouseCreationtime;
        this.warehouseLng = warehouseLng;
        this.warehouseLat = warehouseLat;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(int warehouseType) {
        this.warehouseType = warehouseType;
    }

    public int getWarehhouseStoragenum() {
        return warehhouseStoragenum;
    }

    public void setWarehhouseStoragenum(int warehhouseStoragenum) {
        this.warehhouseStoragenum = warehhouseStoragenum;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getWarehouseManager() {
        return warehouseManager;
    }

    public void setWarehouseManager(String warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    public String getWarehouseManagerTel() {
        return warehouseManagerTel;
    }

    public void setWarehouseManagerTel(String warehouseManagerTel) {
        this.warehouseManagerTel = warehouseManagerTel;
    }

    public String getWarehouseCreationtime() {
        return warehouseCreationtime;
    }

    public void setWarehouseCreationtime(String warehouseCreationtime) {
        this.warehouseCreationtime = warehouseCreationtime;
    }

    public String getWarehouseLng() {
        return warehouseLng;
    }

    public void setWarehouseLng(String warehouseLng) {
        this.warehouseLng = warehouseLng;
    }

    public String getWarehouseLat() {
        return warehouseLat;
    }

    public void setWarehouseLat(String warehouseLat) {
        this.warehouseLat = warehouseLat;
    }
}

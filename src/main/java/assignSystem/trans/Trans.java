package assignSystem.trans;

public class Trans {
    private String warehouseId;
    private String accountName;

    public Trans() {
        this.warehouseId = "";
        this.accountName = "";
    }

    public Trans(String warehouseId, String accountName) {
        this.warehouseId = warehouseId;
        this.accountName = accountName;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

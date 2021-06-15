package assignSystem.trans;

public class Trans {
    private String warehouseId;
    private String accountName;
    private String transWeight;

    public Trans() {
        this.warehouseId = "";
        this.accountName = "";
        this.transWeight = "";
    }

    public Trans(String warehouseId, String accountName, String transWeight) {
        this.warehouseId = warehouseId;
        this.accountName = accountName;
        this.transWeight = transWeight;
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

    public String getTransWeight() {
        return transWeight;
    }

    public void setTransWeight(String transWeight) {
        this.transWeight = transWeight;
    }
}

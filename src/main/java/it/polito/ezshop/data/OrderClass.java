package it.polito.ezshop.data;

public class OrderClass implements Order {

    private Integer orderId;
    private Integer balanceId;
    private String productCode;
    private double pricePerUnit;
    private int quantity;
    private String status;

    public OrderClass(Integer orderId, Integer balanceId, String productCode, double pricePerUnit, int quantity, String status) {
        this.balanceId = balanceId;
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.status = status;
        this.orderId = orderId;
    }

    @Override
    public Integer getBalanceId() {
        return this.balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public String getProductCode() {
        return this.productCode;
    }

    @Override
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Integer getOrderId() {
       return this.orderId;
    }

    @Override
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    
}

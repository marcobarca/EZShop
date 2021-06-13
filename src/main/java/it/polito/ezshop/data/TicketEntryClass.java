package it.polito.ezshop.data;

public class TicketEntryClass implements TicketEntry{

    private String barCode;
    private String productDescription;
    private int amount;
    private double pricePerUnit;
    private double discountRate;


    public TicketEntryClass(String barCode, String productDescription, int amount, double pricePerUnit) {
        this.barCode = barCode;
        this.productDescription = productDescription;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate; 
    }
    
    
}

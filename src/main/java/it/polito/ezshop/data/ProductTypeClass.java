package it.polito.ezshop.data;

public class ProductTypeClass implements ProductType {
    
    private Integer quantity;
    private String location;
    private String note;
    private String productDescription;
    private String barCode;
    private Double pricePerUnit;
    private Integer id;

    public ProductTypeClass(String note, String productDescription, String barCode, Double PricePerUnit, Integer id) {
        this.quantity = 0;
        this.location = "undefined";
        this.note = note;
        this.productDescription = productDescription;
        this.barCode = barCode;
        this.pricePerUnit = PricePerUnit;
        this.id = id;
    }

    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getNote() {
        return this.note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getProductDescription() {
        return this.productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public String getBarCode() {
        return this.barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public Double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
}

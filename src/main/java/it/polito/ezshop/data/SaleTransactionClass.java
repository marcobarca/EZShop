package it.polito.ezshop.data;

import java.util.ArrayList;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

public class SaleTransactionClass implements SaleTransaction {

    //ticket number is the transaction id
    private int ticketNumber;
    private double discountRate;
    private double price;
    private List<TicketEntry> entries;


    public SaleTransactionClass() {
        this.entries = new ArrayList<>();
        this.discountRate = 0;
    }

    @Override
    public Integer getTicketNumber() {
        return ticketNumber;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return this.entries;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    
    
}

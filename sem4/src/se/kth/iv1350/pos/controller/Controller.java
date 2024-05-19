package se.kth.iv1350.pos.controller;


import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.InventorySystemException;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;

import java.util.ArrayList;

import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.SystemLogger;
import se.kth.iv1350.pos.model.LargeReceipt;
import se.kth.iv1350.pos.model.NoSuchItemException;
import se.kth.iv1350.pos.model.Payment;
import se.kth.iv1350.pos.model.Receipt;
import se.kth.iv1350.pos.model.Register;
import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.model.TotalRevenueObserver;

/**
 * This class represents a point of sale's controller. Handles all calls to models.
 */

public class Controller {
    private ExternalInventorySystem inventory;
    private ExternalAccountingSystem accounting;
    private ReceiptPrinter printer;
    private Sale sale;
    private ArrayList<TotalRevenueObserver> revenueObservers;
    private Register register;
    private SystemLogger logger;
    private Receipt receiptType = new LargeReceipt();

    /**
     * Creates a new instance, representing the controller of a point of sale system
     *
     * @param exCreator the {@link ExternalSystemCreator} in charge of creating the external systems
     * @param printer   the {@link ReceiptPrinter} used for printing receipts
     */
    public Controller(ExternalSystemCreator exCreator, ReceiptPrinter printer){
        this.printer = printer;
        this.inventory = exCreator.getExternalInventorySystem();
        this.accounting = exCreator.getExternalAccountingSystem();
        this.register=new Register();
        this.revenueObservers=new ArrayList<>();
    }


    /**
     * Adds the Item identified by the <code>itemIdentifier</code> to the {@link Sale}
     *
     * @param itemIdentifier the String used to identify an item
     * @return a {@link SaleDTO} describing the sale
     * @throws NoSuchItemException when no item with given <code>itemIdentifier</code> exists
     * @throws OperationFailedException when operation is unable to be executed
     */
    public SaleDTO scanItem(String itemIdentifier) throws NoSuchItemException, OperationFailedException {
        SaleDTO saleDTO=null;
        try{
            saleDTO = sale.scanItem(itemIdentifier, inventory);
        } catch (InventorySystemException inventorySystemException){
            logger.logException(inventorySystemException);
            throw new OperationFailedException("Could not add item", inventorySystemException);
        }
        return saleDTO;
    }
    /**
     * 
     * @param logger used to log actions
     */
    public void addLogger(SystemLogger logger){
        this.logger=logger;
    }
    /**
     * 
     * @param observer a {@link TotalRevenueObserver} to observe the revenue of this controller
     */

    public void addRevenueObserver(TotalRevenueObserver observer){
        revenueObservers.add(observer);
    }

    /**
     * 
     * @param receipt the {@link Receipt} type to be used
     */
    public void setReceiptType(Receipt receipt){
        this.receiptType=receipt;
    }

    /**
     * Pays for the {@link Sale} handled by this {@link Controller}
     *
     * @param paidAmount the {@link Amount} used for paying the {@link Sale}
     * @return the {@link Amount} describing the change
     */
    public Amount pay(Amount paidAmount) {
        Payment payment = new Payment(paidAmount);
        Amount change = sale.pay(payment, inventory, accounting);
        sale.printReceipt(change, printer);
        register.updateRegister(paidAmount, change);
        alertRentalObserver(paidAmount.minus(change));
        return change;
    }

    private void alertRentalObserver(Amount revenue){
        for(TotalRevenueObserver observer : revenueObservers){
            observer.newSale(revenue);
        }
    }


    /**
     * Initiates a {@link Sale}
     */
    public void startSale() {
        this.sale = new Sale();
        this.sale.setReceiptType(receiptType);
    }

    /**
     * Ends a {@link Sale}
     *
     * @return the {@link Amount} describing the Sale total
     */
    public Amount endSale() {
        return sale.endSale();
    }

}

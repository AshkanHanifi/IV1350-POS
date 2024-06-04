package se.kth.iv1350.pos.controller;


import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.InventorySystemException;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;

import java.util.ArrayList;

import se.kth.iv1350.pos.integration.DiscountDatabase;
import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.SystemLogger;
import se.kth.iv1350.pos.model.DiscountContainer;
import se.kth.iv1350.pos.model.Payment;
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
    private DiscountDatabase discount;
    private ReceiptPrinter printer;
    private Sale sale;
    private ArrayList<TotalRevenueObserver> revenueObservers;
    private Register register;
    private SystemLogger logger;

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
        this.discount=exCreator.getDiscountDatabase();
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
        return change;
    }

    /**
     * Calculates the discount returns a {@link DiscountContainer} with the discount percentage and new total
     * @param customerID the customerID to be used to find discounts
     * @return a {@link DiscountContainer} containing discount percentage and new total
     */
    public DiscountContainer percentageDiscount(String customerID) {
        DiscountContainer discountContainer = sale.percentageDiscount(discount, customerID);
        return discountContainer;
    }

    /**
     * Calculates the discount returns a {@link DiscountContainer} with the the discount and new total
     * @return a {@link DiscountContainer} containing discount total and new total
     */
    public DiscountContainer discountTotal(){
        return sale.totalDiscount(discount);
    }

    /**
     * Initiates a {@link Sale}
     */
    public void startSale() {
        this.sale = new Sale();
        this.sale.addRevenueObserver(revenueObservers);
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

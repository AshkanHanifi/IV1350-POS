package se.kth.iv1350.pos.controller;


import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.model.Payment;
import se.kth.iv1350.pos.model.Register;
import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.SaleDTO;

/**
 * This class represents a point of sale's controller. Handles all calls to models.
 */

public class Controller {
    private ExternalSystemCreator exCreator;
    private ExternalInventorySystem inventory;
    private ExternalAccountingSystem accounting;
    private ReceiptPrinter printer;
    private Sale sale;
    private Register register;

    /**
     * Creates a new instance, representing the controller of a point of sale system
     *
     * @param exCreator the {@link ExternalSystemCreator} in charge of creating the external systems
     * @param printer   the {@link ReceiptPrinter} used for printing receipts
     */
    public Controller(ExternalSystemCreator exCreator, ReceiptPrinter printer) {
        this.exCreator = exCreator;
        this.printer = printer;
        this.inventory = exCreator.getExternalInventorySystem();
        this.accounting = exCreator.getExternalAccountingSystem();
    }

    /**
     * Adds the Item identified by the <code>itemIdentifier</code> to the {@link Sale}
     *
     * @param itemIdentifier the String used to identify an item
     * @return a {@link SaleDTO} describing the sale
     */
    public SaleDTO scanItem(String itemIdentifier) {
        boolean scanned = sale.previouslyScanned(itemIdentifier);
        SaleDTO saleDTO;
        if (scanned) {
            saleDTO = sale.updateItemQuantity(itemIdentifier);
        } else {
            ItemDTO item = inventory.getItemInfo(itemIdentifier);
            saleDTO = sale.addNewItem(item);
        }
        return saleDTO;
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
        //register.updateRegister(Payment);
        return change;
    }

    /**
     * Initiates a {@link Sale}
     */
    public void startSale() {
        this.sale = new Sale();
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

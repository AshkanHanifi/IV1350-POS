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


public class Controller {
    private ExternalSystemCreator exCreator;
    private ExternalInventorySystem inventory;
    private ExternalAccountingSystem accounting;
    private ReceiptPrinter printer;
    private Sale sale;
    private Register register;

    public Controller(ExternalSystemCreator exCreator, ReceiptPrinter printer) {
        this.exCreator = exCreator;
        this.printer = printer;
        this.inventory = exCreator.getExternalInventorySystem();
        this.accounting= exCreator.getExternalAccountingSystem();
    }

    public SaleDTO scanItem(String itemIdentifier){
        boolean scanned = sale.previouslyScanned(itemIdentifier);
        SaleDTO saleDTO;
        if(scanned){
            saleDTO=sale.updateItemQuantity(itemIdentifier);
        } else {
            ItemDTO item = inventory.getItemInfo(itemIdentifier);
            saleDTO=sale.addNewItem(item);
        }
        return saleDTO;
    }

    public Amount pay(Amount paidAmount){
        Payment payment=new Payment(paidAmount);
        Amount change=sale.pay(payment,inventory,accounting);
        sale.printReceipt(change, printer);
        //register.updateRegister(Payment);
        return change;
    }

    public void startSale(){
        this.sale=new Sale();
    }

    public Amount endSale(){
        return sale.endSale();
    }

}

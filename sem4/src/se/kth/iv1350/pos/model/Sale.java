package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.DiscountDatabase;
import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.util.Amount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents the sale occurring at a point of sale
 */
public class Sale {
    private HashMap<ItemDTO, Integer> items;
    private Amount total = new Amount(0);
    private Amount vatAmount = new Amount(0);
    private boolean closedSale = false;
    private ArrayList<TotalRevenueObserver> revenueObservers;
    private Amount discountTotal=new Amount(0);



    /**
     * Creates a new instance, representing a sale
     */
    public Sale() {
        items = new HashMap<>();
        this.revenueObservers=new ArrayList<>();
    }

    /**
     * 
     * @param discountDatabase the {@link DiscountDatabase} to use for finding discounts
     * @param customerID the customerID to be used to find discounts
     * @return a {@link DiscountContainer} containing discount percentage and new total
     */
    public DiscountContainer percentageDiscount(DiscountDatabase discountDatabase, String customerID){
        Amount discount=discountDatabase.precentageDiscount(customerID, createSaleDTO());
        Amount discountAmount=this.total.scale(discount);
        this.total=this.total.minus(discountAmount);
        this.discountTotal=this.discountTotal.addition(discountAmount);
        return new DiscountContainer(discount, total);
    }

    /**
     * 
     * @param discountDatabase the {@link DiscountDatabase} to use for finding discounts
     * @param customerID the customerID to be used to find discounts
     * @return a {@link DiscountContainer} containing discount amount and new total
     */
    public DiscountContainer totalDiscount(DiscountDatabase discountDatabase){
        Amount discount=discountDatabase.totalDiscount(createSaleDTO());
        this.total=this.total.minus(discount);
        this.discountTotal=this.discountTotal.addition(discount);
        DiscountContainer container=new DiscountContainer(discount, this.total);
        return container;
    }


    /**
     * Adds the Item identified by the <code>itemIdentifier</code> to the {@link Sale}
     *
     * @param itemIdentifier the String used to identify an item
     * @param inventory a referens to a {@link ExternalInventorySystem}
     * @return a {@link SaleDTO} describing the sale
     * @throws NoSuchItemException when no item with given <code>itemIdentifier</code> exists
     */
    public SaleDTO scanItem(String itemIdentifier, ExternalInventorySystem inventory) throws NoSuchItemException{
        boolean scanned = previouslyScanned(itemIdentifier);
        SaleDTO saleDTO;
        if (scanned) {
            saleDTO = updateItemQuantity(itemIdentifier);
        } else {
            ItemDTO item = inventory.getItemInfo(itemIdentifier);
            saleDTO = addNewItem(item);
        }
        return saleDTO;
    }

    private SaleDTO addNewItem(ItemDTO item) {
        items.put(item, 1);
        increaseCost(item);
        return createSaleDTO();
    }

    private SaleDTO updateItemQuantity(String itemIdentifier) {
        ItemDTO wantedItem = findInScannedItems(itemIdentifier);
        int currentQuantity = items.get(wantedItem);
        items.put(wantedItem, ++currentQuantity);
        increaseCost(wantedItem);
        return createSaleDTO();
    }

    /**
     * Pays for the sale
     * @param payment a {@link Payment} describing the paid amount
     * @param inventory a reference to a {@link ExternalInventorySystem}
     * @param accounting a reference to a {@link ExternalAccountingSystem}
     * @return the {@link Amount} to be returned
     */
    public Amount pay(Payment payment, ExternalInventorySystem inventory, ExternalAccountingSystem accounting) {
        SaleDTO saleDTO = createSaleDTO();
        Amount change = payment.calculateChange(saleDTO);
        accounting.logSale(saleDTO, change);
        inventory.updateInventory(saleDTO);
        alertRentalObserver(createSaleDTO());
        return change;
    }

    /**
     * Marks the sale as closed
     *
     * @return the total {@link Amount} of the sale
     */
    public Amount endSale() {
        closedSale = true;
        return total;
    }

    private boolean previouslyScanned(String itemIdentifier) {
        return (null != findInScannedItems(itemIdentifier));
    }

    /**
     * Prints the receipt
     *
     * @param change the {@link Amount} to be returned as change
     * @param printer a reference to a {@link ReceiptPrinter}
     */
    public void printReceipt(Amount change, ReceiptPrinter printer) {
        SaleDTO saleDTO = createSaleDTO();
        Receipt receipt = new Receipt(saleDTO, change, discountTotal);
        printer.PrintReceipt(receipt);
    }

        /**
     * 
     * @param observer a list of {@link TotalRevenueObserver} to observe the revenue of this controller
     */

     public void addRevenueObserver(ArrayList<TotalRevenueObserver> observer){
        revenueObservers.addAll(observer);
    }

    private void alertRentalObserver(SaleDTO saleDTO){
        for(TotalRevenueObserver observer : revenueObservers){
            observer.newSale(saleDTO);
        }
    }

    private ItemDTO findInScannedItems(String itemIdentifier) {
        Iterator<ItemDTO> iterator = items.keySet().iterator();
        ItemDTO currentItem;
        while (iterator.hasNext()) {
            currentItem = iterator.next();
            if (currentItem.getItemIdentifier().equals(itemIdentifier)) {
                return currentItem;
            }
        }
        return null;
    }

    private SaleDTO createSaleDTO() {
        Map<ItemDTO, Integer> immutable = Collections.unmodifiableMap(new HashMap<>(items));
        SaleDTO saleDTO = new SaleDTO(immutable, total, vatAmount, closedSale);
        return saleDTO;
    }

    private void increaseCost(ItemDTO item) {
        Amount itemVAT = item.getVatAmount();
        vatAmount = vatAmount.addition(itemVAT);
        Amount itemTotalCost = item.getTotalAmount();
        total = total.addition(itemTotalCost);
    }
}

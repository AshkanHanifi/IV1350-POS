package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.util.Amount;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents the sale occurring at a point of sale
 */
public class Sale {
    private HashMap<ItemDTO, Integer> items;
    private Amount total = new Amount(0);
    private Amount vatAmount = new Amount(0);
    private boolean closedSale = false;
    private final int SCALE_VAT = 100;

    /**
     * Creates a new instance, representing a sale
     */
    public Sale() {
        items = new HashMap<>();
    }

    /**
     * Adds a new item to the sale
     * @param item an {@link ItemDTO} of the item to be added
     * @return a {@link SaleDTO} of the updated sale
     */
    public SaleDTO addNewItem(ItemDTO item) {
        items.put(item, 1);
        increaseCost(item);
        return createSaleDTO();
    }

    /**
     * Updates the quantity an existing item
     * @param itemIdentifier the identifier of the item to be added
     * @return a {@link SaleDTO} of the updated sale
     */

    public SaleDTO updateItemQuantity(String itemIdentifier) {
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

    /**
     * Checks if item has been scanned
     *
     * @param itemIdentifier identifier of item to be searched
     * @return a <code>boolean</code> describing if the item has been scanned previously
     */
    public boolean previouslyScanned(String itemIdentifier) {
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
        Receipt receipt = new Receipt(saleDTO, change);
        printer.PrintReceipt(receipt);
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
        SaleDTO saleDTO = new SaleDTO(items, total, vatAmount, closedSale);
        return saleDTO;
    }

    private void increaseCost(ItemDTO item) {
        Amount itemVAT = item.getVatAmount();
        vatAmount = vatAmount.addition(itemVAT);
        Amount itemTotalCost = item.getTotalAmount();
        total = total.addition(itemTotalCost);
    }
}

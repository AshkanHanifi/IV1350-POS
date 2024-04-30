package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.util.Amount;

import java.util.HashMap;
import java.util.Iterator;

public class Sale {
    private HashMap<ItemDTO, Integer> items;
    private Amount total=new Amount(0);
    private Amount vatAmount=new Amount(0);
    private boolean closedSale=false;
    private final int SCALE_VAT=100;

    public Sale(){
        items=new HashMap<>();
    }

    public SaleDTO addNewItem(ItemDTO item){
        items.put(item, 1);
        increaseCost(item);
        return createSaleDTO();
    }

    public SaleDTO updateItemQuantity(String itemIdentifier){
        ItemDTO wantedItem=findInScannedItems(itemIdentifier);
        int currentQuantity=items.get(wantedItem);
        items.put(wantedItem, ++currentQuantity);
        increaseCost(wantedItem);
        return createSaleDTO();
    }

    public Amount pay(Payment payment, ExternalInventorySystem inventory, ExternalAccountingSystem accounting){
        SaleDTO saleDTO = createSaleDTO();
        Amount change=payment.calculateChange(saleDTO);
        accounting.logSale(saleDTO, change);
        inventory.updateInventory(saleDTO);
        return change;
    }

    public Amount endSale(){
        closedSale=true;
        return total;
    }

    public boolean previouslyScanned(String itemIdentifier){
        return (null!=findInScannedItems(itemIdentifier));
    }

    public void printReceipt(Amount change, ReceiptPrinter printer){
        SaleDTO saleDTO = createSaleDTO();
        Receipt receipt = new Receipt(saleDTO, change);
        printer.PrintReceipt(receipt);
    }

    private ItemDTO findInScannedItems(String itemIdentifier){
        Iterator<ItemDTO> iterator = items.keySet().iterator();
        ItemDTO currentItem;
        while (iterator.hasNext()){
            currentItem=iterator.next();
            if(currentItem.getItemIdentifier().equals(itemIdentifier)){
                return currentItem;
            }
        }
        return null;
    }

    private SaleDTO createSaleDTO(){
        SaleDTO saleDTO = new SaleDTO(items, total, vatAmount, closedSale);
        return saleDTO;
    }
    private void increaseCost(ItemDTO item){
        Amount itemVAT=calculateVAT(item);
        vatAmount=vatAmount.addition(itemVAT);
        Amount itemTotalCost=itemVAT.addition(item.getPrice());
        total=total.addition(itemTotalCost);
    }
    private Amount calculateVAT(ItemDTO item){
        return item.getPrice().scale(item.getVatRate()/SCALE_VAT);
    }

}

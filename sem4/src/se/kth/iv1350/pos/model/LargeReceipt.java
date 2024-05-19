package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.util.Amount;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
/**
 * This class represents a detailed receipt of a closed sale in a point of sale
 */
public class LargeReceipt implements Receipt {
    private String timestamp;
    private SaleDTO saleDTO;
    private Amount change;

    /**
     * Creates a new instance, describes a receipt
     * 
    */
    public LargeReceipt() {
        createTimestamp();
    }

    private void createTimestamp() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime currentTime = LocalDateTime.now();
        this.timestamp = dateTimeFormatter.format(currentTime).toString();
    }

    private void addItemsToReceipt(StringBuilder stringBuilder) {
        Map<ItemDTO, Integer> items = saleDTO.getItems();
        for (ItemDTO item : items.keySet()) {
            int itemAmount = items.get(item);
            String formatted = String.format("%-40s %12d x %-10s %10s %8s\n",
                    item.getName(), itemAmount, item.getTotalAmount(), item.getTotalAmount().scale(itemAmount), Amount.CURRENCY);
            stringBuilder.append(formatted);
        }

    }


    public String createReceiptString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("- - - - - - - - - - - - - - - - - - Begin receipt - - - - - - - - - - - - - - - - - - -\n");
        stringBuilder.append("Time of sale: " + timestamp + "\n\n");
        addItemsToReceipt(stringBuilder);
        stringBuilder.append(String.format("%-66s %10s %8s\n", "Total:", saleDTO.getTotal(), Amount.CURRENCY));
        stringBuilder.append("VAT: " + saleDTO.getVatAmount() + "\n");
        stringBuilder.append(String.format("%-66s %10s %8s\n", "Cash:", saleDTO.getTotal().addition(change), Amount.CURRENCY));
        stringBuilder.append(String.format("%-66s %10s %8s\n", "Change:", change, Amount.CURRENCY));
        stringBuilder.append("- - - - - - - - - - - - - - - - - - End receipt - - - - - - - - - - - - - - - - - - - -\n");
        return stringBuilder.toString();
    }

    @Override
    public void setSale(SaleDTO saleDTO) {
        this.saleDTO=saleDTO;

    }

    @Override
    public void setChange(Amount change) {
        this.change=change;
    }


}

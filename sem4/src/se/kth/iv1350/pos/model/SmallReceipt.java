package se.kth.iv1350.pos.model;


import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.util.Amount;

/**
 * This class represents a less receipt of a closed sale in a point of sale
 */

public class SmallReceipt implements Receipt{
    private SaleDTO saleDTO;
    private Amount change;

    /**
     * Creates a new instance, describes a receipt
     * 
     * @param saleDTO the {@link SaleDTO} of the paid sale
     * @param change the {@link Amount} to be returned as change
     */



    @Override
    public String createReceiptString() {
        StringBuilder strb = new StringBuilder("\nBegin Recipt:\n");
        formatItems(strb);
        strb.append("Total: "+ saleDTO.getTotal()+" "+Amount.CURRENCY +"\n");
        strb.append("VAT: "+ saleDTO.getVatAmount()+" "+Amount.CURRENCY +"\n");
        strb.append("Change: "+ change+" "+Amount.CURRENCY +"\n");
        strb.append("End Recipt\n");
        return strb.toString();
    }

    private void formatItems(StringBuilder strb){
        saleDTO.getItems().forEach((ItemDTO item, Integer amount)->{
            String line = String.format("%-18s x %-3s %-3s %s", item.getName(), amount, item.getTotalAmount().scale(amount), Amount.CURRENCY);
           strb.append(line + "\n"); 
        });
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

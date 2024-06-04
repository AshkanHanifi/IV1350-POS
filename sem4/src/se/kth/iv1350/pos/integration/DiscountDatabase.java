package se.kth.iv1350.pos.integration;

import java.util.Iterator;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;
/**
 * A class modeling the discount database from the program flow
 */
public class DiscountDatabase {

    private static final DiscountDatabase discountDatabase=new DiscountDatabase();
    private PercentageDiscount percentageDiscounter = new CompositePercentageDiscount();

    /*
     * Creates an instance of a <code>DiscountDatabase<code/>
     */
    private DiscountDatabase(){
    }

    /**
     * 
     * @return the single instance of the <code>DiscountDatabase<code/> class
     */
    public static DiscountDatabase getInstance(){
        return discountDatabase;
    }
    /**
     * Calculates the percentage to reduce the sale by 
     * 
     * @param customerID the customerID to be used when calculating discount
     * @param sale the {@link SaleDTO} containing information about the sale to be discounted
     * @return {@link Amount} in percentage to reduce the sale total
     */
    public Amount precentageDiscount(String customerID, SaleDTO sale){
        return percentageDiscounter.percentageDiscount(customerID, sale);
    }

    /**
     * Calculates the total to reduce the sale by
     * 
     * @param sale the {@link SaleDTO} containing information about the sale to be discounted
     * @return {@link Amount} to reduce the sale total
     */
    public Amount totalDiscount(SaleDTO sale){
        if (findItem("abc123", sale)) {
            return new Amount(5);
        }
        return new Amount(0);
    }

    private boolean findItem(String itemIdentifier, SaleDTO sale){
        Iterator<ItemDTO> iterator = sale.getItems().keySet().iterator();
        ItemDTO item;
        while (iterator.hasNext()) {
            item=iterator.next();
            if(item.getItemIdentifier().equals(itemIdentifier)){
                return true;
            }
        }
        return false;
    }
    
}

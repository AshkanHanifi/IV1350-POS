package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

public interface PercentageDiscount {
    
    /**
     * Returns the percentage to reduce a sale by
     * 
     * @param customerID the ID to be used when calculating discounts
     * @param sale the {@link SaleDTO} describing the sale to be discounted
     * @return the {@link Amount} to reduce the sale by in percentage
     */
    Amount percentageDiscount(String customerID, SaleDTO sale);
}

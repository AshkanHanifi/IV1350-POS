package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;
/**
 * An implementation of the {@link PercentageDiscount} giving discount based on customerID
 */
public class CustomerIdDiscount implements PercentageDiscount {
    private final Amount NO_DISCOUNT_FOUND_PERCENTAGE=new Amount(0);
    private final Amount DISCOUNT_PERCENTAGE=new Amount(0.1f);
    
    @Override
    public Amount percentageDiscount(String customerID, SaleDTO sale){
        if (customerID.equals("discount")) {
            return DISCOUNT_PERCENTAGE;
        }
        return NO_DISCOUNT_FOUND_PERCENTAGE;
    };
}

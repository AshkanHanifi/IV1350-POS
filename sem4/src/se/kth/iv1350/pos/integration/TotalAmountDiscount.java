package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

/**
 * An implementation of the {@link} PercentageDiscount giving discount based on the total amount of the sale
 */
public class TotalAmountDiscount implements PercentageDiscount{
    private final Amount NO_DISCOUNT_FOUND_PERCENTAGE=new Amount(0);
    private final Amount DISCOUNT_THRESHOLD=new Amount(200);
    private final Amount DISCOUNT_PERCENTAGE=new Amount(0.05f);



    @Override
    public Amount percentageDiscount(String customerID, SaleDTO sale) {
        if (sale.getTotal().isGreaterThan(DISCOUNT_THRESHOLD)) {
            return DISCOUNT_PERCENTAGE;
        }

        return NO_DISCOUNT_FOUND_PERCENTAGE;
    }
    
}

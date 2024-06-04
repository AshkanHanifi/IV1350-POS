package se.kth.iv1350.pos.integration;

import java.util.ArrayList;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;
/**
 * An implementation of the {@link PercentageDiscount} giving discount based on a composite
 */
public class CompositePercentageDiscount implements PercentageDiscount{

    private ArrayList<PercentageDiscount> discountTypes = new ArrayList<>();

    @Override
    public Amount percentageDiscount(String customerID, SaleDTO sale) {
        Amount totalPercentageDiscount = new Amount(0);
        for(PercentageDiscount discounter : discountTypes){
            totalPercentageDiscount=totalPercentageDiscount.addition(discounter.percentageDiscount(customerID, sale));
        }
        return totalPercentageDiscount;
    }
    
    /**
     * Creates a composite discounter
     */
    public CompositePercentageDiscount(){
        discountTypes.add(new TotalAmountDiscount());
        discountTypes.add(new CustomerIdDiscount());
    }

}

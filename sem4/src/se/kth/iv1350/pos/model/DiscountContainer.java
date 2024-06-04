package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * A class that contains both a discount and new total
 */
public class DiscountContainer {
    private Amount discountAmount;
    private Amount newTotal;
    
    /**
     * Container with discount amount and updated total
     * 
     * @param discountAmount the discount {@link} 
     * @param newTotal the updated total of the sale
     */
    public DiscountContainer(Amount discountAmount, Amount newTotal){
        this.discountAmount=discountAmount;
        this.newTotal=newTotal;
    }

    /**
     * 
     * @return the discount {@link Amount}
     */
    public Amount getDiscountAmount(){
        return this.discountAmount;
    }

    /**
     * 
     * @return the new updated total {@link Amount}
     */
    public Amount getNewTotal(){
        return this.newTotal;
    }
    
}

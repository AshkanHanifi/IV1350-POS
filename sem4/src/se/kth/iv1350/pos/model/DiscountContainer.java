package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

public class DiscountContainer {
    private Amount discountAmount;
    private Amount newTotal;
    
    public DiscountContainer(Amount discountAmount, Amount newTotal){
        this.discountAmount=discountAmount;
        this.newTotal=newTotal;
    }

    public Amount getDiscountAmount(){
        return this.discountAmount;
    }

    public Amount getNewTotal(){
        return this.newTotal;
    }
    
}

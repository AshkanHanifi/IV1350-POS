package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

public class Payment {

    private Amount amount;
    public Payment(Amount paidAmount){
        this.amount=paidAmount;
    }

    Amount calculateChange(SaleDTO saleDTO){
        Amount total=saleDTO.getTotal();
        Amount change = amount.minus(total);
        return change;
    }
}

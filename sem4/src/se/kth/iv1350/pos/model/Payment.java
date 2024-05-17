package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * This class represents the payment of of a sale in a point of sale
 */
public class Payment {

    private Amount amount;

    /**
     * Creates a new instance, representing the payment
     *
     * @param paidAmount the {@link Amount} represented by the new instance
     */
    public Payment(Amount paidAmount) {
        this.amount = paidAmount;
    }

    /**
     * Calculates the change for given {@link Payment} and {@link SaleDTO}
     *
     * @param saleDTO the {@link SaleDTO} describing the sale to be paid
     * @return the {@link Amount} to be returned as change
     */
    Amount calculateChange(SaleDTO saleDTO) {
        Amount total = saleDTO.getTotal();
        Amount change = amount.minus(total);
        return change;
    }
}

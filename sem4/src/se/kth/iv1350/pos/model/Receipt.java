package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * Defines a receipt of a closed sale in a point of sale.
 * Is implemented by class providing a customized receipt
 */
public interface Receipt {
    /**
     * Creates a <code>String</code> representation of the {@link Receipt}
     * @return a <code>String</code> desribing a {@link Sale}
     */
    String createReceiptString();

    /**
     * 
     * @param saleDTO the sale to be turned into a receipt
     */
    void setSale(SaleDTO saleDTO);

    /**
     * 
     * @param change the <code>Amount</code> to returned as change
     */
    void setChange(Amount change);
}

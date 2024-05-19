package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * Defines the operations of observer class observing the revenue of a {@link Controller}
 */
public interface TotalRevenueObserver {

    /**
     * 
     * @param revenue the {@link Amount} describing the net revenue of a completed sale 
     */
    void newSale(Amount revenue);
}

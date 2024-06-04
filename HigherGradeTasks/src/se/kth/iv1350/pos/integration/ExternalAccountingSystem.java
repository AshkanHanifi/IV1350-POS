package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

/**
 * A singleton that represents the external accounting system of a point of sale
 */
public class ExternalAccountingSystem {
    private static final ExternalAccountingSystem externalAccountingSystem = new ExternalAccountingSystem();
    /**
     * Creates a new instance, represents an external accounting system
     */
    private ExternalAccountingSystem() {
    }

    /**
     * 
     * @return the only instance of this singleton
     */
    public static ExternalAccountingSystem getInstance(){
        return externalAccountingSystem;
    }

    /**
     * Logs the sale in a database
     *
     * @param saleDTO the {@link SaleDTO} describing the paid Sale
     * @param change  the {@link Amount} describing the change
     */
    public void logSale(SaleDTO saleDTO, Amount change) {
        System.out.println("Sent sale info to external accounting system.");
    }

}

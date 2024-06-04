package se.kth.iv1350.pos.integration;

/**
 * This class represents a creator for the external systems of a point of sale.
 */
public class ExternalSystemCreator {
    private ExternalAccountingSystem accounting;
    private ExternalInventorySystem inventory;
    private DiscountDatabase discount;

    /**
     * Returns an {@link ExternalAccountingSystem}
     *
     * @return an {@link ExternalAccountingSystem}
     */
    public ExternalAccountingSystem getExternalAccountingSystem() {
        return accounting;
    }

    /**
     * Returns an {@link ExternalInventorySystem}
     *
     * @return an {@link ExternalInventorySystem}
     */
    public ExternalInventorySystem getExternalInventorySystem() {
        return inventory;
    }

    /**
     * Returns an {@link DiscountDatabase}
     * @return a {@link DiscountDatabase}
     */
    public DiscountDatabase getDiscountDatabase(){
        return discount;
    }

    /**
     * Creates a new instance, creates all external systems
     */
    public ExternalSystemCreator() {
        this.accounting = ExternalAccountingSystem.getInstance();
        this.inventory = ExternalInventorySystem.getInstance();
        this.discount= DiscountDatabase.getInstance();
    }
}

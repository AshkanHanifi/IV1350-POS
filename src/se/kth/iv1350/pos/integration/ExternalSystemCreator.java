package se.kth.iv1350.pos.integration;

/**
 * This class represents a creator for the external systems of a point of sale.
 */
public class ExternalSystemCreator {
    private ExternalAccountingSystem accounting;
    private ExternalInventorySystem inventory;

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
     * Creates a new instance, creates all external systems
     */
    public ExternalSystemCreator() {
        this.accounting = new ExternalAccountingSystem();
        this.inventory = new ExternalInventorySystem();
    }
}

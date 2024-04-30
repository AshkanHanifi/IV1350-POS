package se.kth.iv1350.pos.integration;

public class ExternalSystemCreator {
    private  ExternalAccountingSystem accounting;
    private ExternalInventorySystem inventory;

    public ExternalAccountingSystem getExternalAccountingSystem() {
        return accounting;
    }

    public ExternalInventorySystem getExternalInventorySystem() {
        return inventory;
    }

    public ExternalSystemCreator() {
        this.accounting= new ExternalAccountingSystem();
        this.inventory= new ExternalInventorySystem();
    }
}

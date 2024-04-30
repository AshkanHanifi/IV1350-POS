package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

public class ExternalAccountingSystem {
    ExternalAccountingSystem() {
    }

    public void logSale(SaleDTO saleDTO, Amount change){
        System.out.println("Sent sale info to external accounting system.");
    }
}

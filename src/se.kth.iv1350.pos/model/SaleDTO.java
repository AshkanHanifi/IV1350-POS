package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.util.Amount;

import java.util.HashMap;

public class SaleDTO {

    private HashMap<ItemDTO, Integer> items;
    private Amount total;
    private Amount vatAmount;
    private boolean closedSale;

    SaleDTO(HashMap<ItemDTO, Integer> items, Amount total, Amount vatAmount, boolean closedSale) {
        this.items = items;
        this.total = total;
        this.vatAmount = vatAmount;
        this.closedSale = closedSale;
    }

    public Amount getTotal() {
        return total;
    }

    public HashMap<ItemDTO, Integer> getItems() {
        return items;
    }

    public Amount getVatAmount() {
        return vatAmount;
    }

    public boolean isClosedSale() {
        return closedSale;
    }
}

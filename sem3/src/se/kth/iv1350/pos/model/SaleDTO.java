package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.util.Amount;

import java.util.Map;

/**
 * This class represents the data transfer object of a sale
 */
public class SaleDTO {

    private Map<ItemDTO, Integer> items;
    private Amount total;
    private Amount vatAmount;
    private boolean closedSale;

    /**
     * Creates a new instance, describes a sale
     * @param items an immutable <code>Map<ItemDTO, Integer></code> containing all items of a sale
     * @param total the total {@link Amount} of a sale
     * @param vatAmount the VAT-{@link Amount} of a sale
     * @param closedSale a <code>boolean</code> flag describing if a sale is closed or not
     */
    SaleDTO(Map<ItemDTO, Integer> items, Amount total, Amount vatAmount, boolean closedSale) {
        this.items = items;
        this.total = total;
        this.vatAmount = vatAmount;
        this.closedSale = closedSale;
    }

    /**
     * @return the total of the sale, type of {@link Amount}
     */
    public Amount getTotal() {
        return total;
    }

    /**
     * @return the items of the sale, type of <code>Map<ItemDTO, Integer></code>
     */
    public Map<ItemDTO, Integer> getItems() {
        return items;
    }

    /**
     * @return the VAT of the sale, type of {@link Amount}
     */
    public Amount getVatAmount() {
        return vatAmount;
    }

    /**
     * @return the closed/open state of the sale, type of <code>boolean</code>
     */
    public boolean isClosedSale() {
        return closedSale;
    }
}

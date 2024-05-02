package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.util.Amount;

import java.util.Objects;


/**
 * This class represents the data transfer object of an item
 */
public class ItemDTO {
    private float vatRate;
    private Amount price;
    private Amount vatAmount;
    private Amount totalAmount;
    private String itemIdentifier;
    private String name;
    private String itemDescription;
    private final int SCALE_VAT = 100;


    ItemDTO(String itemIdentifier, String name, Amount price, float vatRate, String itemDescription) {
        this.vatRate = vatRate;
        this.price = price;
        this.itemIdentifier = itemIdentifier;
        this.name = name;
        this.itemDescription = itemDescription;
        this.vatAmount = calculateVAT();
        this.totalAmount = calculateTotal();
    }

    /**
     * @return the VAT-rate of the item in percentage, type of <code>float</code>
     */
    public float getVatRate() {
        return vatRate;
    }

    /**
     * @return the VAT-free price, type of {@link Amount}
     */
    public Amount getPrice() {
        return price;
    }

    /**
     * @return the item identifier, type of <code>String</code>
     */
    public String getItemIdentifier() {
        return itemIdentifier;
    }

    /**
     * @return the item name, type of <code>String</code>
     */
    public String getName() {
        return name;
    }

    /**
     * @return the item description, type of <code>String</code>
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * @return the item VAT-amount, type of {@link Amount}
     */
    public Amount getVatAmount() {
        return vatAmount;
    }

    /**
     * @return the VAT-included price, type of {@link Amount}
     */
    public Amount getTotalAmount() {
        return totalAmount;
    }

    private Amount calculateVAT() {
        return price.scale(vatRate / SCALE_VAT);
    }

    private Amount calculateTotal() {
        return calculateVAT().addition(price);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Item ID: " + itemIdentifier + "\n");
        stringBuilder.append("Item name: " + name + "\n");
        stringBuilder.append("Item cost: " + price.addition(price.scale(vatRate / SCALE_VAT)) + " " + Amount.CURRENCY + "\n");
        stringBuilder.append("VAT: " + (int) (vatRate) + "%\n");
        stringBuilder.append("Item description: " + itemDescription + "\n");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemIdentifier);
    }
}

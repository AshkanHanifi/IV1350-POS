package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.util.Amount;

import java.util.Objects;
import java.util.Stack;

public class ItemDTO {
    private float vatRate;
    private Amount price;
    private String itemIdentifier;
    private String name;
    private String itemDescription;
    private final int SCALE_VAT=100;


    ItemDTO(String itemIdentifier, String name, Amount price, float vatRate, String itemDescription) {
        this.vatRate = vatRate;
        this.price = price;
        this.itemIdentifier = itemIdentifier;
        this.name = name;
        this.itemDescription = itemDescription;
    }


    public float getVatRate() {
        return vatRate;
    }

    public Amount getPrice() {
        return price;
    }

    public String getItemIdentifier() {
        return itemIdentifier;
    }

    public String getName() {
        return name;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Item ID: " + itemIdentifier + "\n");
        stringBuilder.append("Item name: " + name + "\n");
        stringBuilder.append("Item cost: " + price.addition(price.scale(vatRate/SCALE_VAT)) + "\n");
        stringBuilder.append("VAT: " + (int)(vatRate) + "%\n");
        stringBuilder.append("Item description: " + itemDescription + "\n");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode(){
        return Objects.hash(itemIdentifier);
    }
}

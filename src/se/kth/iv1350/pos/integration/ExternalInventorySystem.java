package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This class represents the external inventory system of a point of sale
 */
public class ExternalInventorySystem {
    private ArrayList<ItemDTO> database = new ArrayList<>();

    ExternalInventorySystem() {
        setupItems();
    }


    /**
     * Returns an {@link ItemDTO} if present in database
     *
     * @param itemIdentifier String used to identify item
     * @return an {@link ItemDTO} of the searched item
     */
    public ItemDTO getItemInfo(String itemIdentifier) {
        ItemDTO wantedItem = findItem(itemIdentifier);
        return wantedItem;
    }

    /**
     * Updates the inventory system
     *
     * @param saleDTO a {@link SaleDTO} describing the current sale
     */
    public void updateInventory(SaleDTO saleDTO) {
        HashMap<ItemDTO, Integer> items = saleDTO.getItems();
        for (ItemDTO item : items.keySet()) {
            System.out.println("Told external inventory system to decrease inventory quantity of item " +
                    item.getItemIdentifier() + " by " + items.get(item) + " units");
        }
    }

    public void addItem(String itemString) {
                String item = itemString.replace(":", ".");
                String[] args = item.split(";");
                database.add(new ItemDTO(args[0], args[1], new Amount(Float.valueOf(args[2])), Float.valueOf(args[3]), args[4]));
        }

    private ItemDTO findItem(String itemIdentifier) {
        for (ItemDTO item : database) {
            if (itemIdentifier.equals(item.getItemIdentifier())) {
                return item;
            }
        }
        return null;

    }

    private void setupItems(){
        addItem("abc123;BigWheel Oatmeal;28:208;6;BigWheel Oatmeal 500 ml, whole grain oats, high fiber, gluten free");
        addItem("def456;YouGoGo Blueberry;14:90;6;YouGoGo Blueberry 240 g, low sugar youghurt, blueberry flavour");
        addItem("q1;Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
    }
}

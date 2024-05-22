package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;
import java.util.ArrayList;
import java.util.Map;

/**
 * A singleton that represents the external inventory system of a point of sale
 */
public class ExternalInventorySystem {
private static final ExternalInventorySystem externalInventorySystem = new ExternalInventorySystem();

    private ArrayList<ItemDTO> database = new ArrayList<>();

    /**
     * 
     * @return the instance of this singleton
     */
    public static ExternalInventorySystem getInstance(){
        return externalInventorySystem;
    }

    /**
     * Creates a new instance, represents an external inventory system
     */
    private ExternalInventorySystem() {
        setupItems();
    }

    /**
     * Returns an {@link ItemDTO} if present in database
     *
     * @param itemIdentifier String used to identify item
     * @return an {@link ItemDTO} of the searched item
     * @throws NoSuchItemException when item not found
     * @throws InventorySystemException when connection to server fails
     */
    public ItemDTO getItemInfo(String itemIdentifier) throws NoSuchItemException{
        if(itemIdentifier.equals("error")){
            throw new InventorySystemException("Server connection failure");
        }
        ItemDTO wantedItem = findItem(itemIdentifier);
        if(wantedItem==null){
            throw new NoSuchItemException(itemIdentifier);
        }
        return wantedItem;
    }

    /**
     * Updates the inventory system
     *
     * @param saleDTO a {@link SaleDTO} describing the current sale
     */
    public void updateInventory(SaleDTO saleDTO) {
        Map<ItemDTO, Integer> items = saleDTO.getItems();
        for (ItemDTO item : items.keySet()) {
            System.out.println("Told external inventory system to decrease inventory quantity of item " +
                    item.getItemIdentifier() + " by " + items.get(item) + " units");
        }
    }

    /**
     * Adds an item to the inventory
     * 
     * @param itemString a string describing an item in following format:
     * itemIdentifier;itemName;itemCost(decimal seprated by ":");VAT(integer);itemDescription
     */
    public void addItem(String itemString) {
        String item = itemString.replace(":", ".");
        String[] args = item.split(";");
        database.add(
                new ItemDTO(args[0], args[1], new Amount(Float.valueOf(args[2])), Float.valueOf(args[3]), args[4]));
    }

    private ItemDTO findItem(String itemIdentifier) {
        for (ItemDTO item : database) {
            if (itemIdentifier.equals(item.getItemIdentifier())) {
                return item;
            }
        }
        return null;
    }

    private void setupItems() {
        addItem("abc123;BigWheel Oatmeal;20:00;25;BigWheel Oatmeal 500 ml, whole grain oats, high fiber, gluten free");
        addItem("def456;YouGoGo Blueberry;10:00;25;YouGoGo Blueberry 240 g, low sugar youghurt, blueberry flavour");
        addItem("q1;Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
    }
}

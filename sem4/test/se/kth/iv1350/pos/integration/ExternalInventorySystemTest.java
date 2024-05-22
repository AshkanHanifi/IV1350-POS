package se.kth.iv1350.pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExternalInventorySystemTest {
    private ExternalInventorySystem inventorySystem;
    private String itemIdentifier;
    @BeforeEach
    void setUp() {
        itemIdentifier="hij789";
        inventorySystem=ExternalInventorySystem.getInstance();
        inventorySystem.addItem(itemIdentifier+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
    }

    @AfterEach
    void tearDown() {
        inventorySystem=null;
    }

    @Test
    void testGetItemInfoWhenItemExists() {
        String expResult=itemIdentifier;
        ItemDTO searchedItem=null;
        try{
        searchedItem=inventorySystem.getItemInfo(itemIdentifier);
        } catch (Exception e){
            fail("Exception thrown");
            e.printStackTrace();
        }
        assertEquals(expResult, searchedItem.getItemIdentifier(),
                "wrong or no item was found");
    }

    @Test
    void testGetItemInfoWhenItemDoesntExist() {
        String searchedItemIdentifier="";
        try {
            @SuppressWarnings("unused")
            ItemDTO searchedItem=inventorySystem.getItemInfo(searchedItemIdentifier);
            fail("Exception not thrown");
        } catch (NoSuchItemException e) {
            assertTrue(e.getMessage().contains("Unable to find item"), 
            "Error doesn't contain correct message");
        } catch (Exception e){
            fail("Wrong exception thrown");
        }
    }
}
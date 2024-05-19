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
        ItemDTO searchedItem=inventorySystem.getItemInfo(itemIdentifier);
        assertEquals(expResult, searchedItem.getItemIdentifier(),
                "wrong or no item was found");
    }

    @Test
    void testGetItemInfoWhenItemDoesntExist() {
        String searchedItemIdentifier="";
        String expResult=null;
        ItemDTO searchedItem=inventorySystem.getItemInfo(searchedItemIdentifier);
        assertEquals(expResult, searchedItem,
                "wrong or no item was found");
    }
}
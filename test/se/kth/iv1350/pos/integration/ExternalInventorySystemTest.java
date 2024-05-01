package se.kth.iv1350.pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExternalInventorySystemTest {
    private ExternalInventorySystem inventorySystem;
    @BeforeEach
    void setUp() {
        inventorySystem=new ExternalInventorySystem();
    }

    @AfterEach
    void tearDown() {
        inventorySystem=null;
    }

    @Test
    void testGetItemInfoWhenItemExists() {
        String searchedItemIdentifier="def456";
        String expResult=searchedItemIdentifier;
        ItemDTO searchedItem=inventorySystem.getItemInfo(searchedItemIdentifier);
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
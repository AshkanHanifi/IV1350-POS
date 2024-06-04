package se.kth.iv1350.pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.SaleDTO;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ExternalInventorySystemTest {
    private ExternalInventorySystem inventorySystem;
    private String itemIdentifier;
    private String itemIdentifierSecond;

    ByteArrayOutputStream outStream;
    PrintStream originalSys;
    ExternalInventorySystem inventory;

    @BeforeEach
    void setUp() {
        originalSys = System.out;
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        itemIdentifier="hij789";
        itemIdentifierSecond="hij7a1";

        inventorySystem=ExternalInventorySystem.getInstance();
        inventorySystem.addItem(itemIdentifier+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
        inventorySystem.addItem(itemIdentifierSecond+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");

    }

    @AfterEach
    void tearDown() {
        inventorySystem=null;
        outStream = null;
        System.setOut(originalSys);

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

    @Test
    void testUpdateInventoryOneItem(){
        Sale sale = new Sale();
        SaleDTO saleDTO=null;
        try {
            saleDTO=sale.scanItem(itemIdentifier, inventorySystem);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        inventorySystem.updateInventory(saleDTO);
        String expResult = "Told external inventory system to decrease inventory quantity of item " + itemIdentifier + " by 1 units";
        assertTrue(outStream.toString().contains(expResult), "Inventory doesn't correctly inform of update");
    }

    @Test
    void testUpdateInventoryTwoOfOneItem(){
        Sale sale = new Sale();
        SaleDTO saleDTO=null;
        try {
            saleDTO=sale.scanItem(itemIdentifier, inventorySystem);
            saleDTO=sale.scanItem(itemIdentifier, inventorySystem);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        inventorySystem.updateInventory(saleDTO);
        String expResult = "Told external inventory system to decrease inventory quantity of item " + itemIdentifier + " by 2 units";
        assertTrue(outStream.toString().contains(expResult), "Inventory doesn't correctly inform of update");
    }

    @Test
    void testUpdateInventoryTwoDifferentItems(){
        Sale sale = new Sale();
        SaleDTO saleDTO=null;
        try {
            sale.scanItem(itemIdentifier, inventorySystem);
            saleDTO=sale.scanItem(itemIdentifierSecond, inventorySystem);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }

        inventorySystem.updateInventory(saleDTO);
        String expResult = "Told external inventory system to decrease inventory quantity of item " + itemIdentifier + " by 1 units\n";
        String secondExpResult = "Told external inventory system to decrease inventory quantity of item " + itemIdentifierSecond + " by 1 units";
        System.out.println(secondExpResult);

        assertTrue(outStream.toString().contains(expResult), "Inventory doesn't correctly inform of update");
        assertTrue(outStream.toString().contains(secondExpResult), "Inventory doesn't correctly inform of update");

    }
}
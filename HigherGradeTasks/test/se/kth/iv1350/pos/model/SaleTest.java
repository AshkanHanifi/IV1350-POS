package se.kth.iv1350.pos.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.InventorySystemException;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.util.Amount;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SaleTest {
    private ExternalInventorySystem inventory;
    private ExternalAccountingSystem accountingSystem;
    private Sale saleWith1ItemWithTotal5;
    private Sale saleWithNoItems;
    private String itemIdentifier;
    @BeforeEach
    void setUp() throws NoSuchItemException {
        itemIdentifier="hij789";
        ExternalSystemCreator externalSystemCreator=new ExternalSystemCreator();
        inventory=externalSystemCreator.getExternalInventorySystem();
        inventory.addItem(itemIdentifier+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
        accountingSystem= externalSystemCreator.getExternalAccountingSystem();
        saleWith1ItemWithTotal5=new Sale();
        saleWith1ItemWithTotal5.scanItem(itemIdentifier, inventory);
        saleWithNoItems=new Sale();
    }

    @AfterEach
    void tearDown() {
        inventory=null;
        saleWith1ItemWithTotal5=null;
        saleWithNoItems=null;
    }

    @Test
    @SuppressWarnings("unused")
    public void testAddingItemThatDoesNotExist(){
        try {
            SaleDTO updatedSale = saleWithNoItems.scanItem("nonExistingItem", inventory);
            fail("Could add non-existant item");
        } catch (NoSuchItemException e) {
            assertTrue(e.getMessage().contains("nonExistingItem"),
                       "Error does not contain item identifier: "
                       + e.getMessage());
        } catch (InventorySystemException e){
            fail("Wrong exception thrown");
            e.printStackTrace();
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void testAddingItemWithInventoryError(){
        try {
            SaleDTO updatedSale = saleWithNoItems.scanItem("error", inventory);
            fail("Could add non-existant item");
        } catch (NoSuchItemException e) {
            fail("Wrong exception thrown");
            e.printStackTrace();
        } catch (InventorySystemException e){
            assertTrue(e.getMessage().contains("connection"),
            "Error does not contain correct message: "
            + e.getMessage());
        }
    }

    @Test
    public void testAddingItemToEmptySale(){
        ItemDTO item=null;
        SaleDTO updatedSale=null;
        try {
            item = inventory.getItemInfo(itemIdentifier);
            updatedSale = saleWithNoItems.scanItem(itemIdentifier, inventory);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        int expResult=1;
        int result=findAmountOfItem(item, updatedSale);
        assertEquals(expResult, result, "Quantity of item incorrect");
    }

    @Test
    public void testAddingAlreadyExistingItem(){
        ItemDTO item = null;
        SaleDTO updatedSale=null;
        try {
            item = inventory.getItemInfo(itemIdentifier);
            updatedSale = saleWith1ItemWithTotal5.scanItem(itemIdentifier, inventory);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        int expResult=2;
        int result=findAmountOfItem(item, updatedSale);
        assertEquals(expResult, result, "Quantity of item incorrect");
    }

    @Test
    public void testAddingIncrementingTotal(){
        SaleDTO updatedSale=null;
        try {
            updatedSale = saleWithNoItems.scanItem(itemIdentifier, inventory);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        Amount expResult= new Amount(5);
        Amount result=updatedSale.getTotal();
        assertEquals(expResult, result, "Total of sale incorrect");
    }
    @Test
    public void testAddingIncrementingVAT(){
        SaleDTO updatedSale=null;
        try {
            updatedSale = saleWithNoItems.scanItem(itemIdentifier, inventory);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        Amount expResult= new Amount(1);
        Amount result=updatedSale.getVatAmount();
        assertEquals(expResult, result, "Total of sale incorrect");
    }

    @Test
    public void testPayWithExactAmount(){
        Amount paidAmount=new Amount(5);
        Payment payment=new Payment(paidAmount);
        Amount expResult=new Amount(0);
        Amount result=saleWith1ItemWithTotal5.pay(payment,inventory, accountingSystem);
        assertEquals(expResult, result, "Change amount is incorrect");
    }

    @Test
    public void testPayWithOneMoreAmount(){
        Amount paidAmount=new Amount(6);
        Payment payment=new Payment(paidAmount);
        Amount expResult=new Amount(1);
        Amount result=saleWith1ItemWithTotal5.pay(payment,inventory, accountingSystem);
        assertEquals(expResult, result, "Change amount is incorrect");
    }

    @Test
    public void testEndingSaleReturnsCorrectAmount(){
        Amount expResult=new Amount(5);
        Amount result=saleWith1ItemWithTotal5.endSale();
        assertEquals(expResult, result, "Incorrect sale total");
    }

    @Test
    public void testEndingSaleReturnsCorrectAmountWhenZero(){
        Amount expResult=new Amount(0);
        Amount result=saleWithNoItems.endSale();
        assertEquals(expResult, result, "Incorrect sale total");
    }
    private int findAmountOfItem(ItemDTO item, SaleDTO sale){
        Map<ItemDTO, Integer> items=sale.getItems();
        return items.get(item);
    }
}
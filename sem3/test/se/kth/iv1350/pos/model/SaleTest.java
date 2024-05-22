package se.kth.iv1350.pos.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.ItemDTO;
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
    void setUp() {
        itemIdentifier="hij789";
        ExternalSystemCreator externalSystemCreator=new ExternalSystemCreator();
        inventory=externalSystemCreator.getExternalInventorySystem();
        inventory.addItem(itemIdentifier+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
        accountingSystem= externalSystemCreator.getExternalAccountingSystem();
        saleWith1ItemWithTotal5=new Sale();
        ItemDTO item = inventory.getItemInfo(itemIdentifier);
        saleWith1ItemWithTotal5.addNewItem(item);
        saleWithNoItems=new Sale();
    }

    @AfterEach
    void tearDown() {
        inventory=null;
        saleWith1ItemWithTotal5=null;
        saleWithNoItems=null;
    }

    @Test
    public void testAddingItemToEmptySale(){
        ItemDTO item = inventory.getItemInfo(itemIdentifier);
        SaleDTO updatedSale=saleWithNoItems.addNewItem(item);
        int expResult=1;
        int result=findAmountOfItem(item, updatedSale);
        assertEquals(expResult, result, "Quantity of item incorrect");
    }

    @Test
    public void testAddingAlreadyExistingItem(){
        ItemDTO item = inventory.getItemInfo(itemIdentifier);
        SaleDTO updatedSale=saleWith1ItemWithTotal5.updateItemQuantity(itemIdentifier);
        int expResult=2;
        int result=findAmountOfItem(item, updatedSale);
        assertEquals(expResult, result, "Quantity of item incorrect");
    }

    @Test
    public void testAddingIncrementingTotal(){
        ItemDTO item = inventory.getItemInfo(itemIdentifier);
        SaleDTO updatedSale=saleWithNoItems.addNewItem(item);
        Amount expResult= new Amount(5);
        Amount result=updatedSale.getTotal();
        assertEquals(expResult, result, "Total of sale incorrect");
    }
    @Test
    public void testAddingIncrementingVAT(){
        ItemDTO item = inventory.getItemInfo(itemIdentifier);
        SaleDTO updatedSale=saleWithNoItems.addNewItem(item);
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
    public void testIfPreviouslyScannedWhenScanned(){
        boolean expResult=true;
        boolean result=saleWith1ItemWithTotal5.previouslyScanned(itemIdentifier);
        assertEquals(expResult, result, "Item has been previously scanned");
    }

    @Test
    public void testIfPreviouslyScannedWhenNotScanned(){
        boolean expResult=false;
        boolean result=saleWithNoItems.previouslyScanned(itemIdentifier);
        assertEquals(expResult, result, "Item has not been previously scanned");
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
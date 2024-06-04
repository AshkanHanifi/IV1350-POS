package se.kth.iv1350.pos.view;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.util.Amount;

class ViewTest {

    ByteArrayOutputStream outStream;
    PrintStream originalSys;
    ExternalInventorySystem inventory;

    @BeforeEach
    void setUp() throws IOException {
        originalSys = System.out;
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        ExternalSystemCreator exCreator = new ExternalSystemCreator();
        inventory=exCreator.getExternalInventorySystem();
        ReceiptPrinter printer = new ReceiptPrinter();
        Controller controller = new Controller(exCreator, printer);
        new View(controller);
    }

    @AfterEach
    public void tearDown() {
        outStream = null;
        System.setOut(originalSys);
    }

    @Test
    void testFakeExecutionSuccesfullyAddingItem() {
        String expResult="Add 1 item with item id abc123";
        assertTrue(outStream.toString().contains(expResult), "View doesn't correctly inform addition of item");
    }

    @Test
    void testFakeExecutionSuccesfullyAddingItemInformation() {
        ItemDTO item=null;
        try {
            item=inventory.getItemInfo("abc123");
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        String expResult=itemDTOToString(item);
        assertTrue(outStream.toString().contains(expResult), "View doesn't correctly inform addition of item");
    }

    @Test
    void testFakeExecutionUnsuccesfullyAddingItem() {
        String expResult="Add 1 item with item id q1d\n" +
        "No item found with following identifier: q1d";
        assertTrue(outStream.toString().contains(expResult), "View doesn't correctly inform addition of non existing item");
    }

    @Test
    void testFakeExecutionIncrementingTotal() {
        ItemDTO firstItem=null;
        ItemDTO secondItem=null;
        ItemDTO thirdItem=null;

        try {
            firstItem=inventory.getItemInfo("abc123");
            secondItem=inventory.getItemInfo("abc123");
            thirdItem=inventory.getItemInfo("q1");
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        String firstExpResult="Total cost (incl VAT): " + firstItem.getTotalAmount() + " " +Amount.CURRENCY;
        String secondExpResult="Total cost (incl VAT): " + secondItem.getTotalAmount().addition(firstItem.getTotalAmount()) +" "+ Amount.CURRENCY;
        String thirdExpResult="Total cost (incl VAT): "+  thirdItem.getTotalAmount().addition(secondItem.getTotalAmount().addition(firstItem.getTotalAmount())) + " " +Amount.CURRENCY;


        assertTrue(outStream.toString().contains(firstExpResult), "View doesn't correctly inform addition of incremented total");
        assertTrue(outStream.toString().contains(secondExpResult), "View doesn't correctly inform addition of incremented total");
        assertTrue(outStream.toString().contains(thirdExpResult), "View doesn't correctly inform addition of incremented total");
    }

    @Test
    void testFakeExecutionIncrementingVAT() {
        ItemDTO firstItem=null;
        ItemDTO secondItem=null;
        ItemDTO thirdItem=null;

        try {
            firstItem=inventory.getItemInfo("abc123");
            secondItem=inventory.getItemInfo("abc123");
            thirdItem=inventory.getItemInfo("q1");
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        String firstExpResult="Total VAT: " + firstItem.getVatAmount() + " " +Amount.CURRENCY;
        String secondExpResult="Total VAT: " + secondItem.getVatAmount().addition(firstItem.getVatAmount()) +" "+ Amount.CURRENCY;
        String thirdExpResult="Total VAT: "+  thirdItem.getVatAmount().addition(secondItem.getVatAmount().addition(firstItem.getVatAmount())) + " " +Amount.CURRENCY;


        assertTrue(outStream.toString().contains(firstExpResult), "View doesn't correctly inform addition of incremented total");
        assertTrue(outStream.toString().contains(secondExpResult), "View doesn't correctly inform addition of incremented total");
        assertTrue(outStream.toString().contains(thirdExpResult), "View doesn't correctly inform addition of incremented total");

    }

    @Test
    void testFakeExecutionInformingExternalInventoryUpdates(){
        String expResult="Told external inventory system to decrease inventory quantity";
        assertTrue(outStream.toString().contains(expResult), "View doesn't correctly inform of external inventory update");
    }

    @Test
    void testFakeExecutionInformingExternalAccountingUpdates(){
        String expResult="Sent sale info to external accounting system.";
        assertTrue(outStream.toString().contains(expResult), "View doesn't correctly inform of external accounting update");
    }

        private String itemDTOToString(ItemDTO item){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Item ID: " + item.getItemIdentifier() + "\n");
        stringBuilder.append("Item name: " + item.getName() + "\n");
        stringBuilder.append("Item cost: " + item.getPrice().addition(item.getPrice().scale(item.getVatRate() / item.getScaleVat())) + " " + Amount.CURRENCY + "\n");
        stringBuilder.append("VAT: " + (int) (item.getVatRate()) + "%\n");
        stringBuilder.append("Item description: " + item.getItemDescription() + "\n");
        return stringBuilder.toString();
    }
}

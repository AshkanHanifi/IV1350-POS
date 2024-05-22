package se.kth.iv1350.pos.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Controller controllerWithEmptySale;
    private Controller controllerWithSaleTotalling5;
    private ItemDTO itemCosting5;
    private String itemIdentifier;
    @BeforeEach
    void setUp() {
        ExternalSystemCreator externalSystemCreator=new ExternalSystemCreator();
        ExternalInventorySystem inventory = externalSystemCreator.getExternalInventorySystem();
        itemIdentifier="hij789";
        inventory.addItem(itemIdentifier+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
        itemCosting5=inventory.getItemInfo(itemIdentifier);
        ReceiptPrinter printer=new ReceiptPrinter();
        controllerWithEmptySale=new Controller(externalSystemCreator, printer);
        controllerWithEmptySale.startSale();
        controllerWithSaleTotalling5=new Controller(externalSystemCreator,printer);
        controllerWithSaleTotalling5.startSale();
        controllerWithSaleTotalling5.scanItem(itemIdentifier);
    }

    @AfterEach
    void tearDown() {
        controllerWithEmptySale=null;
        controllerWithSaleTotalling5=null;
    }

    @Test
    void testScanningNotScannedItem() {
        SaleDTO updatedSale=controllerWithEmptySale.scanItem(itemIdentifier);
        int expResult=1;
        int result=findAmountOfItem(itemCosting5, updatedSale);
        assertEquals(expResult, result, "Item quantity not increased correctly");
    }

    @Test
    void testScanningAlreadyScannedItem() {
        SaleDTO updatedSale=controllerWithSaleTotalling5.scanItem(itemIdentifier);
        int expResult=2;
        int result=findAmountOfItem(itemCosting5, updatedSale);
        assertEquals(expResult, result, "Item quantity not increased correctly");
    }

    @Test
    void testScanningIncrementingTotal() {
        SaleDTO updatedSale=controllerWithEmptySale.scanItem(itemIdentifier);
        Amount expResult=new Amount(5);
        Amount result=updatedSale.getTotal();
        assertEquals(expResult, result, "Sale total not increased correctly");
    }

    @Test
    void testScanningIncrementingVAT() {
        SaleDTO updatedSale=controllerWithEmptySale.scanItem(itemIdentifier);
        Amount expResult=new Amount(1);
        Amount result=updatedSale.getVatAmount();
        assertEquals(expResult, result, "Sale total not increased correctly");
    }

    @Test
    void testPayReturningZeroWhenPaymentEqualsTotal(){
        Amount paidAmount=new Amount(5);
        Amount expResult=new Amount(0);
        Amount result=controllerWithSaleTotalling5.pay(paidAmount);
        assertEquals(expResult,result, "Incorrect change");
    }
    @Test
    void testPayReturningOneWhenPaymentEqualsTotalPlusOne(){
        Amount paidAmount=new Amount(6);
        Amount expResult=new Amount(1);
        Amount result=controllerWithSaleTotalling5.pay(paidAmount);
        assertEquals(expResult,result, "Incorrect change");
    }

    @Test
    void testEndingSaleReturningCorrectTotal() {
        Amount expResult=new Amount(5);
        Amount result=controllerWithSaleTotalling5.endSale();
        assertEquals(expResult, result, "Sale total is not correct");
    }

    @Test
    void testEndingSaleReturningCorrectTotalWhenZero() {
        Amount expResult=new Amount(0);
        Amount result=controllerWithEmptySale.endSale();
        assertEquals(expResult, result, "Sale total is not correct");
    }

    private int findAmountOfItem(ItemDTO item, SaleDTO sale){
        Map<ItemDTO, Integer> items=sale.getItems();
        return items.get(item);
    }
}
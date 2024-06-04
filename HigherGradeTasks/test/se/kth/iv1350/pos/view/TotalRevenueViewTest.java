package se.kth.iv1350.pos.view;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.model.Payment;
import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.TotalRevenueObserver;
import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.FileLogger;

public class TotalRevenueViewTest {

    ByteArrayOutputStream outStream;
    PrintStream originalSys;
    Sale saleWithTotalOf5;
    Sale saleWithTotalOf10;
    TotalRevenueView observer;
    ExternalAccountingSystem accounting;
    ExternalInventorySystem inventory;


    @BeforeEach
    void setUp() throws IOException, NoSuchItemException {
        originalSys = System.out;
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        String itemIdentifier="hij789";
        observer=new TotalRevenueView(new FileLogger("test-exceptions.txt", false));

        inventory=ExternalInventorySystem.getInstance();
        accounting=ExternalAccountingSystem.getInstance();
        inventory.addItem(itemIdentifier+";Ice cream;4:00;25;Ice cream 100 g, chocolate flavour, dairy");
        saleWithTotalOf5=new Sale();
        saleWithTotalOf5.scanItem(itemIdentifier, inventory);
        saleWithTotalOf10=new Sale();
        saleWithTotalOf10.scanItem(itemIdentifier, inventory);
        saleWithTotalOf10.scanItem(itemIdentifier, inventory);
    }

    @AfterEach
    void tearDown() {
        outStream = null;
        System.setOut(originalSys);
        saleWithTotalOf10=null;
        saleWithTotalOf5=null;
        observer=null;
    }

    @Test
    void testNewSaleWithOneSale(){
        Amount paidAmount = new Amount(100);
        Payment payment=new Payment(paidAmount);
        saleWithTotalOf5.addRevenueObserver(new ArrayList<TotalRevenueObserver>(Arrays.asList(observer)));
        Amount change=saleWithTotalOf5.pay(payment,inventory,accounting);
        Amount revenue=paidAmount.minus(change);
        String expResult="Current revenue: " + revenue;
        assertTrue(outStream.toString().contains(expResult), "Observer not printing correct revenue");
    }

    @Test
    void testNewSaleWithTwoSales(){
        Amount paidAmount = new Amount(100);
        Amount totalCost=new Amount(15);
        Payment payment=new Payment(paidAmount);
        saleWithTotalOf5.addRevenueObserver(new ArrayList<TotalRevenueObserver>(Arrays.asList(observer)));
        saleWithTotalOf5.pay(payment,inventory,accounting);
        saleWithTotalOf10.addRevenueObserver(new ArrayList<TotalRevenueObserver>(Arrays.asList(observer)));
        saleWithTotalOf10.pay(payment,inventory,accounting);
        String expResult="Current revenue: " + totalCost;
        assertTrue(outStream.toString().contains(expResult), "Observer not printing correct revenue");
    }


    
}

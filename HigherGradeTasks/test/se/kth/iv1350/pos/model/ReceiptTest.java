package se.kth.iv1350.pos.model;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.integration.ExternalAccountingSystem;
import se.kth.iv1350.pos.integration.ExternalInventorySystem;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.util.Amount;

public class ReceiptTest {
    private Sale saleWithOneIncludedItem = new Sale();
    private SaleDTO saleDTOWithOneIncludedItem;

    private String includedItemName;
    private String itemIdentifier;

    private Amount includedVatPrice;
    private Amount includedTotalPrice;
    private ExternalAccountingSystem accounting;
    private ExternalInventorySystem inventory;

    @BeforeEach
    void setUp() throws NoSuchItemException {
        itemIdentifier="hij123";
        includedItemName = "Ice cream";
        includedVatPrice = new Amount(20).scale(.25f);
        includedTotalPrice = new Amount(20).scale(1.25f);

        ExternalSystemCreator externalSystemCreator = new ExternalSystemCreator();
        this.inventory = externalSystemCreator.getExternalInventorySystem();
        this.accounting = externalSystemCreator.getExternalAccountingSystem();
        this.inventory.addItem(itemIdentifier+";Ice cream;20:00;25;Ice cream 100 g, chocolate flavour, dairy");
        saleDTOWithOneIncludedItem = saleWithOneIncludedItem.scanItem("hij123", inventory);
    }

    @AfterEach
    void tearDown() {
        includedItemName = null;
        includedVatPrice = null;
        includedVatPrice = null;
        inventory = null;
        accounting = null;
        saleWithOneIncludedItem = null;
    }

    @Test
    void testCreateReceiptStringForSingleItems(){
        int itemAmount = 1;
        Amount paidAmount = new Amount(100);
        Payment payment = new Payment(paidAmount);
        Amount change = saleWithOneIncludedItem.pay(payment, inventory, accounting);
        Receipt receipt = new Receipt(saleDTOWithOneIncludedItem, change, new Amount(0));
        LocalDateTime purchaseTime = LocalDateTime.now();
        String itemsResult = createReceiptStringForItem(itemAmount, includedItemName, includedTotalPrice);
        String discountResult =createReceiptStringForValueField("Discounts", new Amount(0));
        String totalResult = createReceiptStringForValueField("Total", includedTotalPrice);
        String vatResult =  "VAT: " + includedVatPrice + "\n";
        String cashResult = createReceiptStringForValueField("Cash", paidAmount);
        String changeResult = createReceiptStringForValueField("Change", change);

        String result = receipt.createReceiptString();
        assertTrue(result.contains(itemsResult), "Wrong printout. The receipt does not represent items correctly");
        assertTrue(result.contains(discountResult), "Wrong printout. The receipt does not represent discount correctly");
        assertTrue(result.contains(totalResult), "Wrong printout. The receipt does not represent the total correctly");
        assertTrue(result.contains(vatResult), "Wrong printout. The receipt does not represent the VAT correctly");
        assertTrue(result.contains(cashResult), "Wrong printout. The receipt does not represent the cash correctly");
        assertTrue(result.contains(changeResult), "Wrong printout. The receipt does not represent change correctly");

        assertTrue(result.contains(Integer.toString(purchaseTime.getYear())), "Wrong rental year.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getMonthValue())), "Wrong rental month.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getDayOfMonth())), "Wrong rental day.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getHour())), "Wrong rental hour.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getMinute())), "Wrong rental minute.");
    }

    @Test
    void testCreateReceiptStringForTwoItem() {

        String itemName = "Sandwich";
        Amount vatPrice = new Amount(40).scale(.25f);
        Amount totalPrice = new Amount(40).scale(1.25f);
        int itemAmount = 1;

        Amount finalTotal = includedTotalPrice.addition(totalPrice);
        Amount finalTotalVat = includedVatPrice.addition(vatPrice);
        inventory.addItem("hij456;Sandwich;40:00;25;Sandwich, ham, egg");

        Amount paidAmount = new Amount(100);
        SaleDTO saleDTO=null;
        try {
            saleDTO = saleWithOneIncludedItem.scanItem("hij456", inventory);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        Payment payment = new Payment(paidAmount);
        Amount change = saleWithOneIncludedItem.pay(payment, inventory, accounting);
        Receipt receipt = new Receipt(saleDTO, change, new Amount(0));
        LocalDateTime purchaseTime = LocalDateTime.now();
        String itemsResult = createReceiptStringForItem(itemAmount, includedItemName, includedTotalPrice);
        itemsResult=itemsResult+createReceiptStringForItem(itemAmount, itemName, totalPrice);
        String discountResult =createReceiptStringForValueField("Discounts", new Amount(0));
        String totalResult = createReceiptStringForValueField("Total", finalTotal);
        String vatResult =  "VAT: " + finalTotalVat + "\n";
        String cashResult = createReceiptStringForValueField("Cash", paidAmount);
        String changeResult = createReceiptStringForValueField("Change", change);

        String result = receipt.createReceiptString();
        assertTrue(result.contains(itemsResult), "Wrong printout. The receipt does not represent items correctly");
        assertTrue(result.contains(discountResult), "Wrong printout. The receipt does not represent discount correctly");
        assertTrue(result.contains(totalResult), "Wrong printout. The receipt does not represent the total correctly");
        assertTrue(result.contains(vatResult), "Wrong printout. The receipt does not represent the VAT correctly");
        assertTrue(result.contains(cashResult), "Wrong printout. The receipt does not represent the cash correctly");
        assertTrue(result.contains(changeResult), "Wrong printout. The receipt does not represent change correctly");

        assertTrue(result.contains(Integer.toString(purchaseTime.getYear())), "Wrong rental year.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getMonthValue())), "Wrong rental month.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getDayOfMonth())), "Wrong rental day.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getHour())), "Wrong rental hour.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getMinute())), "Wrong rental minute.");
    }

    @Test
    void testCreateReceiptStringForMultipleOfOneItem(){
        int itemAmount = 2;

        Amount finalTotal = includedTotalPrice.scale(itemAmount);
        Amount finalTotalVat = includedVatPrice.scale(itemAmount);

        Amount paidAmount = new Amount(100);
        SaleDTO saleDTO=null;
        try {
            saleDTO = saleWithOneIncludedItem.scanItem("hij123", inventory);
        } catch (NoSuchItemException e) {
            fail("Got exception");
            e.printStackTrace();
        }
        Payment payment = new Payment(paidAmount);
        Amount change = saleWithOneIncludedItem.pay(payment, inventory, accounting);
        Receipt receipt = new Receipt(saleDTO, change, new Amount(0));
        LocalDateTime purchaseTime = LocalDateTime.now();

        String itemsResult = createReceiptStringForItem(itemAmount, includedItemName, includedTotalPrice);
        String discountResult =createReceiptStringForValueField("Discounts", new Amount(0));
        String totalResult = createReceiptStringForValueField("Total", finalTotal);
        String vatResult =  "VAT: " + finalTotalVat + "\n";
        String cashResult = createReceiptStringForValueField("Cash", paidAmount);
        String changeResult = createReceiptStringForValueField("Change", change);

        String result = receipt.createReceiptString();
        assertTrue(result.contains(itemsResult), "Wrong printout. The receipt does not represent items correctly");
        assertTrue(result.contains(discountResult), "Wrong printout. The receipt does not represent discount correctly");
        assertTrue(result.contains(totalResult), "Wrong printout. The receipt does not represent the total correctly");
        assertTrue(result.contains(vatResult), "Wrong printout. The receipt does not represent the VAT correctly");
        assertTrue(result.contains(cashResult), "Wrong printout. The receipt does not represent the cash correctly");
        assertTrue(result.contains(changeResult), "Wrong printout. The receipt does not represent change correctly");

        assertTrue(result.contains(Integer.toString(purchaseTime.getYear())), "Wrong rental year.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getMonthValue())), "Wrong rental month.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getDayOfMonth())), "Wrong rental day.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getHour())), "Wrong rental hour.");
        assertTrue(result.contains(Integer.toString(purchaseTime.getMinute())), "Wrong rental minute.");
    }

    private String createReceiptStringForItem(int amount, String name, Amount price) {
        StringBuilder stringBuilder = new StringBuilder();
        String formatted = String.format("%-40s %12d x %-10s %10s %8s\n",
                name, amount, price, price.scale(amount), Amount.CURRENCY);
        stringBuilder.append(formatted);
        return stringBuilder.toString();
    }

    private String createReceiptStringForValueField(String fieldName, Amount fieldValue) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%-66s %10s %8s\n", fieldName + ":", fieldValue, Amount.CURRENCY));
        return stringBuilder.toString();
    }
}

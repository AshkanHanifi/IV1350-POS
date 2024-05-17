package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

import java.util.Iterator;

/**
 * This class contains a hardcoded version of a view, since the program doesn't have a view
 */
public class View {
    private Controller controller;

    /**
     * Creates a new instance, describes the view of a point of sale
     * @param controller the {@link Controller} controlling the point of sale
     */
    public View(Controller controller) {
        this.controller = controller;
        fakeAction();
    }

    /**
     * Simulates interaction with the controller
     */
    public void fakeAction() {
        controller.startSale();
        scanItem("abc123");
        scanItem("abc123");
        scanItem("q1");
        scanItem("def456");
        endSale();
        Amount change = pay(new Amount(100));
        System.out.println("Change to give the customer: " + change + " " + Amount.CURRENCY);
    }

    private Amount pay(Amount amount) {
        System.out.println("Customer pays " + amount + " " + Amount.CURRENCY + ":");
        Amount change = controller.pay(amount);
        return change;
    }

    private void endSale() {
        System.out.println("End sale:");
        System.out.println("Total cost (incl VAT): " + controller.endSale() + " " + Amount.CURRENCY);
    }

    private void scanItem(String itemIdentifier) {
        System.out.println("Add 1 item with item id " + itemIdentifier);
        SaleDTO saleDTO = controller.scanItem(itemIdentifier);
        printItemInfo(saleDTO, itemIdentifier);
        System.out.println("Total cost (incl VAT): " + saleDTO.getTotal() + " " + Amount.CURRENCY);
        System.out.println("Total VAT: " + saleDTO.getVatAmount() + " " + Amount.CURRENCY + "\n");
    }

    private void printItemInfo(SaleDTO saleDTO, String itemIdentifier) {
        Iterator<ItemDTO> iterator = saleDTO.getItems().keySet().iterator();
        ItemDTO currentItem;
        while (iterator.hasNext()) {
            currentItem = iterator.next();
            if (itemIdentifier.equals(currentItem.getItemIdentifier())) {
                System.out.println(currentItem);
            }
        }
    }
}

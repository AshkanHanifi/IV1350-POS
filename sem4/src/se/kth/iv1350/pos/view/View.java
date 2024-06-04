package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.controller.OperationFailedException;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.NoSuchItemException;
import se.kth.iv1350.pos.model.DiscountContainer;
import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.FileLogger;
import se.kth.iv1350.pos.util.SystemLogger;

import java.io.IOException;
import java.util.Iterator;


/**
 * This class contains a hardcoded version of a view, since the program doesn't have a view
 */
@SuppressWarnings("unused")
public class View {
    private Controller controller;
    private SystemLogger logger;

    /**
     * Creates a new instance, describes the view of a point of sale
     * @param controller the {@link Controller} controlling the point of sale
     * @throws IOException 
     */
    public View(Controller controller) throws IOException {
        this.controller = controller;
        this.logger=new FileLogger("exception-log.txt", false);
        controller.addRevenueObserver(new TotalRevenueFileOutput());
        controller.addRevenueObserver(new TotalRevenueView());
        controller.addLogger(logger);
        fakeAction();
    }

    private void fakeAction() {
        controller.startSale();
        scanItem("abc123");
        scanItem("abc123");
        scanItem("q1");
        scanItem("q1d");
        scanItem("def456");
        scanItem("error");
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
        DiscountContainer discountContainer =controller.percentageDiscount("discount");
        if(discountContainer.getDiscountAmount().equals(new Amount(0))){
            System.out.println("No percentage discount found");
        } else {
            System.out.println("Reduced total by " + discountContainer.getDiscountAmount().scale(100f) + "%");
            System.out.println("New total: " + discountContainer.getNewTotal());
        }
        discountContainer = controller.discountTotal();
        if(discountContainer.getDiscountAmount().equals(new Amount(0))){
            System.out.println("No total discount found");
        } else {
            System.out.println("Reduced total by " + discountContainer.getDiscountAmount());
            System.out.println("New total: " + discountContainer.getNewTotal());
        }
        System.out.println();
        System.out.println("End sale:");
        System.out.println("Total cost (incl VAT): " + controller.endSale() + " " + Amount.CURRENCY);
    }

    private void scanItem(String itemIdentifier) {
        System.out.println("Add 1 item with item id " + itemIdentifier);
        SaleDTO saleDTO=null;
        try {
            saleDTO = controller.scanItem(itemIdentifier);
            printItemInfo(saleDTO, itemIdentifier);
            System.out.println("Total cost (incl VAT): " + saleDTO.getTotal() + " " + Amount.CURRENCY);
            System.out.println("Total VAT: " + saleDTO.getVatAmount() + " " + Amount.CURRENCY + "\n");
        } catch (NoSuchItemException e) {
            System.out.println("No item found with following identifier: " + itemIdentifier);
            System.out.println();
            logger.logException(e);
        } catch (OperationFailedException e) {
            System.out.println("Could not add item");
            System.out.println();
            logger.logException(e);
        }
    }

    private void printItemInfo(SaleDTO saleDTO, String itemIdentifier) {
        Iterator<ItemDTO> iterator = saleDTO.getItems().keySet().iterator();
        ItemDTO currentItem;
        while (iterator.hasNext()) {
            currentItem = iterator.next();
            if (itemIdentifier.equals(currentItem.getItemIdentifier())) {
                System.out.println(itemDTOToString(currentItem));
            }
        }
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

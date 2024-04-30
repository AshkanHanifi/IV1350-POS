package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.util.Amount;

import java.util.Iterator;

public class View {
    private Controller controller;
    public View(Controller controller){
        this.controller=controller;
    }

    public void fakeAction(){
        controller.startSale();
        scanItem("abc123");
        scanItem("abc123");
        scanItem("def456");
        endSale();
        pay(new Amount(100));
    }

    private void pay(Amount amount){
        System.out.println("Customer pays " +amount + " :");
        controller.pay(amount);
    }
    private void endSale(){
        System.out.println("End sale:");
        System.out.println("Total cost (incl VAT): " + controller.endSale());
    }
    private void scanItem(String itemIdentifier){
        System.out.println("Add 1 item with item id " + itemIdentifier);
        SaleDTO saleDTO=controller.scanItem(itemIdentifier);
        printItemInfo(saleDTO, itemIdentifier);
        System.out.println("Total cost (incl VAT): " + saleDTO.getTotal());
        System.out.println("Total VAT: " + saleDTO.getVatAmount() + "\n");
    }
    private void printItemInfo(SaleDTO saleDTO, String itemIdentifier){
        Iterator<ItemDTO> iterator = saleDTO.getItems().keySet().iterator();
        ItemDTO currentItem;
        while (iterator.hasNext()){
            currentItem=iterator.next();
            if(itemIdentifier.equals(currentItem.getItemIdentifier())){
                System.out.println(currentItem);
            }
        }
    }
}

package se.kth.iv1350.pos.startup;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.view.View;

public class Main {
    public static void main(String[] args) {
        ExternalSystemCreator exCreator=new ExternalSystemCreator();
        ReceiptPrinter printer=new ReceiptPrinter();
        Controller controller=new Controller(exCreator,printer);
        View view = new View(controller);

        view.fakeAction();
    }
}

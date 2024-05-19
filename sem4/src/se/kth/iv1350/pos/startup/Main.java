package se.kth.iv1350.pos.startup;

import java.io.IOException;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ExternalSystemCreator;
import se.kth.iv1350.pos.integration.ReceiptPrinter;
import se.kth.iv1350.pos.view.View;

public class Main {
    public static void main(String[] args) {
        try{
        ExternalSystemCreator exCreator = new ExternalSystemCreator();
        ReceiptPrinter printer = new ReceiptPrinter();
        Controller controller = new Controller(exCreator, printer);
        new View(controller);
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }
}

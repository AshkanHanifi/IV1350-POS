package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.Receipt;

public class ReceiptPrinter {
    public ReceiptPrinter() {
    }

    public void PrintReceipt(Receipt receipt){
        System.out.println(receipt);
    }
}

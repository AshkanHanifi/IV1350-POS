package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.Receipt;

public class ReceiptPrinter {
    public ReceiptPrinter() {
    }

    /**
     * Prints out {@link Receipt} to output
     *
     * @param receipt the {@link Receipt} to be printed
     */
    public void PrintReceipt(Receipt receipt){
        System.out.println(receipt);
    }
}

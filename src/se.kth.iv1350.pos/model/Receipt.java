package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

import java.time.LocalDateTime;

public class Receipt {
    private LocalDateTime timestamp;
    private SaleDTO saleDTO;
    private Amount change;

    private final int PADDING=14;

    Receipt(SaleDTO saleDTO, Amount change){
        //this.timestamp=new LocalDateTime();
        this.saleDTO=saleDTO;
        this.change=change;
    }

    @Override
    public String toString(){

        return null;
    }



}

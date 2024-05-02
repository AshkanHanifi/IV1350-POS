package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

public class Register {
    private Amount balance;
    private final Amount START_BALANCE=new Amount(5000);
    public Register(){
        this.balance=START_BALANCE;
    }

    public void updateRegister(Payment payment, Amount change){

    }
}

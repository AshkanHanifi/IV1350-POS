package se.kth.iv1350.pos.util;

import java.text.DecimalFormat;

public final class Amount {
    private final int amount;
    private final int SCALE_AMOUNT=1000;

    public Amount(int amount){
        this.amount=amount*SCALE_AMOUNT;
    }

    public Amount(float amount) {
        this.amount=(int)(amount*SCALE_AMOUNT);
    }

    public Amount minus(Amount other){
        return new Amount(((float)amount-other.amount)/SCALE_AMOUNT);
    }

    public Amount addition(Amount other){
        return new Amount(((float)amount+other.amount)/SCALE_AMOUNT);
    }

    public Amount scale(float scaleValue){
        float currentAmount=(float)amount/SCALE_AMOUNT;
        return new Amount(currentAmount*scaleValue);
    }

    @Override
    public String toString(){
        float printAmount=(float)amount/SCALE_AMOUNT;
        String stringAmount = String.format("%.2f", printAmount);
        stringAmount=stringAmount.replace(",",":");
        return stringAmount;
    }
}
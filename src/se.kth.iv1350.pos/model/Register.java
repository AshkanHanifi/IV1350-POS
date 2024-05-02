package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.util.Amount;

/**
 * This class represents the cash register of a point of sale
 */
public class Register {
    private Amount balance;
    private final Amount START_BALANCE = new Amount(5000);

    /**
     * Creates a new instance, representing a cash register with a fixed starting {@link Amount}
     */
    public Register() {
        this.balance = START_BALANCE;
    }

    /**
     * Updates the register amount
     *
     * @param paidAmount the {@link Amount} used for paying the sale
     * @param change the {@link Amount} describing the change to be returned
     */
    public void updateRegister(Amount paidAmount, Amount change) {
        this.balance=balance.addition(paidAmount.minus(change));
    }

    /**
     *
     * @return the current balance, type of {@link Amount}
     */
    public Amount getRegisterBalance(){
        return balance;
    }
}

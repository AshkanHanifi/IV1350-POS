package se.kth.iv1350.pos.util;
/**
 * This class describes an a number with three decimals of precision
 */
public final class Amount {
    private final int amount;
    private final int SCALE_AMOUNT = 1000;

    static public final String CURRENCY = "SEK";

    /**
     * 
     * @param amount creates an instance with given integer value
     */
    public Amount(int amount) {
        this.amount = amount * SCALE_AMOUNT;
    }

    /**
     * 
     * @param amount creates an instance with given float value
     */
    public Amount(float amount) {
        this.amount = (int) (amount * SCALE_AMOUNT);
    }

    /**
     * 
     * @param other <code>Amount</code> to be subtraced from this instance
     * @return a new <code>Amount</code> describing the amount after
     * subtracting <code>other</code> from <code>this</code>
     */
    public Amount minus(Amount other) {
        return new Amount(((float) amount - other.amount) / SCALE_AMOUNT);
    }

    /**
     * 
     * @param other <code>Amount</code> to be add to this instance
     * @return a new <code>Amount</code> describing the amount after
     * adding <code>other</code> to <code>this</code>
     */

    public Amount addition(Amount other) {
        return new Amount(((float) amount + other.amount) / SCALE_AMOUNT);
    }

    /**
     * 
     * @param scaleValue the value to scale this amount with
     * @return a new <code>Amount</code> scaled by <code>scaleValue</code>
     */
    public Amount scale(float scaleValue) {
        float currentAmount = (float) amount / SCALE_AMOUNT;
        return new Amount(currentAmount * scaleValue);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Amount)) {
            return false;
        }
        Amount otherAmount = (Amount) other;
        return amount == otherAmount.amount;
    }

    @Override
    public String toString() {
        float printAmount = (float) amount / SCALE_AMOUNT;
        String stringAmount = String.format("%.2f", printAmount);
        stringAmount = stringAmount.replace(",", ":");
        return stringAmount;
    }
}
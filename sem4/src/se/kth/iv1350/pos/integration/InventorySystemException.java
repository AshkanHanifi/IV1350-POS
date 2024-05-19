package se.kth.iv1350.pos.integration;

/**
 * Exception thrown when an error occurs during a operation using
 * a {@link ExternalInventorySystem}
 */
public class InventorySystemException extends RuntimeException{

    /**
     * Creates an <code>Exception</code> with a message
     * @param msg a message describing the exception
     */
    public InventorySystemException(String msg){
        super(msg);
    }
    
}
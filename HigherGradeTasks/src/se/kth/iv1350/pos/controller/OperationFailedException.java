package se.kth.iv1350.pos.controller;

/**
 * Exception thrown whenever a more general exception needs to be thrown to the view
 */
public class OperationFailedException extends Exception{

    /**
     * 
     * @param msg a message detailing the exception
     * @param cause a referens to the original exception
     */
    public OperationFailedException(String msg, Exception cause){
        super(msg, cause);
    }
}

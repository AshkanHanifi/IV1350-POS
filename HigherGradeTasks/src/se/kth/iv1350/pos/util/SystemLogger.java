package se.kth.iv1350.pos.util;
/**
 * Defines the interface of a logger. 
 * Will be implemented by classes logging in different formats
 */
public interface SystemLogger {
    /**
     * 
     * @param message to be logged
     */
    void log(String message);

    /**
     * 
     * @param exception to be logged
     */
    void logException(Exception exception);
}

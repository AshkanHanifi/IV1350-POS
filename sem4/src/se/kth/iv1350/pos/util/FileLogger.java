package se.kth.iv1350.pos.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Exception;

/**
 * A {@link SystemLogger} that logs to a file
 */
public class FileLogger implements SystemLogger{

    private final String LOG_FILE_PATH;
    private PrintWriter filePrinter;


    /**
     * 
     * @param path the path where the messages will be logged
     * @param append determines if file should be overwritten
     * @throws IOException when unable to open file
     */
    public FileLogger(String path, boolean append) throws IOException{
        this.LOG_FILE_PATH=path;
        this.filePrinter=new PrintWriter(new FileWriter(LOG_FILE_PATH, append), true);
    }

    @Override
    public void log(String message) {
        filePrinter.println(message);
    }

    @Override
    public void logException(Exception exception) {
        StringBuilder strb = new StringBuilder("Exception occured: ");
        strb.append(exception.getMessage());
        filePrinter.println(strb);
        exception.printStackTrace(filePrinter);
    }
}

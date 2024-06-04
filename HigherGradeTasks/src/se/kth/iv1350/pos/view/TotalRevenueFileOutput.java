package se.kth.iv1350.pos.view;

import java.io.IOException;

import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.FileLogger;
import se.kth.iv1350.pos.util.SystemLogger;

public class TotalRevenueFileOutput extends TotalRevenueDisplay {

    private SystemLogger logger;
    private SystemLogger errorLog;

    /**
     * 
     * @param errorLog error logger used for logging exceptions
     * @throws IOException when unable to create or find file
     */

    public TotalRevenueFileOutput(SystemLogger errorLog) throws IOException{
        logger=new FileLogger("revenue-log.txt", false);
        this.errorLog=errorLog;
    }

    @Override
    protected void doShowTotalIncome(Amount totalRevenue) throws Exception {
        logger.log("Current revenue: " + totalRevenue);
    }

    @Override
    protected void handleErrors(Exception e) {
        errorLog.logException(e);
    }
}

package se.kth.iv1350.pos.view;

import java.io.IOException;

import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.SystemLogger;


public class TotalRevenueView extends TotalRevenueDisplay{
    private SystemLogger errorLog;


    /**
     * 
     * @param errorLog error logger used for logging exceptions
     * @throws IOException when unable to create or find file
     */

    public TotalRevenueView(SystemLogger errorLog){
        this.errorLog=errorLog;
    }

    @Override
    protected void doShowTotalIncome(Amount totalRevenue) throws Exception {
        System.out.println("Current revenue: " + totalRevenue);
    }

    @Override
    protected void handleErrors(Exception e) {
        errorLog.logException(e);
    }
    
}

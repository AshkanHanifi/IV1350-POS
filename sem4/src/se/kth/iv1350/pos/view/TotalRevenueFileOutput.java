package se.kth.iv1350.pos.view;

import java.io.IOException;

import se.kth.iv1350.pos.model.TotalRevenueObserver;
import se.kth.iv1350.pos.util.Amount;
import se.kth.iv1350.pos.util.FileLogger;
import se.kth.iv1350.pos.util.SystemLogger;

public class TotalRevenueFileOutput implements TotalRevenueObserver {
    private Amount revenue;
    private SystemLogger logger;

    public TotalRevenueFileOutput() throws IOException{
        logger=new FileLogger("revenue-log.txt", false);
        this.revenue=new Amount(0);
    }

    @Override
    public void newSale(Amount revenue) {
        this.revenue=this.revenue.addition(revenue);
        this.revenue.addition(revenue);
        logger.log("Current revenue: " + this.revenue + " Added: " + revenue);
    }
}

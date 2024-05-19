package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.model.TotalRevenueObserver;
import se.kth.iv1350.pos.util.Amount;


public class TotalRevenueView implements TotalRevenueObserver{
    private Amount revenue;

    public TotalRevenueView(){
        this.revenue=new Amount(0);
    }

    @Override
    public void newSale(Amount revenue) {
        this.revenue=this.revenue.addition(revenue);
        System.out.println("Current revenue: " + this.revenue + " Added: " + revenue);
    }
    
}

package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.model.SaleDTO;
import se.kth.iv1350.pos.model.TotalRevenueObserver;
import se.kth.iv1350.pos.util.Amount;

public abstract class TotalRevenueDisplay implements TotalRevenueObserver {
    private Amount totalRevenue = new Amount(0);

    @Override
    public void newSale(SaleDTO saleDTO) {
        calculateTotalIncome(saleDTO.getTotal());
        showTotalIncome();
    }

    private void showTotalIncome() {
        try {
            doShowTotalIncome(totalRevenue);
        } catch (Exception e) {
            handleErrors(e);
        }
    }

    protected abstract void doShowTotalIncome(Amount totalRevenue) throws Exception;

    protected abstract void handleErrors(Exception e);

    private void calculateTotalIncome(Amount revenue) {
        this.totalRevenue = this.totalRevenue.addition(revenue);
    }

}

package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import se.kth.iv1350.pos.util.Amount;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {
    private SaleDTO saleDTOWithTotal100;
    private Payment paymentWithAmt125;
    private Payment paymentWithAmt100;
    private Payment paymentWithAmt75;

    @BeforeEach
    void setUp() {
        saleDTOWithTotal100=new SaleDTO(null,new Amount(100), null, true);
        paymentWithAmt125= new Payment(new Amount(125));
        paymentWithAmt100 =new Payment(new Amount(100));
        paymentWithAmt75 =new Payment(new Amount(75));
    }

    @AfterEach
    void tearDown() {
        saleDTOWithTotal100=null;
        paymentWithAmt125=null;
        paymentWithAmt100 =null;
        paymentWithAmt75 =null;
    }

    @Test
    void testCalculateChangeWhenPaymentGreaterThanTotal() {
        Amount expResult=new Amount(25);
        Amount result=paymentWithAmt125.calculateChange(saleDTOWithTotal100);
        assertEquals(expResult, result,
                 "Calculated change incorrect");
    }
    @Test
    void testCalculateChangeWhenPaymentEqualsTotal() {
        Amount expResult=new Amount(0);
        Amount result=paymentWithAmt100.calculateChange(saleDTOWithTotal100);
        assertEquals(expResult, result,
                "Calculated change incorrect");
    }
    @Test
    void testCalculateChangeWhenPaymentLessThanTotal() {
        Amount expResult=new Amount(-25);
        Amount result=paymentWithAmt75.calculateChange(saleDTOWithTotal100);
        assertEquals(expResult, result,
                "Calculated change incorrect");
    }
}
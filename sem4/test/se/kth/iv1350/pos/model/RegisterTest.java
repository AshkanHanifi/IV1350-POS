package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.util.Amount;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    private Amount amountContaining0;
    private Amount amountContaining5;
    private Register cashRegister;


    @BeforeEach
    void setUp() {
        amountContaining0=new Amount(0);
        amountContaining5=new Amount(5);
        cashRegister=new Register();
    }

    @AfterEach
    void tearDown() {
        amountContaining0=null;
        amountContaining5=null;
        cashRegister=null;
    }

    @Test
    void testUpdatingRegisterWith0AndNoChange() {
        Amount change=new Amount(0);
        Amount expResult=cashRegister.getRegisterBalance();
        cashRegister.updateRegister(amountContaining0, change);
        Amount result=cashRegister.getRegisterBalance();
        assertEquals(expResult, result, "Register not updating correctly");
    }
    @Test
    void testUpdatingRegisterWith5AndNoChange() {
        Amount change=new Amount(5);
        Amount expResult=cashRegister.getRegisterBalance();
        cashRegister.updateRegister(amountContaining5, change);
        Amount result=cashRegister.getRegisterBalance();
        assertEquals(expResult, result, "Register not updating correctly");
    }
    @Test
    void testUpdatingRegisterWith0And5Change() {
        Amount change=new Amount(5);
        Amount expResult=cashRegister.getRegisterBalance().minus(change);
        cashRegister.updateRegister(amountContaining0, change);
        Amount result=cashRegister.getRegisterBalance();
        assertEquals(expResult, result, "Register not updating correctly");
    }
    @Test
    void testUpdatingRegisterWith5And5Change() {
        Amount change=new Amount(5);
        Amount expResult=cashRegister.getRegisterBalance();
        cashRegister.updateRegister(amountContaining5, change);
        Amount result=cashRegister.getRegisterBalance();
        assertEquals(expResult, result, "Register not updating correctly");
    }

}
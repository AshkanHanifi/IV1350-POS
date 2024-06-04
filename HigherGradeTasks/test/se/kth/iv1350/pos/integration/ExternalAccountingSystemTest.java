package se.kth.iv1350.pos.integration;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ExternalAccountingSystemTest {

    ByteArrayOutputStream outStream;
    PrintStream originalSys;
    ExternalAccountingSystem accounting;


    @BeforeEach
    void setUp() throws IOException {
        originalSys = System.out;
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        ExternalSystemCreator exCreator = new ExternalSystemCreator();
        accounting=exCreator.getExternalAccountingSystem();
    }

    @AfterEach
    public void cleanUpStreams() {
        outStream = null;
        System.setOut(originalSys);
        accounting=null;
    }

    @Test
    void testLogSale(){
        String expResult = "Sent sale info to external accounting system.";
        accounting.logSale(null, null);
        assertTrue(outStream.toString().contains(expResult), "Accounting system doesn't correctly inform of update");

    }
}

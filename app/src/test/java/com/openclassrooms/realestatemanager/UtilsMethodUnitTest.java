package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class UtilsMethodUnitTest {

    private static final String TODAY_DATE = "13/01/2021"; //change date before test
    public static final int EURO = 82;
    public static final int DOLLARS = 100;
    public static final int DOLLARS_TO_FORMAT_IN_EUROS = 100000;
    public static final int EUROS_TO_FORMAT_IN_DOLLARS = 100000;
    public static final String EURO_STRING = "122 200 €";
    public static final String DOLLARS_STRING = "81 800 $";

    @Test
    public void getFormatTodayDate(){
        assertEquals(Utils.getTodayDate(), TODAY_DATE);
    }

    @Test
    public void convertDollarToEuro(){
        assertEquals(Utils.convertDollarToEuro(DOLLARS), EURO);
    }

    @Test
    public void convertEuroToDollar(){
        assertEquals(Utils.convertEuroToDollar(EURO), DOLLARS);
    }

    @Test
    public void formatPriceDollarsToEuros(){
        assertEquals(Utils.formatPrice(DOLLARS_TO_FORMAT_IN_EUROS, "EUR", "USD"), EURO_STRING);
    }

    @Test
    public void formatPriceEurosToDollars(){
        assertEquals(Utils.formatPrice(EUROS_TO_FORMAT_IN_DOLLARS, "USD", "EUR"), DOLLARS_STRING);
    }

}
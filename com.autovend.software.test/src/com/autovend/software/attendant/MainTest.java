package com.autovend.software.attendant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.autovend.software.customer.CustomerStationLogic;

public class MainTest {

    @Test
    public void testSetupCustomerStations() {
        int amount = 4;
        List<CustomerStationLogic> stationList = Main.setupCustomerStations(amount);

        assertEquals(amount, stationList.size());

        for (CustomerStationLogic station : stationList) {
            assertNotNull(station);
        }
    }

    @Test
    public void testSetupCustomerStationsZero() {
        int amount = 0;
        List<CustomerStationLogic> stationList = Main.setupCustomerStations(amount);

        assertEquals(amount, stationList.size());
    }

//  ** CURRENTLY NO CODE TO TEST IF CUSTOMER STATION SETUP IS NEGATIVE **
//    @Test(expected = IllegalArgumentException.class)
//    public void testSetupCustomerStationsNegative() {
//        int amount = -1;
//        Main.setupCustomerStations(amount);
//    }
}
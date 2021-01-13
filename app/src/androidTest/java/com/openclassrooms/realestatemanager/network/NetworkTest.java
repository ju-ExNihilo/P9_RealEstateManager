package com.openclassrooms.realestatemanager.network;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.openclassrooms.realestatemanager.utils.NetworkTestUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NetworkTest {

   @Test
    public void isOffline() throws IOException, InterruptedException {
       NetworkTestUtils.setWifiEnabled(false);
       assertFalse(Utils.isConnected());
       NetworkTestUtils.setWifiEnabled(true);
   }

    @Test
    public void isOnline() throws IOException, InterruptedException {
        NetworkTestUtils.setWifiEnabled(true);
        assertTrue(Utils.isConnected());
    }
}

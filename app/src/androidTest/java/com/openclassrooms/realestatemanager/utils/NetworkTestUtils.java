package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class NetworkTestUtils {

    public static void setWifiEnabled(boolean enable){
        try {
            WifiManager wifiManager = (WifiManager) getInstrumentation()
                    .getTargetContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(enable);
        } catch (Exception ignored) {
        }
    }
}

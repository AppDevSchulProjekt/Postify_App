package com.appdev.postify.Manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soeren on 27.04.2016.
 */
public class WifiManagement {
    private static WifiManagement instance;

    /**
     * Private Constructor for Singleton implementation
     */
    private WifiManagement(){}

    public static WifiManagement getInstance(){
        if(instance == null){
            instance = new WifiManagement();
        }
        return instance;
    }

    public List<String> getRouterSSIDList(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        class WifiScanReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        }
        WifiScanReceiver wifiScanReceiver = new WifiScanReceiver();
        context.registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        List<ScanResult> scans = wifiManager.getScanResults();
        ArrayList<String> SSIDList = new ArrayList<>();

        for(ScanResult scan: scans){
            String ssid = scan.SSID;
            if (!SSIDList.contains(ssid) && !ssid.isEmpty()){
                SSIDList.add(scan.SSID);
            }
        }
        context.unregisterReceiver(wifiScanReceiver);
        return SSIDList;
    }
}

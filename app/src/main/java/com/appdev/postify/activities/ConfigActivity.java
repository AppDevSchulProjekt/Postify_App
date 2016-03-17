package com.appdev.postify.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.appdev.postify.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soeren on 14.03.2016.
 * Activity to configure the Router Connection
 */
public class ConfigActivity extends AppCompatActivity {
    Spinner spinner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setTitle(getResources().getString(R.string.config_caption));

        spinner = (Spinner) findViewById(R.id.sp_ssid);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fillSSIDSpinner();
                return false;
            }
        });
    }

    private void fillSSIDSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, getRouterSSIDList());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private List<String> getRouterSSIDList(){
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        class WifiScanReceiver extends BroadcastReceiver{
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        }
        WifiScanReceiver wifiScanReceiver = new WifiScanReceiver();
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        List<ScanResult> scans = wifiManager.getScanResults();
        ArrayList<String> SSIDList = new ArrayList<>();

        for(ScanResult scan: scans){
            String ssid = scan.SSID;
            if (!SSIDList.contains(ssid)){
                SSIDList.add(scan.SSID);
            }
        }
        return SSIDList;
    }
}
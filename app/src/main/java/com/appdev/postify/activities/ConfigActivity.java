package com.appdev.postify.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appdev.postify.BaseApplication;
import com.appdev.postify.Controller.DBController;
import com.appdev.postify.R;
import com.appdev.postify.datastorage.SDCardManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soeren on 14.03.2016.
 * Activity to configure the Router Connection
 */
public class ConfigActivity extends AppCompatActivity {
    Toolbar toolbar;
    Spinner ssidSpinner;
    Button confirmButton;
    EditText networkKeyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity Settings
        setContentView(R.layout.activity_config);
        setTitle(getResources().getString(R.string.config_caption));

        // Toolbar Settings
        toolbar = (Toolbar) findViewById(R.id.tool_bar_config);
        setSupportActionBar(toolbar);

        // SSID Spinner Settings
        ssidSpinner = (Spinner) findViewById(R.id.sp_ssid);
        ssidSpinner.setOnTouchListener(ssidOnTouchListener);

        // Confirm Button Settings
        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(confirmButtonClickListener);

        // Network Key Settings
        networkKeyEditText = (EditText) findViewById(R.id.network_key_edit_text);
        
        // TODO: 25.04.2016 Solange keine SD Karte eingelegt ist soll eine AlertDialog angezeigt werden.

    }

    View.OnTouchListener ssidOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            fillSSIDSpinner();
            return false;
        }
    };

    View.OnClickListener confirmButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //if (networkKeyEditText.getText().length() > 0 && ssidSpinner.getSelectedItem().toString().length() > 0) {
                //ssid and key are filled:
                // TODO: 25.04.2016 Textdateien auf SSD Karte speichern
                //buildSDConnection();
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext()); // Context entscheidend
                builder.setTitle("SD Karte erforderlich");
                builder.setMessage("Es konnte keine SD Karte gefunden werden. Bitte prüfen Sie ob diese korrekt eingelegt ist und widerholen Sie den Vorgang");
                builder.setPositiveButton("Wiederholen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.cancel();
                    }
                });
                AlertDialog aDialog = builder.create();
                aDialog.show();
                */
                if(SDCardManager.trySaveFile("Testdatei2.txt", "Testinhalt")){
                    Toast.makeText(ConfigActivity.this, "Datei erzeugt", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ConfigActivity.this, "Datei nicht erzeugt", Toast.LENGTH_LONG).show();
                }


            //} else {
                // Bitte fülle die Felder aus
            //    Toast.makeText(getApplicationContext(), "Bitte felder ausfüllen", Toast.LENGTH_LONG).show();
            //}
        }
    };

    private void fillSSIDSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, getRouterSSIDList());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssidSpinner.setAdapter(adapter);
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

    private void writeBytes() {
        // Provides a client-side TCP socket.
        Socket client;

                try{
                    // Creates a new unconnected socket.
                    client = new Socket();
                    // Connects this socket to the given remote host address and port specified by the SocketAddress remoteAddr with the specified timeout
                    client.connect(new InetSocketAddress(" www.example.com", 80));
                    // Constructs a new DataOutputStream on the OutputStream out.
                    DataOutputStream DataOut = new DataOutputStream(client.getOutputStream());
                    // Writes the low order 8-bit bytes from the specified string.
                    DataOut.writeBytes(ssidSpinner.getSelectedItem().toString() + " " + networkKeyEditText.getText());
                    // Flushes this stream to ensure all pending data is sent out to the target stream.
                    DataOut.flush();

                    // Test Log und Toast
                    System.out.println(ssidSpinner.getSelectedItem().toString() + " " + networkKeyEditText.getText());
                    Toast.makeText(getApplicationContext(), "Write Bytes", Toast.LENGTH_LONG).show();

                } catch(UnknownHostException e) {
                    // if the host name could not be resolved into an IP address.
                    System.out.println("Unknown host: www.example.com");

                } catch(IOException e) {
                    // if the socket is already connected or an error occurs while connecting.
                    // if an error occurs while writing to the target stream.
                    // if an error occurs attempting to flush this stream.
                    System.out.println("No I/O");
                }
    }

}

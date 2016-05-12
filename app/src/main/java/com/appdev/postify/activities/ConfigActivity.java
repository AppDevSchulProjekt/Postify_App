package com.appdev.postify.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.appdev.postify.BaseApplication;
import com.appdev.postify.Manager.WifiManagement;
import com.appdev.postify.R;
import com.appdev.postify.datastorage.PreferencesManagement;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Soeren on 14.03.2016.
 * Activity to configure the Router Connection
 */
public class ConfigActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText ssidEditText;
    Button confirmButton;
    EditText networkKeyEditText;
    CheckBox showNetworkKeyCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity Settings
        setContentView(R.layout.activity_config);
        setTitle(getResources().getString(R.string.config_caption));

        // Toolbar Settings
        toolbar = (Toolbar) findViewById(R.id.tool_bar_config);
        setSupportActionBar(toolbar);

        // SSID EditText Settings
        ssidEditText = (EditText) findViewById(R.id.sp_ssid);
        ssidEditText.setText(PreferencesManagement.getStringPreferences(PreferencesManagement.SSID_Key));
        ssidEditText.setOnClickListener(ssidOnClickListener);

        // Confirm Button Settings
        confirmButton = (Button) findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(confirmButtonClickListener);

        // Network Key Settings
        networkKeyEditText = (EditText) findViewById(R.id.network_key_edit_text);
        networkKeyEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        networkKeyEditText.setText(PreferencesManagement.getStringPreferences(PreferencesManagement.NetworkKey_Key));

        // Network Key Checkbox
        showNetworkKeyCheckBox = (CheckBox) findViewById(R.id.cb_show_net_key);
        showNetworkKeyCheckBox.setOnCheckedChangeListener(showNetworkKeyOnCheckedChangeListener);
    }

    View.OnClickListener ssidOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSSIDSelection();
        }
    };

    OnCheckedChangeListener showNetworkKeyOnCheckedChangeListener = new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                // show password
                networkKeyEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                // hide password
                networkKeyEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            networkKeyEditText.setSelection(networkKeyEditText.getText().length());
        }
    };

    View.OnClickListener confirmButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!networkKeyEditText.getText().toString().isEmpty() && !ssidEditText.getText().toString().isEmpty()) {
                // Get Device iD
                String id = PreferencesManagement.getStringPreferences(BaseApplication.DEVICEID_KEY);
                // Create Config Data
                String text = ssidEditText.getText().toString()+"\n"+networkKeyEditText.getText().toString()+"\n"+id+"\n";
                // Send Config Data
                new SendDataTask().execute(text);
                // Save Values for Textfields
                PreferencesManagement.saveStringPreferences(PreferencesManagement.SSID_Key, ssidEditText.getText().toString());
                PreferencesManagement.saveStringPreferences(PreferencesManagement.NetworkKey_Key, networkKeyEditText.getText().toString());
            } else {
                // The Fields have to be filled
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_fill_fields), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void showSSIDSelection() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfigActivity.this); // Context entscheidend
        builder.setTitle(getResources().getString(R.string.title_available_networks));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.select_dialog_item, WifiManagement.getInstance().getRouterSSIDList(this));
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ssidEditText.setText(adapter.getItem(which));
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class SendDataTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... configDataText) {
            try{
                // Creates a new unconnected client.
                Socket client = new Socket();

                // Identfify IP Address from connected Wifi Modul
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                String stringIP = android.text.format.Formatter.formatIpAddress(wifiManager.getDhcpInfo().serverAddress);

                // Connects this client to the given remote host address and port.
                client.connect(new InetSocketAddress(stringIP, 8090));
                // Returns an output stream to write data into this client.
                DataOutputStream DataOut = new DataOutputStream(client.getOutputStream());
                // Writes the low order 8-bit bytes from the specified string.
                DataOut.writeBytes(configDataText[0]);
                // Flushes this stream to ensure all pending data is sent out to the target stream.
                DataOut.flush();
                // Constructs a new BufferedReader, providing in with a buffer of 8192 characters.
                // Constructs a new InputStreamReader on the InputStream in.
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                while ((in.readLine()) != null) {
                    // Returns the next line of text available from this reader.
                    return "success";
                }

            } catch(UnknownHostException e) {
                Log.e("ConfigActivity",e.getMessage());
                return e.getMessage();
            } catch(IOException e) {
                Log.e("ConfigActivity",e.getMessage());
                return e.getMessage();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String aString) {
            super.onPostExecute(aString);
            if (aString == "success") {
                Toast.makeText(getApplicationContext(), "Daten erfolgreich gesendet", Toast.LENGTH_LONG).show();
                ConfigActivity.this.finish();
            } else {
                if (aString.length() > 0) {
                    Toast.makeText(getApplicationContext(), aString, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}

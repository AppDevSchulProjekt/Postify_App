package com.appdev.postify.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.appdev.postify.Manager.WifiManagement;
import com.appdev.postify.R;
import com.appdev.postify.datastorage.PreferencesManagement;

import java.io.DataOutputStream;
import java.io.IOException;
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
                //ssid and key are filled:

                /*
                if(SDCardManager.trySaveFile("Testdatei2.txt", "Testinhalt")){
                    Toast.makeText(ConfigActivity.this, "Datei erzeugt", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ConfigActivity.this, "Datei nicht erzeugt", Toast.LENGTH_LONG).show();
                }
                */

                // TODO: 28.04.2016 Prüfe SD Karte
                // TODO: 28.04.2016 Schreibe auf SD Karte

                PreferencesManagement.saveStringPreferences(PreferencesManagement.SSID_Key, ssidEditText.getText().toString());
                PreferencesManagement.saveStringPreferences(PreferencesManagement.NetworkKey_Key, networkKeyEditText.getText().toString());
                Toast.makeText(ConfigActivity.this, getResources().getString(R.string.msg_config_saved), Toast.LENGTH_SHORT).show();
                ConfigActivity.this.finish();

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
            DataOut.writeBytes(ssidEditText.getText().toString() + " " + networkKeyEditText.getText());
            // Flushes this stream to ensure all pending data is sent out to the target stream.
            DataOut.flush();

            // Test Log und Toast
            System.out.println(ssidEditText.getText().toString() + " " + networkKeyEditText.getText());
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

package com.appdev.postify.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.appdev.postify.R;

/**
 * Created by Soeren on 26.04.2016.
 */
public class StatisticsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView ssidTextView = (TextView) findViewById(R.id.DeviceId);
        ssidTextView.setText(Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID));
    }
}

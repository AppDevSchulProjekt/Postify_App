package com.appdev.postify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickstartConfig(final View sfNormal){
        Intent i = new Intent(this, ConfigActivity.class);
        startActivity(i);
    }

    // @Override Methods... onCreateOptionsMenu
    // @Override Methods... onOptionsItemSelected
}

package com.appdev.postify;

import android.content.Intent;
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

    // HttpURLConnection - http://developer.android.com/reference/java/net/HttpURLConnection.html
    // for POST and GET Requests

    // AsyncTask -  http://developer.android.com/reference/android/os/AsyncTask.html
    // Load Data a synchron

    // SQLiteOpenHelper - http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html
    // http://www.programmierenlernenhq.de/android-sqlite-datenbank-tutorial/

    // Shared Preferences - http://developer.android.com/reference/android/content/SharedPreferences.html
    // Save Values (firststart)

    // Listview - http://developer.android.com/guide/topics/ui/layout/listview.html
    // Show Table Data

    // @Override Methods... onCreateOptionsMenu
    // @Override Methods... onOptionsItemSelected
}

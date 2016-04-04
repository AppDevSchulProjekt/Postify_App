package com.appdev.postify.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.appdev.postify.model.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Soere on 30.03.2016.
 */
public class DBController {
    private static DBController instance;
    private SQLiteDatabase database;

    public static final int TODAY = 1;
    public static final int WEEK = 7;
    public static final int ALL = -1;

    private static final String LOCAL_DATABASE_NAME = "LocalDatabase";
    private static final String ENTRY_TABLE_NAME = "Entries";
    private static final String TIME_FIELD_NAME = "time";
    private static final String WEIGHT_FIELD_NAME = "weight";

    public static DBController getInstance() {
        if(instance == null){
            instance = new DBController();
        }
        return instance;
    }

    private DBController() {

    }

    public ArrayList<Entry> readLocalEntries(int days, Context context){
        ArrayList<Entry> entries = new ArrayList<>();

        if(days != -1) {
            days = days * -1;
            days++;
        }

        Calendar compareTime = new GregorianCalendar();
        compareTime.add(Calendar.DAY_OF_MONTH, days);
        compareTime.set(Calendar.HOUR, 0);
        compareTime.set(Calendar.MINUTE, 0);
        compareTime.set(Calendar.SECOND, 0);
        compareTime.set(Calendar.MILLISECOND, 0);

        database = context.openOrCreateDatabase(LOCAL_DATABASE_NAME, Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + ENTRY_TABLE_NAME + " ("+ TIME_FIELD_NAME +" INTEGER, "+ WEIGHT_FIELD_NAME +" TEXT)"); // Aufbau der Tabelle an ServerDB anpassen

        Cursor dbEntries = database.rawQuery("SELECT * FROM "+ ENTRY_TABLE_NAME, null);
        dbEntries.moveToFirst();
        if(dbEntries.getCount() > 0){
            do {
                long timeAsInt = dbEntries.getInt(dbEntries.getColumnIndex("time"));
                Calendar time = new GregorianCalendar();
                //time.setTime(new Date(timeAsInt));
                time.setTimeInMillis(timeAsInt * 1000);

                if(days == -1){
                    // Gesamtansicht
                    Float weight = dbEntries.getFloat(dbEntries.getColumnIndex("weight"));
                    entries.add(new Entry(time, weight));
                }else {
                    if (time.compareTo(compareTime) > 0){
                        entries.add(new Entry(time, dbEntries.getFloat(dbEntries.getColumnIndex("weight"))));
                    }
                }
            }while (dbEntries.moveToNext());
        }
        return entries;
    }

    public void fuelleDatenbankMitTestdaten(Context context){
        database = context.openOrCreateDatabase(LOCAL_DATABASE_NAME, Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + ENTRY_TABLE_NAME + " (" + TIME_FIELD_NAME + " INTEGER, " + WEIGHT_FIELD_NAME + " TEXT)");

        //database.execSQL("DELETE FROM " + ENTRY_TABLE_NAME);

        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Toast.makeText(context, deviceId ,Toast.LENGTH_LONG).show();

        Calendar cal = new GregorianCalendar();
        //cal.add(Calendar.DAY_OF_MONTH, -2);
        Entry entry = new Entry(cal, 10f);
        Log.d("TimeTest", String.valueOf(entry.getTime()));
        database.execSQL("INSERT INTO " + ENTRY_TABLE_NAME + " VALUES(" + entry.getTime().getTimeInMillis() / 1000 + ", " + entry.getWeight() + ")");
    }

    public void readExternalEntries(Context context){
        //IP: postifier.esy.es
        //HTTP-Request: postifier.esy.es/get.php?id=DeviceID

    }

}

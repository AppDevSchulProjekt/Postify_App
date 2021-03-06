package com.appdev.postify.Controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.appdev.postify.activities.MainActivity;
import com.appdev.postify.model.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by Soeren on 30.03.2016.
 */
public class DBController {
    private static DBController instance;
    private SQLiteDatabase database;

    private HttpURLConnection connection;
    private InputStreamReader streamReader;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final int TODAY = 1;
    public static final int WEEK = 7;
    public static final int ALL = -1;

    private static final String LOCAL_DATABASE_NAME = "LocalDatabase";
    private static final String ENTRY_TABLE_NAME = "Entries";
    private static final String TIME_FIELD_NAME = "timestamp";
    private static final String WEIGHT_FIELD_NAME = "weight";

    public static DBController getInstance() {
        if(instance == null){
            instance = new DBController();
        }
        return instance;
    }

    /**
     * Private Constructor for Singleton implementation
     */
    private DBController() {}

    public ArrayList<Entry> readLocalEntries(int days, Context context){
        ArrayList<Entry> entries = new ArrayList<>();

        if(days != -1) {
            days = days * -1;
            days++;
        }

        Calendar compareTime = Entry.makeCaledarWithoutTime(new GregorianCalendar()); //Aktuelle Zeit
        compareTime.add(Calendar.DAY_OF_MONTH, days);

        Log.d("DebuggDate", String.valueOf(compareTime.getTimeInMillis()));

        database = context.openOrCreateDatabase(LOCAL_DATABASE_NAME, Context.MODE_PRIVATE, null);
        // TODO: 06.04.2016 delete später löschen
        //database.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE_NAME);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + ENTRY_TABLE_NAME + " (" + TIME_FIELD_NAME + " INTEGER, " + WEIGHT_FIELD_NAME + " DOUBLE)"); // Aufbau der Tabelle an ServerDB anpassen

        Cursor dbEntries = database.rawQuery("SELECT * FROM "+ ENTRY_TABLE_NAME + " ORDER BY "+TIME_FIELD_NAME+" DESC", null);
        dbEntries.moveToFirst();
        if(dbEntries.getCount() > 0){
            do {
                int timeAsInt = dbEntries.getInt(dbEntries.getColumnIndex(TIME_FIELD_NAME));
                Calendar time = new GregorianCalendar();
                time.setTimeInMillis(timeAsInt * 1000L);

                if(days == -1){
                    // Gesamtansicht
                    Double weight = dbEntries.getDouble(dbEntries.getColumnIndex(WEIGHT_FIELD_NAME));
                    entries.add(new Entry(timeAsInt, weight));
                }else {
                    //if (time.compareTo(compareTime) > 0){
                    if(time.getTime().after(compareTime.getTime())){
                        entries.add(new Entry(time, dbEntries.getDouble(dbEntries.getColumnIndex(WEIGHT_FIELD_NAME))));
                    }
                }
            }while (dbEntries.moveToNext());
        }
        return entries;
    }

    public void readExternalEntries(Context context, SwipeRefreshLayout swipeRefreshLayout ) {
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;

        try {
            String android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            URL url = new URL("http://postifier.esy.es/get.php?id=" + android_id);
            new AsyncClass().execute(url);

        }catch (Exception e){
            //keine Verbindung zur DB aufgebaut
        }finally {
            if(streamReader != null){
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class AsyncClass extends AsyncTask<URL,Integer,String>{

        @Override
        protected String doInBackground(URL... params) {
            StringBuffer sb = new StringBuffer();

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                streamReader = new InputStreamReader(connection.getInputStream());
                int data = streamReader.read();

                while (data != -1){
                    sb.append((char) data);
                    data = streamReader.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                List<Entry> entries = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    int timestamp = jObj.getInt(TIME_FIELD_NAME);
                    double weight = jObj.getDouble(WEIGHT_FIELD_NAME);

                    entries.add(new Entry(timestamp, weight));
                    this.cancel(true);
                }
                refreshLocalDatabase(entries);
                MainActivity.updateAdapter();

                if (swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void refreshLocalDatabase(List<Entry> entriesExtern){
            database = context.openOrCreateDatabase(LOCAL_DATABASE_NAME, Context.MODE_PRIVATE, null);
            Cursor localEntriesCursor = database.rawQuery("SELECT * FROM " + ENTRY_TABLE_NAME + " ORDER BY " + TIME_FIELD_NAME + " DESC LIMIT 1", null);
            int timeInSecondsLocal = 0;
            if(localEntriesCursor.getCount()>0) {
                localEntriesCursor.moveToFirst();
                timeInSecondsLocal = localEntriesCursor.getInt(localEntriesCursor.getColumnIndex(TIME_FIELD_NAME));
            }

            for (Entry entry: entriesExtern) {
                int timeInSecondsExtern = entry.getSeconds();
                if(timeInSecondsExtern > timeInSecondsLocal){
                    database.execSQL("INSERT INTO "+ENTRY_TABLE_NAME+" VALUES ("+timeInSecondsExtern+","+entry.getWeight()+")");
                }
            }
        }
    }
}
package com.appdev.postify.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.appdev.postify.Controller.DBController;
import com.appdev.postify.R;

import com.appdev.postify.model.Entry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Soeren on 26.04.2016.
 */
public class StatisticsActivity extends AppCompatActivity{
    BarChart barChart;

    private final int AMOUNT_OF_BARS = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.stats_caption));
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_stats);
        setSupportActionBar(toolbar);

        setupBarChart();

    }
    private void setupBarChart(){
        barChart = (BarChart) findViewById(R.id.lineChart);
        barChart.setDrawGridBackground(false); //Keine Auswirkung
        barChart.setDrawBorders(false); //Keine Auswirkung
        barChart.setDescription(null);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);


        ArrayList<Entry> entries = DBController.getInstance().readLocalEntries(DBController.WEEK, this);
        Collections.reverse(entries);
        int[] werte = new int[AMOUNT_OF_BARS];
        String[] captions = new String[AMOUNT_OF_BARS];
        for (int j = 0; j<captions.length;j++) {
            captions[j] = " ";
        }
        int oldDayOfMonth = 0;
        int i = -1;

        for (Entry entry: entries) {
            int dayOfMonth = entry.getTime().get(Calendar.DAY_OF_MONTH);
            if(dayOfMonth != oldDayOfMonth){
                i++;
                captions[i] = String.valueOf(dayOfMonth) + "." + entry.getTime().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

                oldDayOfMonth = dayOfMonth;
            }
            werte[i] ++;
        }

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int counter = 0; counter < AMOUNT_OF_BARS; counter++) {
            barEntries.add(new BarEntry(werte[counter],counter));
            xVals.add(captions[counter]);
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Anzahl Posteingänge");
        barDataSet.setColor(getResources().getColor(R.color.Blue_700));

        BarData barData = new BarData(xVals, barDataSet);
        barChart.setData(barData);
        barChart.invalidate(); // refresh
    }
}

package com.appdev.postify.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Soeren on 21.03.2016.
 */
public class Entry {

    private Calendar time;
    private Double weight;

    public Entry(){
        time = new GregorianCalendar();
        weight = 0d;
    }

    public Entry(Calendar time, Double weight){
        setTime(time);
        setWeight(weight);
    }
    public Entry(int timeinSeconds, Double weight){
        time = new GregorianCalendar();
        time.setTimeInMillis(timeinSeconds * 1000L);
        setWeight(weight);
    }

    public Entry(long timeinMillis, Double weight){
        time = new GregorianCalendar();
        time.setTimeInMillis(timeinMillis);
        setWeight(weight);
    }

    public Calendar getTime() {
        return time;
    }

    public int getSeconds(){
        long timeInMillis = time.getTimeInMillis();
        int timeInSeconds = (int) (timeInMillis / 1000);
        return timeInSeconds;
    }

    public long getMillis(){
        return time.getTimeInMillis();
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Double getWeight() {
        return weight;
    }

    public String getFormattedTime(int currentTab, String todayCaption, String yesterdayCaption){
        String formattedTime = "";

        Calendar today = new GregorianCalendar();
        Calendar yesterday = new GregorianCalendar();
        yesterday.add(Calendar.DAY_OF_MONTH,-1);

        DateFormat df;
        if(today.get(Calendar.DAY_OF_MONTH) == time.get(Calendar.DAY_OF_MONTH)){
            if(currentTab == 1){
                df = DateFormat.getTimeInstance();
                formattedTime = df.format(time.getTime());
            }else {
                df = DateFormat.getTimeInstance();
                formattedTime = todayCaption + " - " + df.format(time.getTime());
            }
        }else if(yesterday.get(Calendar.DAY_OF_MONTH) == time.get(Calendar.DAY_OF_MONTH)){
            df = DateFormat.getTimeInstance();
            formattedTime = yesterdayCaption + " - " + df.format(time.getTime());
        }else{
            df = DateFormat.getDateTimeInstance();
            formattedTime = df.format(time.getTime());
        }
        return formattedTime;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}

package com.appdev.postify.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Soere on 21.03.2016.
 */
public class Entry {

    private Calendar time;
    private Float weight;

    public Entry(){
    }

    public Entry(Calendar time, Float weight){
        setTime(time);
        setWeight(weight);
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getFormattedTime(int currentTab){
        Calendar today = new GregorianCalendar();
        Calendar yesterday = new GregorianCalendar();

        DateFormat df;
        if(today.get(Calendar.DAY_OF_MONTH) == time.get(Calendar.DAY_OF_MONTH)){
            if(currentTab == 1){
                df = new SimpleDateFormat("hh:mm");
            }else {
                df = new SimpleDateFormat("'Heute' - hh:mm"); //DateFormat.getDateTimeInstance();
            }
        }else{
            df = new SimpleDateFormat("dd.MM.yyyy - hh:mm");
        }

        return df.format(time.getTime());
    }
}

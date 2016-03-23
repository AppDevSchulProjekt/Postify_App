package com.appdev.postify.model;

import java.util.Date;

/**
 * Created by Soere on 21.03.2016.
 */
public class Entry {

    //private Date date;

    private int day;
    private int month;
    private int year;

    private Float weight;

    public Entry(){
    }

    public Entry(int day, int month, int year, Float weight){
        setDay(day);
        setMonth(month);
        setYear(year);
        setWeight(weight);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getDate(){
        return day + "." + month + "." + year;
    }
}

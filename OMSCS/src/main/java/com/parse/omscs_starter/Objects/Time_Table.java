package com.parse.omscs_starter.Objects;

/**
 * Created by wangd on 7/12/2017.
 */

public class Time_Table {
    public static final String TABLE = "time_table";

    //FIELDS
    public static final String KEY_TIME = "time";
    public static final String KEY_YESTERDAY = "yesterday";
    public static final String KEY_WEEK = "week";      //String to make it easier
    public static final String KEY_PREV_WEEK = "pre_week";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_ID = "course";
    public static final String KEY_DATE = "date";

    //Properties
    public int today;
    public int yesterday;
    public int week;
    public int pre_week;
    public int total;
    public String course;

    private Time_Table (){}; //empty constructor

    public Time_Table(int today, int yesterday,int week, int pre_week, int total,String course){
        this.today = today;
        this.yesterday = yesterday;
        this.week = week;
        this.pre_week = pre_week;
        this.total = total;
        this.course = course;
    }

    public int getToday() {
        return this.today;
    }

    public int getYesterday() {
        return this.yesterday;
    }

    public int getWeek() {
        return this.week;
    }

    public int getPre_week() {
        return this.pre_week;
    }

    public int getTotal() {
        return this.total;
    }

    public String getID(){
        return this.course;
    }
}

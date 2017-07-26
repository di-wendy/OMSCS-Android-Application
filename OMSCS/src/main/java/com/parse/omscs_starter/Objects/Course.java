package com.parse.omscs_starter.Objects;

/**
 * Created by wangd on 7/10/2017.
 */

public class Course {

    public static final String TABLE = "course_table";

    //FIELDS
    public static final String KEY_DEP = "dep";
    public static final String KEY_NUMBER = "num";
    public static final String KEY_NAME = "name";      //String to make it easier
    public static final String KEY_DIFFICULTY = "difficulty";
    public static final String KEY_HOURS = "hours";


    //Properties
    public String dep;
    public String num;
    public String name;      //String to make it easier
    public float difficulty;
    public float hours;

    private Course (){}; //empty constructor

    public Course(String num,String name,float difficulty, float hours){
        this.num = num;
        this.name = name;
        this.difficulty = difficulty;
        this.hours = hours;
    }

    public Course(String dep,String num,String name,float difficulty, float hours){
        this.dep = dep;
        this.num = num;
        this.name = name;
        this.difficulty = difficulty;
        this.hours = hours;
    }

    public String getDep() {
        return this.dep;
    }

    public String getNum(){
        return this.num;
    }

    public String getName(){
        return this.name;
    }

    public float getDifficulty(){
        return this.difficulty;
    }

    public float getHours(){
        return this.hours;
    }
}

package com.parse.omscs_starter.Support;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.omscs_starter.Objects.Course;
import com.parse.omscs_starter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangd on 7/10/2017.
 */

public class CourseDisplayAdapter extends ArrayAdapter {

    List list = new ArrayList();

    static class fivehandler{
        TextView NUM,NAME,DIFF,HOURS;
    }

    public CourseDisplayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object){
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        CourseDisplayAdapter.fivehandler four;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.four_col,parent,false);

            four = new CourseDisplayAdapter.fivehandler();

            four.NUM = (TextView)row.findViewById(R.id.text_num);
            four.NAME = (TextView)row.findViewById(R.id.text_name);
            four.DIFF = (TextView)row.findViewById(R.id.text_diff);
            four.HOURS = (TextView)row.findViewById(R.id.text_hours);
            row.setTag( four);
        }
        else{
            four = (CourseDisplayAdapter.fivehandler) row.getTag();
        }

        Course course = (Course)this.getItem(position);

        four.NUM.setText(course.getNum());
        four.NAME.setText(course.getName());
        four.DIFF.setText(Float.toString(course.getDifficulty()));
        four.HOURS.setText(Float.toString(course.getHours()));

        return row;
    }
}

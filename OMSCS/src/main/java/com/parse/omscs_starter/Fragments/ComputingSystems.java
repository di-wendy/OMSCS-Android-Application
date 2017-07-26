package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.omscs_starter.R;
import com.parse.omscs_starter.Support.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ComputingSystems extends Fragment {

    private CheckBox cs_6210;
    private CheckBox cs_6250;
    private CheckBox cs_6400;
    private CheckBox cs_6300;
    private CheckBox cs_6290;

    private CheckBox cs_6035;
    private CheckBox cs_6310;
    private CheckBox cs_6262;
    private CheckBox cs_6220;
    private CheckBox cs_6340;

    private ListView listview;

    private ArrayAdapter<String> listDataAdapter;
    private ArrayList<String> temp_courses;
    private ArrayList<String> course_to_select;

    private HashSet<String> hs;
    private HashMap<String,Float> timetable;
    private HashMap<String,Float> difftable;

    private Button confirm;
    private SparseBooleanArray checkedItems;
    private SharedPreferences sharedPreferences;

    private float time;
    private float difficulty;

    public ComputingSystems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.fragment_computing_systems, container, false);
        confirm = (Button) view.findViewById(R.id.update_computing);
        listview = (ListView) view.findViewById(R.id.course_list_computing);

        course_to_select = new ArrayList<String>();
        temp_courses = new ArrayList<>();
        hs = new HashSet<String>();
        timetable = new HashMap<String,Float>();
        difftable = new HashMap<String,Float>();

        temp_courses.add("8803-G Graduate Algorithms");
    //[6210,6250,6400,6300,6290,6035,6310,6262,6220,6340]
        hs.add("8803-G Graduate Algorithms");
        hs.add("6210 Advanced Operating Systems");
        hs.add("6250 Computer Networks");
        hs.add("6400 Intro to Health Informatics");
        hs.add("6300 Software Development Process");
        hs.add("6290 High Perform Computer Architecture");
        hs.add("6035 Intro to Information Security");
        hs.add("6310 Software Architecture and Design");
        hs.add("6262 Network Security");
        hs.add("6220 Intro to High-Perform Computing");
        hs.add("6340 Software Analysis and Test");

        cs_6210 = (CheckBox) view.findViewById(R.id.check_6210);
        cs_6250 = (CheckBox) view.findViewById(R.id.check_6250);
        cs_6400 = (CheckBox) view.findViewById(R.id.check_6400);
        cs_6300 = (CheckBox) view.findViewById(R.id.check_6300);
        cs_6290 = (CheckBox) view.findViewById(R.id.check_6290);

        cs_6035 = (CheckBox) view.findViewById(R.id.check_6035);
        cs_6310 = (CheckBox) view.findViewById(R.id.check_6310);
        cs_6262 = (CheckBox) view.findViewById(R.id.check_6262);
        cs_6220 = (CheckBox) view.findViewById(R.id.check_6220);
        cs_6340 = (CheckBox) view.findViewById(R.id.check_6340);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        for(ParseObject course:objects){
                            String ID = course.getString("ID");
                            String name = course.getString("Name");
                            if(!hs.contains(ID + " " + name)){
                                course_to_select.add(ID + " " + name);
                            }
                            float diff = course.getNumber("Difficulty").floatValue();
                            float time = course.getNumber("Hours").floatValue();
                            timetable.put(ID + " " + name,time);
                            difftable.put(ID + " " + name,diff);
                        }
                    }

                    listDataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,course_to_select);
                    listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listview.setAdapter(listDataAdapter);
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        listview.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        //Button onClick Listener
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                int elective = 0;

                if (cs_6210.isChecked()) count++;
                if (cs_6250.isChecked()) count++;
                if (cs_6400.isChecked()) count++;
                if (cs_6300.isChecked()) count++;
                if (cs_6290.isChecked()) count++;

                if (count < 2) {
                    Toast.makeText(getActivity(), "Please Pick 2 or more cores", Toast.LENGTH_SHORT).show();
                } else {
                    if (cs_6035.isChecked()) elective++;
                    if (cs_6310.isChecked()) elective++;
                    if (cs_6262.isChecked()) elective++;
                    if (cs_6220.isChecked()) elective++;
                    if (cs_6340.isChecked()) elective++;

                    if (elective < 3) {
                        Toast.makeText(getActivity(), "Please Pick 3 or more electives", Toast.LENGTH_SHORT).show();
                    } else {
                        checkedItems = listview.getCheckedItemPositions();
                        for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                            if (checkedItems.get(i)) {
                                elective++;
                            }
                        }
                        if (count + elective + 1 > 10) {
                            String temp = String.valueOf(count + elective + 1 - 10);
                            Toast.makeText(getActivity(), "Please select " + temp + " less", Toast.LENGTH_SHORT).show();
                        } else if (count + elective + 1 < 10) {
                            String temp = String.valueOf(10 - (count + elective + 1));
                            Toast.makeText(getActivity(), "Please select " + temp + " more", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cs_6210.isChecked()) temp_courses.add("6210 Advanced Operating Systems");
                            if (cs_6250.isChecked()) temp_courses.add("6250 Computer Networks");
                            if (cs_6400.isChecked()) temp_courses.add("6400 Database Concepts and Design");
                            if (cs_6300.isChecked()) temp_courses.add("6300 Software Development Process");
                            if (cs_6290.isChecked()) temp_courses.add("6290 High Perform Computer Architecture");

                            if (cs_6035.isChecked()) temp_courses.add("6035 Intro to Information Security");
                            if (cs_6310.isChecked()) temp_courses.add("6310 Software Architecture and Design");
                            if (cs_6262.isChecked()) temp_courses.add("6262 Network Security");
                            if (cs_6220.isChecked()) temp_courses.add("6220 Intro to High-Perform Computing");
                            if (cs_6340.isChecked()) temp_courses.add("6340 Software Analysis and Test");

                            for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                                if (checkedItems.get(i)) {
                                    temp_courses.add(course_to_select.get(i));
                                }
                            }

                            for(String course: temp_courses){
                                time += timetable.get(course);
                                difficulty += difftable.get(course);
                            }
                            sharedPreferences.edit().putFloat("time",time/10).apply();
                            sharedPreferences.edit().putFloat("diff",difficulty/10).apply();

                            try {
                                sharedPreferences.edit().putString("courses", ObjectSerializer.serialize(temp_courses)).apply();
                                Toast.makeText(getActivity(), "Course Updated!", Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Local_Summary fragment = new Local_Summary();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.commit();


                        }
                    }
                }
            }
        });

        return view;
    }


}

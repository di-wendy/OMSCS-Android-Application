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


public class cpRobotics extends Fragment {

    private CheckBox cs_6601;
    private CheckBox cs_7641;

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

    public cpRobotics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cp_robotics, container, false);


        sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);

        confirm = (Button) view.findViewById(R.id.update_robotics);

        listview = (ListView) view.findViewById(R.id.course_robotics);

        course_to_select = new ArrayList<String>();
        temp_courses = new ArrayList<>();
        hs = new HashSet<String>();
        timetable = new HashMap<String,Float>();
        difftable = new HashMap<String,Float>();

        temp_courses.add("8803-G Graduate Algorithms");
        temp_courses.add("6475 Computational Photography");
        temp_courses.add("6476 Computer Vision");
        temp_courses.add("8803-1 AI for Robotics");

        hs.add("8803-G Graduate Algorithms");
        hs.add("6475 Computational Photography");
        hs.add("6476 Computer Vision");
        hs.add("8803-1 AI for Robotics");
        hs.add("6601 Artificial Intelligence");
        hs.add("7641 Machine Learning");

        cs_6601 = (CheckBox) view.findViewById(R.id.check_6601);
        cs_7641 = (CheckBox) view.findViewById(R.id.check_7641);

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
                if (cs_6601.isChecked()) count++;
                if (cs_7641.isChecked()) count++;

                if (count < 1) {
                    Toast.makeText(getActivity(), "Please Pick 1 or More Core", Toast.LENGTH_SHORT).show();
                }
                else {
                    checkedItems = listview.getCheckedItemPositions();
                    for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                        if (checkedItems.get(i)) {
                            count++;
                        }
                    }

                    if (count + 4 > 10) {
                        String temp = String.valueOf(count + 4 - 10);
                        Toast.makeText(getActivity(), "Please select " + temp + " less", Toast.LENGTH_SHORT).show();
                    }
                    else if (count + 4 < 10) {
                        String temp = String.valueOf(10 - count - 4);
                        Toast.makeText(getActivity(), "Please select " + temp + " more", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (cs_6601.isChecked()) temp_courses.add("6601 Artificial Intelligence");
                        if (cs_7641.isChecked()) temp_courses.add("7641 Machine Learning");

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
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        Local_Summary fragment = new Local_Summary();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();

                        /*
                        ArrayList<String> newFriends = new ArrayList<>();

                        try {

                            newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("courses", ObjectSerializer.serialize(new ArrayList<String>())));


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.i("newFriends", newFriends.toString());
                        */
                    }
                }
            }
        });
        return view;
    }

}

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


public class machine_learning_frag extends Fragment {

    private CheckBox cs_7642;
    private CheckBox cs_7646;
    private CheckBox cs_6242;
    private CheckBox cs_6250;
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

    public machine_learning_frag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.fragment_machine_learning_frag, container, false);

        confirm = (Button) view.findViewById(R.id.update_machine_learning);

        listview = (ListView) view.findViewById(R.id.course_list_computing);

        course_to_select = new ArrayList<String>();
        temp_courses = new ArrayList<>();
        hs = new HashSet<String>();
        timetable = new HashMap<String,Float>();
        difftable = new HashMap<String,Float>();

        temp_courses.add("8803-G Graduate Algorithms");
        temp_courses.add("7641 Machine Learning");

        hs.add("8803-G Graduate Algorithms");
        hs.add("7641 Machine Learning");
        hs.add("7642 Reinforcement Learning");
        hs.add("7646 Machine Learning for Trading");
        hs.add("6242 Data and Visual Analytics");
        hs.add("6250E Big Data for Health Informatics");

        cs_7642 = (CheckBox) view.findViewById(R.id.check_7642);
        cs_7646 = (CheckBox) view.findViewById(R.id.check_7646);
        cs_6242 = (CheckBox) view.findViewById(R.id.check_6242);
        cs_6250 = (CheckBox) view.findViewById(R.id.check_6250E);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        float sum = 0;
                        float sum2 = 0;
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
                            sum += course.getNumber("Difficulty").floatValue();
                            sum2 += course.getNumber("Hours").floatValue();
                        }
                        sum /= 28;
                        sum2 /= 28;
                        //Log.i("Diff",String.valueOf(sum));
                        //Log.i("Diff",String.valueOf(sum2));
                    }

                    listDataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_multiple_choice,
                            course_to_select);
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
                if (cs_7642.isChecked()) count++;
                if (cs_7646.isChecked()) count++;
                if (cs_6242.isChecked()) count++;
                if (cs_6250.isChecked()) count++;

                if (count < 3) {
                    Toast.makeText(getActivity(), "Please Pick 3 or more electives", Toast.LENGTH_SHORT).show();
                }
                else {
                    checkedItems = listview.getCheckedItemPositions();
                    for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                        if (checkedItems.get(i)) {
                            count++;
                        }
                    }

                    if (count + 2 > 10) {
                        String temp = String.valueOf(count + 2 - 10);
                        Toast.makeText(getActivity(), "Please select " + temp + " less", Toast.LENGTH_SHORT).show();
                    } else if (count + 2 < 10) {
                        String temp = String.valueOf(10 - count - 2);
                        Toast.makeText(getActivity(), "Please select " + temp + " more", Toast.LENGTH_SHORT).show();
                    } else {
                        if (cs_7642.isChecked()) temp_courses.add("7642 Reinforcement Learning");
                        if (cs_7646.isChecked()) temp_courses.add("7646 Machine Learning for Trading");
                        if (cs_6242.isChecked()) temp_courses.add("6242 Data and Visual Analytics");
                        if (cs_6250.isChecked()) temp_courses.add("6250E Big Data for Health Informatics");

                        for (int i = 0; i < listview.getAdapter().getCount(); i++) {
                            if (checkedItems.get(i)) {
                                temp_courses.add(course_to_select.get(i));
                            }
                        }

                        try {
                            sharedPreferences.edit().putString("courses", ObjectSerializer.serialize(temp_courses)).apply();
                            time = 0;
                            difficulty = 0;
                            for(String course: temp_courses){
                                time += timetable.get(course);
                                difficulty += difftable.get(course);
                            }
                            sharedPreferences.edit().putFloat("time",time/10).apply();
                            sharedPreferences.edit().putFloat("diff",difficulty/10).apply();

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
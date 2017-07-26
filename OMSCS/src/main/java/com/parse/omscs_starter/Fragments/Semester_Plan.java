package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.omscs_starter.R;
import com.parse.omscs_starter.Support.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Semester_Plan extends Fragment {

    private Spinner course_1;
    private Spinner course_2;
    private Spinner course_3;
    private Spinner completed;
    private Spinner uncompleted;
    private Button comfirm_sem_course;
    private Button completed_button;
    private Button uncompleted_button;
    private SharedPreferences sharedPreferences;

    private ArrayList<String> selected_course;
    private ArrayList<String> completed_course;

    private ArrayList<String> current_term;

    public Semester_Plan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_semester__plan, container, false);

        course_1 = (Spinner) view.findViewById(R.id.spinner_1);
        course_2 = (Spinner) view.findViewById(R.id.spinner_2);
        course_3 = (Spinner) view.findViewById(R.id.spinner_3);
        completed = (Spinner) view.findViewById(R.id.spinner_4);
        uncompleted = (Spinner) view.findViewById(R.id.spinner_5);

        comfirm_sem_course = (Button) view.findViewById(R.id.confirm_sem_course);
        completed_button = (Button) view.findViewById(R.id.completed_button);
        uncompleted_button = (Button) view.findViewById(R.id.uncompleted_button);

        sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);

        selected_course = new ArrayList<>();
        completed_course = new ArrayList<>();


        try {
            selected_course = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("courses",
                    ObjectSerializer.serialize(new ArrayList<String>())));

            completed_course = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("completed",
                    ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, selected_course);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, selected_course);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, selected_course);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, selected_course);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        course_1.setAdapter(adapter1);
        course_2.setAdapter(adapter2);
        course_3.setAdapter(adapter3);
        completed.setAdapter(adapter4);

        if(completed_course.size() != 0){
            ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this.getActivity(),
                    android.R.layout.simple_spinner_item, completed_course);
            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            uncompleted.setAdapter(adapter5);
        }


        comfirm_sem_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_term = new ArrayList<>();

                String first = course_1.getSelectedItem().toString();
                Log.i("course",String.valueOf(first.length()));
                String second = course_2.getSelectedItem().toString();
                String third = course_3.getSelectedItem().toString();

                int count = 0;

                if(first.length()!=0) {current_term.add(first); count++;}
                if(second.length()!=0 && !first.equals(second)) {current_term.add(second);count++;}
                if(third.length()!=0 && !second.equals(third) && !first.equals(third)) {current_term.add(third);count++;}

                Toast.makeText(getActivity(), "You've selected " + String.valueOf(count) +" courses this term!", Toast.LENGTH_LONG).show();

                try {
                    sharedPreferences.edit().putString("current", ObjectSerializer.serialize(current_term)).apply();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        completed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comp = completed.getSelectedItem().toString();

                if(!Arrays.asList(completed_course).contains(comp)){
                    Log.i("added",comp);
                    completed_course.add(comp);
                }
                try {
                    sharedPreferences.edit().putString("completed", ObjectSerializer.serialize(completed_course)).apply();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, completed_course);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                uncompleted.setAdapter(adapter5);
            }
        });

        uncompleted_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uncomp = uncompleted.getSelectedItem().toString();
                if(completed_course.contains(uncomp)){
                    Log.i("added",uncomp);
                    completed_course.remove(uncomp);
                }
                try {
                    sharedPreferences.edit().putString("completed", ObjectSerializer.serialize(completed_course)).apply();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, completed_course);
                adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                uncompleted.setAdapter(adapter5);
            }
        });

        return view;


    }


}

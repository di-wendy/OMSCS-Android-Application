package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.omscs_starter.Objects.Course;
import com.parse.omscs_starter.R;
import com.parse.omscs_starter.Support.CourseDisplayAdapter;

import java.util.List;


public class CourseReview extends Fragment {

    private CourseDisplayAdapter listDataAdapter;

    public CourseReview() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        */
        ParseAnalytics.trackAppOpenedInBackground(getActivity().getIntent());

        View view = inflater.inflate(R.layout.fragment_course_review, container, false);

        ListView listview = (ListView) view.findViewById(R.id.course_review_list);
        listDataAdapter = new CourseDisplayAdapter (getActivity().getApplicationContext(), R.layout.four_col);
        listview.setAdapter(listDataAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    //Log.i("findinBackground", objects.size() + "objects");

                    if(objects.size() > 0){
                        for(ParseObject course:objects){

                            String id = course.getString("ID");
                            String name = course.getString("Name");
                            float diff = course.getNumber("Difficulty").floatValue();
                            float hour = course.getNumber("Hours").floatValue();

                            Course temp = new Course(id, name, diff, hour);
                            listDataAdapter.add(temp);
                        }
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView username = (TextView) view.findViewById(R.id.text_num);
                String message = username.getText().toString();

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("sub_course",message).apply();

                Fragment_course_INFO fragment = new Fragment_course_INFO();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

            }
        });


        return view;
    }


}

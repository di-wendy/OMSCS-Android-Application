package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.omscs_starter.R;

import java.util.List;

public class Fragment_course_INFO extends Fragment {

    private TextView courseID;
    private TextView courseName;
    private TextView summer;
    private TextView language;
    private TextView competition;
    private ImageButton goback;

    public Fragment_course_INFO() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course__info, container, false);

        courseID = (TextView)view.findViewById(R.id.course_id);
        courseName = (TextView)view.findViewById(R.id.course_name);
        summer = (TextView)view.findViewById(R.id.summer);
        language = (TextView)view.findViewById(R.id.language);
        competition = (TextView)view.findViewById(R.id.competition);

        goback = (ImageButton) view.findViewById(R.id.go_back);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);
        String select = sharedPreferences.getString("sub_course","");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Courses");
        query.whereEqualTo("ID",select);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        ParseObject course = objects.get(0);
                        courseID.setText(course.getString("ID"));
                        courseName.setText(course.getString("Name"));
                        summer.setText(course.getBoolean("Summer")?"Yes":"No");
                        language.setText(course.getString("Language"));
                        competition.setText(course.getString("competition"));

                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseReview fragment = new CourseReview();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}

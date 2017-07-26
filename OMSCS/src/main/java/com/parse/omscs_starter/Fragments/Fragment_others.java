package com.parse.omscs_starter.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.omscs_starter.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_others extends Fragment {

    private ListView listView;
    private List<String> display;
    private ArrayAdapter<String> adapter;

    public Fragment_others() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_others, container, false);
        listView = (ListView)view.findViewById(R.id.course_time_list);

        display = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedules");

        query.orderByDescending("total");

        adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, display);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(adapter);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    Log.i("test","test");
                    int i = 0;
                    if(objects.size() > 0){
                        for(ParseObject item:objects){
                            String course = item.getString("Course");
                            String name = item.getString("name");
                            int total = item.getNumber("total").intValue();
                            adapter.add(name + " "+ course + " " + String.format("%.1f",(total/3600.0)) +" hr");
                            i++;
                            if (i > 10 ){
                                break;
                            }
                        }
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}

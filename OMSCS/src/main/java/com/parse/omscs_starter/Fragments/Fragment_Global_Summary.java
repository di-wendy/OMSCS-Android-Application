package com.parse.omscs_starter.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.omscs_starter.R;


public class Fragment_Global_Summary extends Fragment {

    public Fragment_Global_Summary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__global__summary, container, false);
        Spinner view_option = (Spinner) view.findViewById(R.id.local_global);

        String[] options = new String[]{"Yourself","Others"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view_option.setAdapter(adapter);

        view_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Fragment_view_yourself fragment = new Fragment_view_yourself ();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.local_global_container, fragment);
                    fragmentTransaction.commit();
                }
                if(position == 1){
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser == null) {
                        Toast.makeText(getActivity(), "Please Sign In to View", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Fragment_others fragment = new Fragment_others();
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.local_global_container, fragment);
                        fragmentTransaction.commit();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

}

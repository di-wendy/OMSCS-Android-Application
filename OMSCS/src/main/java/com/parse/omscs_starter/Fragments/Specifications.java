package com.parse.omscs_starter.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.omscs_starter.R;

import java.util.ArrayList;

public class Specifications extends Fragment {

    private Spinner specifications;
    //private Button check;

    public Specifications() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_specifications, container, false);

        //SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);

        //String cur = sharedPreferences.getString("Specification","");

        specifications = (Spinner) view.findViewById(R.id.Specializations);
        //check = (Button) view.findViewById(R.id.check_course);

        ArrayList spec_list = new ArrayList<String>();
        spec_list.add("Computational Perception and Robotics");
        spec_list.add("Computing Systems");
        spec_list.add("Interactive Intelligence");
        spec_list.add("Machine Learning");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, spec_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specifications.setAdapter(adapter);

        specifications.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String cur = specifications.getSelectedItem().toString();

                if(cur.equals("Computational Perception and Robotics")){
                    cpRobotics fragment = new cpRobotics();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Specialization_container, fragment);
                    fragmentTransaction.commit();
                }

                if(cur.equals("Computing Systems")){
                    ComputingSystems fragment = new ComputingSystems();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Specialization_container, fragment);
                    fragmentTransaction.commit();
                }

                if(cur.equals("Interactive Intelligence")){
                    Interactive_Intelligence fragment = new Interactive_Intelligence();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Specialization_container, fragment);
                    fragmentTransaction.commit();
                }

                if(cur.equals("Machine Learning")){
                    machine_learning_frag fragment = new machine_learning_frag();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.Specialization_container, fragment);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        return view;

    }


}

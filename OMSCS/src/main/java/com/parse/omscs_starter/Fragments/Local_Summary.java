package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.omscs_starter.R;


public class Local_Summary extends Fragment {

    private TextView difficulty;
    private TextView time;
    private TextView rating;

    public Local_Summary() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local__summary, container, false);

        difficulty = (TextView) view.findViewById(R.id.difficulty);
        time = (TextView) view.findViewById(R.id.time);
        rating = (TextView) view.findViewById(R.id.Rating);


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);
        float hours = sharedPreferences.getFloat("time",0);
        float diff = sharedPreferences.getFloat("diff",0);


        double total = hours/20*0.4 + diff/5*0.6;

        time.setText(String.format("%.2f", hours));
        difficulty.setText(String.format("%.2f", diff));

        if(total > 0.7){
            rating.setText("Hell!!");
        }
        else {
            if(total >0.65){
                rating.setText("Hardworking!");
            }
            else{
                if(total > 0.6) {
                    rating.setText("Regular Guy");
                }
                else{
                    rating.setText("Just want degree");
                }
            }

        }

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageFragment fragment = new MainPageFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}

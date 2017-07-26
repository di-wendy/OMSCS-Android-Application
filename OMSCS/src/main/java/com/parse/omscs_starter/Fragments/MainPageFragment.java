package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.omscs_starter.Objects.Time_Table;
import com.parse.omscs_starter.R;
import com.parse.omscs_starter.Support.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MainPageFragment extends Fragment {

    private ArrayList<String> completed_course;
    private ArrayList<String> cur;
    private SharedPreferences sharedPreferences;
    private TextView text;

    public MainPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mainpage, container, false);
        text = (TextView) rootView.findViewById(R.id.main_page_text_view);

        cur = new ArrayList<>();
        completed_course = new ArrayList<>();

        sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);

        //boolean test = sharedPreferences.getBoolean("no_time_table", true);

        if (true) {
            try {
                //sharedPreferences.edit().putBoolean("no_time_table",false).apply();
                SQLiteDatabase db = this.getActivity().openOrCreateDatabase("db", MODE_PRIVATE, null);

                //db.execSQL("DROP TABLE " + Time_Table.TABLE);

                db.execSQL("CREATE TABLE IF NOT EXISTS " + Time_Table.TABLE + " ("
                        + Time_Table.KEY_ID + " TEXT, "
                        + Time_Table.KEY_TIME + " INTEGER DEFAULT 0, "
                        + Time_Table.KEY_DATE + " TEXT) ");

                db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            completed_course = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("completed",
                    ObjectSerializer.serialize(new ArrayList<String>())));
            cur = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("current",
                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(completed_course.size()!=0){
            text.setText(String.valueOf(completed_course.size()) + "/10");
        }

        return rootView;
    }
}

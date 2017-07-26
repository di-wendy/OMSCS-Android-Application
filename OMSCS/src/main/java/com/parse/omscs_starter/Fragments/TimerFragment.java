package com.parse.omscs_starter.Fragments;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;

import com.parse.omscs_starter.Objects.Time_Table;
import com.parse.omscs_starter.R;
import com.parse.omscs_starter.Support.ObjectSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class TimerFragment extends Fragment {

    private Button StartButton;
    private Button PauseButton;
    private Button ResetButton;
    private Chronometer chronometer;
    private Spinner CourseSelector;

    private long lastPause;

    private SharedPreferences sharedPreferences;
    private List<String> current_term;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        StartButton = (Button) view.findViewById(R.id.Start_Button);
        PauseButton = (Button) view.findViewById(R.id.Pause_Button);
        ResetButton = (Button) view.findViewById(R.id.Reset_Button);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        CourseSelector = (Spinner) view.findViewById(R.id.current_course_spinner);

        sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", MODE_PRIVATE);

        current_term = new ArrayList<>();

        try {
            current_term = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("current",
                    ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, current_term);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CourseSelector.setAdapter(adapter);


        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPause != 0) {
                    chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }

                chronometer.start();
                StartButton.setEnabled(false);
                PauseButton.setEnabled(true);
            }
        });


        PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPause = SystemClock.elapsedRealtime();
                chronometer.stop();
                PauseButton.setEnabled(false);
                StartButton.setEnabled(true);
            }
        });

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CourseSelector.getSelectedItem() != null){
                    String course = CourseSelector.getSelectedItem().toString();
                    //store is the seconds spent
                    int store = convert_string_to_second(chronometer.getText().toString());

                    //Get formatted current string
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    String strDate = format.format(calendar.getTime());

                    //Insert SQLite
                    try {
                        SQLiteDatabase db = getActivity().openOrCreateDatabase("db", MODE_PRIVATE, null);
                        db.execSQL("INSERT INTO "+ Time_Table.TABLE +
                                " (course,time,date) VALUES ( '" + course + "' , " + store +" , '" + strDate + "' )");
                    /*
                    Cursor c = db.rawQuery("SELECT * FROM " + Time_Table.TABLE, null);

                    int nameIndex = c.getColumnIndex("course");
                    int timeIndex = c.getColumnIndex("time");
                    int dateIndex = c.getColumnIndex("date");

                    if(c.moveToFirst()){
                        do{
                            Log.i("name",c.getString(nameIndex));
                            Log.i("time",c.getString(timeIndex));
                            Log.i("date",c.getString(dateIndex));
                        }while(c.moveToNext());
                    }

                    c.close();
                    */
                        db.close();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                StartButton.setEnabled(true);
                PauseButton.setEnabled(false);

            }
        });

        return view;

    }

    public int convert_string_to_second(String t) {

        int minute = Integer.valueOf(t.split(":")[0]);
        int second = Integer.valueOf(t.split(":")[1]);

        return minute*60 + second;
    }

}

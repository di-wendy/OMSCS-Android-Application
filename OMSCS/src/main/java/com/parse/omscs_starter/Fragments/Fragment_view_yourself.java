package com.parse.omscs_starter.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.omscs_starter.Objects.Time_Table;
import com.parse.omscs_starter.R;
import com.parse.omscs_starter.Support.ObjectSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class Fragment_view_yourself extends Fragment {

    private TextView today;
    private TextView yesterday;
    private List<String> current_term;
    private SharedPreferences sharedPreferences;
    private ListView listView;

    private int[] course_second;
    private boolean[] check;

    public Fragment_view_yourself() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__view_yourself, container, false);
        today = (TextView) view.findViewById(R.id.today);
        yesterday = (TextView) view.findViewById(R.id.yesterday);
        listView = (ListView) view.findViewById(R.id.course_time_list);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String strToday = format.format(calendar.getTime());
        String strYes = getYesterdayDateString();

        current_term = new ArrayList<>();

        try {
            sharedPreferences = this.getActivity().getSharedPreferences("com.parse.omscs_starter", Context.MODE_PRIVATE);
            current_term = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("current",
                    ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        course_second = new int[current_term.size()];
        String[] display = new String[current_term.size()];
        check = new boolean[current_term.size()];

        try {
            SQLiteDatabase db = getActivity().openOrCreateDatabase("db", MODE_PRIVATE, null);

            /*
            Cursor c = db.rawQuery("SELECT * FROM " + Time_Table.TABLE +
                    " WHERE date ='" + strDate +"';", null);
            */

            Cursor c = db.rawQuery("SELECT * FROM " + Time_Table.TABLE, null);

            float today_t = 0;
            float yesterday_t = 0;

            if (c.moveToFirst()) {
                do {
                    String course = c.getString(c.getColumnIndex("course"));
                    if (current_term.contains(course)) {
                        course_second[current_term.indexOf(course)] += c.getInt(c.getColumnIndex("time"));
                    }
                    if (c.getString(c.getColumnIndex("date")).equals(strToday)) {
                        today_t += c.getInt(c.getColumnIndex("time"));
                    }
                    if (c.getString(c.getColumnIndex("date")).equals(strYes)) {
                        yesterday_t += c.getInt(c.getColumnIndex("time"));
                    }
                } while (c.moveToNext());
            }

            c.close();
            db.close();

            today.setText(String.format("%.1f", (today_t / 60)));
            yesterday.setText(String.format("%.1f", (yesterday_t / 60)));

        }
        catch(Exception e){

        }

        //Update "Schedules" on AWS Server
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null){

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedules");
            query.whereEqualTo("name",currentUser.getUsername());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null){
                        if(objects.size() > 0){
                            for(ParseObject item:objects){
                                String temps = item.getString("Course");
                                if(current_term.contains(temps)){
                                    item.put("total",course_second[current_term.indexOf(temps)]);
                                    check[current_term.indexOf(temps)] = true;
                                }
                            }
                        }

                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if(currentUser.getUsername()!=null){
                            for(int i = 0; i < check.length; i++){
                                if(!check[i]) {
                                    ParseObject temp = new ParseObject("Schedules");
                                    temp.put("Course", current_term.get(i));
                                    temp.put("name", currentUser.getUsername());
                                    temp.put("total", course_second[i]);
                                    temp.saveInBackground();
                                }
                            }
                        }
                    }
                    else{
                        e.printStackTrace();
                    }
                }
            });
        }

        for(int i = 0; i < display.length; i++){
            display[i] = current_term.get(i) + "   " + String.format("%.1f",(course_second[i]/3600.0)) +" hr";

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, display);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(adapter);

        return view;
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private String getYesterdayDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(yesterday());
    }


}

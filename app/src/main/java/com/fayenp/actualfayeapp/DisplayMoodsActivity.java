package com.fayenp.actualfayeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayMoodsActivity extends AppCompatActivity {
    public static final String MENTAL_SHARED = "com.fayenp.actualfayeapp.mentalSharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_moods);
        //read data from shared pref for each number from 1-7 (end at current day)
        Calendar cal = Calendar.getInstance();
        TextView d1 = (TextView)findViewById(R.id.moodday1);
        TextView d2 = (TextView)findViewById(R.id.moodday2);
        TextView d3 = (TextView)findViewById(R.id.moodday3);
        TextView d4 = (TextView)findViewById(R.id.moodday4);
        TextView d5 = (TextView)findViewById(R.id.moodday5);
        TextView d6 = (TextView)findViewById(R.id.moodday6);
        TextView d7 = (TextView)findViewById(R.id.moodday7);
        TextView m1 = (TextView)findViewById(R.id.mood1);
        TextView m2 = (TextView)findViewById(R.id.mood2);
        TextView m3 = (TextView)findViewById(R.id.mood3);
        TextView m4 = (TextView)findViewById(R.id.mood4);
        TextView m5 = (TextView)findViewById(R.id.mood5);
        TextView m6 = (TextView)findViewById(R.id.mood6);
        TextView m7 = (TextView)findViewById(R.id.mood7);
        TextView[] dates = {d1,d2,d3,d4,d5,d6,d7};
        TextView[] moods = {m1,m2,m3,m4,m5,m6,m7};
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        cal.add(Calendar.DATE, -7);
        SharedPreferences sharedPreferences = getSharedPreferences(MENTAL_SHARED, Context.MODE_PRIVATE);
        int totalPoints = 0;
        int totalDays = 0;
        for (int i = 0; i<7; i++) {
            cal.add(Calendar.DATE, 1);
            String mood = sharedPreferences.getString(dateFormat.format(cal.getTime()), "Check your mood in the past week");
            totalDays++;
            if (mood.equals("Check your mood in the past week")) {
                mood = "No mood recorded";
                totalDays--;
            } else if (mood.equals("Fantastic")) {
                totalPoints += 5;
            } else if (mood.equals("Pretty good")) {
                totalPoints += 4;
            } else if (mood.equals("In between")) {
                totalPoints += 3;
            } else if (mood.equals("A bit down")) {
                totalPoints += 2;
            } else if (mood.equals("Having a rough day")) {
                totalPoints += 1;
            }
            dates[i].setText(dateFormat.format(cal.getTime()));
            moods[i].setText(mood);
        }
        ProgressBar pgb = (ProgressBar) this.findViewById(R.id.moodProgress);
        if (totalDays==0){
            pgb.setProgress(0);
        }else{
            pgb.setProgress((20*totalPoints)/totalDays);
        }

    }
}
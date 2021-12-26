package com.example.actualfayeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayMoodsActivity extends AppCompatActivity {
    public static final String MENTAL_SHARED = "mentalSharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_moods);
        //read data from shared pref for each number from 1-7 (end at current day)
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences sharedPreferences  = this.getSharedPreferences(MENTAL_SHARED, this.MODE_PRIVATE);
        TextView[] views = {};
        int totalPoints = 0;
        int totalDays = 0;
        for (int i = 0; i<7; i++){
            cal.add(Calendar.DATE, -1);
            String mood = sharedPreferences.getString(dateFormat.format(cal.getTime()), "");
            totalDays++;
            if (mood.equals("")){
                mood = "No mood recorded";
                totalDays--;
            }else if (mood.equals("Fantastic")){
                totalPoints+= 5;
            }else if (mood.equals("Pretty good")){
                totalPoints+= 4;
            }else if (mood.equals("In between")){
                totalPoints+= 3;
            }else if (mood.equals("A bit down")){
                totalPoints+= 2;
            }else if (mood.equals("Having a rough day")){
                totalPoints+= 1;
            }
        }
        ProgressBar pgb = (ProgressBar) this.findViewById(R.id.moodProgress);
        pgb.setProgress(30);
        //pgb.setProgress(totalPoints/(totalDays*5));
        //display days of week + mood for past 7 days

    }
}
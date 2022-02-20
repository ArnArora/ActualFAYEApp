package com.example.actualfayeapp.ui.mentalhealth;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.actualfayeapp.DisplayMoodsActivity;
import com.example.actualfayeapp.R;
import com.example.actualfayeapp.ui.mentalhealth.MentalHealthViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MentalHealthFragment extends Fragment implements View.OnClickListener{
    private MentalHealthViewModel mentalHealthViewModel;
    public final String MENTAL_SHARED = "com.example.actualfayeapp.mentalSharedPrefs";
    public View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mentalHealthViewModel =
                new ViewModelProvider(this).get(MentalHealthViewModel.class);
        root = inflater.inflate(R.layout.fragment_mentalhealth, container, false);
        Button b = (Button) root.findViewById(R.id.quote);
        b.setOnClickListener(this);
        b = (Button) root.findViewById(R.id.mentaltip);
        b.setOnClickListener(this);
        b = (Button) root.findViewById(R.id.see_results);
        b.setOnClickListener(this);
        Button b1 = (Button) root.findViewById(R.id.bad1);
        Button b2 = (Button) root.findViewById(R.id.bad2);
        Button b3 = (Button) root.findViewById(R.id.pg);
        Button b4 = (Button) root.findViewById(R.id.fan);
        Button b5 = (Button) root.findViewById(R.id.inbet);
        Button[] buttons = {b1, b2, b3, b4, b5};
        for (Button item: buttons){
            item.setOnClickListener(this);
        }
        Context context = getActivity();
        String today = getDate();
        SharedPreferences sharedPreferences = context.getSharedPreferences(MENTAL_SHARED, Context.MODE_PRIVATE);
        String checking = sharedPreferences.getString(today, "Check your mood in the past week");
        if (!(checking.equals("Check your mood in the past week"))){
            for (Button item: buttons){
                item.setBackgroundColor(Color.GRAY);
                item.setEnabled(false);
            }
        }
        final TextView textView = root.findViewById(R.id.mental_title);
        mentalHealthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    public String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(cal.getTime());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quote:
                loadQuote(v);
                break;
            case R.id.mentaltip:
                loadMental(v);
                break;
            case R.id.see_results:
                displayMoods(v);
                break;
            case R.id.bad1:
                chooseMood(v, 1);
                break;
            case R.id.pg:
                chooseMood(v, 2);
                break;
            case R.id.bad2:
                chooseMood(v, 3);
                break;
            case R.id.fan:
                chooseMood(v, 4);
                break;
            case R.id.inbet:
                chooseMood(v, 5);
                break;
        }
    }
    public void displayMoods(View v){
        //launch new activity
        Context context = getActivity();
        Intent intent = new Intent(context, DisplayMoodsActivity.class);
        startActivity(intent);
    }
    public void chooseMood(View v, int num) {
        Button b = null;
        switch (num){
            case 1:
                b = (Button) root.findViewById(R.id.bad1);
                break;
            case 2:
                b = (Button) root.findViewById(R.id.pg);
                break;
            case 3:
                b = (Button) root.findViewById(R.id.bad2);
                break;
            case 4:
                b = (Button) root.findViewById(R.id.fan);
                break;
            case 5:
                b = (Button) root.findViewById(R.id.inbet);
                break;
        }
        Context context = getActivity();
        b.setTextColor(Color.BLUE);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        SharedPreferences sharedPreferences = context.getSharedPreferences(MENTAL_SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //update change
        editor.putString(dateFormat.format(cal.getTime()), b.getText().toString());
        editor.commit();
        b.setTextColor(Color.GREEN);
        b.setEnabled(false);
        //disable other buttons
        Button b1 = (Button) root.findViewById(R.id.bad1);
        Button b2 = (Button) root.findViewById(R.id.bad2);
        Button b3 = (Button) root.findViewById(R.id.pg);
        Button b4 = (Button) root.findViewById(R.id.fan);
        Button b5 = (Button) root.findViewById(R.id.inbet);
        Button[] buttons = {b1, b2, b3, b4, b5};
        for (Button item: buttons){
            if (item.isEnabled()){
                item.setBackgroundColor(Color.GRAY);
                item.setEnabled(false);
            }
        }
    }
    public void loadQuote(View v){
        Context context = getActivity();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        BufferedReader reader = null;
        String tip = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("quotes.txt")));
            // do reading, usually loop until end of file reading
            for (int i = 0; i<(day-1)%102; i++){
                reader.readLine();
            }
            tip = reader.readLine();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        Button b = (Button) v;
        b.setEnabled(false);
        b.setText(tip);
    }
    public void loadMental(View v){
        Context context = getActivity();
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        BufferedReader reader = null;
        String tip = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("mentalhealth.txt")));
            // do reading, usually loop until end of file reading
            for (int i = 0; i<(day-1)%20; i++){
                reader.readLine();
            }
            tip = reader.readLine();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        Button b = (Button) v;
        b.setEnabled(false);
        b.setText(tip);
    }
}

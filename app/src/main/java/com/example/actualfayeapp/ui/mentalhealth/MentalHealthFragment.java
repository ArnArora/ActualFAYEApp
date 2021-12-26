package com.example.actualfayeapp.ui.mentalhealth;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class MentalHealthFragment extends Fragment{
    private MentalHealthViewModel mentalHealthViewModel;
    public static final String MENTAL_SHARED = "mentalSharedPrefs";
    public View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mentalHealthViewModel =
                new ViewModelProvider(this).get(MentalHealthViewModel.class);
        root = inflater.inflate(R.layout.fragment_mentalhealth, container, false);
        final TextView textView = root.findViewById(R.id.mental_title);
        mentalHealthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    public void displayMoods(View v){
        //launch new activity
        Context context = getActivity();
        Intent intent = new Intent(context, DisplayMoodsActivity.class);
        startActivity(intent);
    }
    public void chooseMood(View v) {
        Context context = getActivity();
        Button b = (Button) v;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences sharedPreferences = context.getSharedPreferences(MENTAL_SHARED, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //update change
        editor.putString(dateFormat.format(cal.getTime()), b.getText().toString());
        b.setBackgroundColor(304903);
        b.setEnabled(false);
        editor.apply();
        //disable other buttons
        Button b1 = (Button) root.findViewById(R.id.bad1);
        Button b2 = (Button) root.findViewById(R.id.bad2);
        Button b3 = (Button) root.findViewById(R.id.pg);
        Button b4 = (Button) root.findViewById(R.id.fan);
        Button b5 = (Button) root.findViewById(R.id.inbet);
        Button[] buttons = {b1, b2, b3, b4, b5};
        for (Button item: buttons){
            if (item.isEnabled()){
                item.setEnabled(false);
            }
        }
    }
    public void loadQuote(View v){
        Context context = getActivity();
        Calendar cal = Calendar.getInstance();
        int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
        BufferedReader reader = null;
        String tip = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("quotes.txt")));
            // do reading, usually loop until end of file reading
            for (int i = 0; i<(29*dayofMonth-1)%30; i++){
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
        int dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
        BufferedReader reader = null;
        String tip = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("mentalhealth.txt")));
            // do reading, usually loop until end of file reading
            for (int i = 0; i<17*(dayofMonth-1)%30; i++){
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

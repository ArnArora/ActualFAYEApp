package com.example.actualfayeapp.ui.studytips;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.actualfayeapp.R;
import com.example.actualfayeapp.ui.studytips.StudytipsViewModel;

public class StudytipsFragment extends Fragment{
    private StudytipsViewModel studytipsViewModel;
    private static final String SHARED_PREFS = "sharedPrefs";
    private int dayofMonth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        studytipsViewModel =
                new ViewModelProvider(this).get(StudytipsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_studytips, container, false);
        final TextView textView = root.findViewById(R.id.studytipstitle);
        studytipsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    public void loadTip(View v){
        Context context = getActivity();

        Calendar cal = Calendar.getInstance();
        dayofMonth = cal.get(Calendar.DAY_OF_MONTH);
        BufferedReader reader = null;
        String tip = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("tipsofday.txt")));
            // do reading, usually loop until end of file reading
            for (int i = 0; i<dayofMonth-1; i++){
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
        Button tod = (Button) v.findViewById(R.id.todcontent);
        tod.setEnabled(false);
        tod.setText(tip);
    }

}

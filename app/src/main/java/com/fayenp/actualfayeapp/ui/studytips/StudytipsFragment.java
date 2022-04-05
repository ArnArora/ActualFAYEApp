package com.fayenp.actualfayeapp.ui.studytips;
import android.content.Context;
import android.graphics.Color;
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

import com.fayenp.actualfayeapp.R;

public class StudytipsFragment extends Fragment implements View.OnClickListener {
    private StudytipsViewModel studytipsViewModel;
    private static final String SHARED_PREFS = "sharedPrefs";
    private int dayofMonth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        studytipsViewModel =
                new ViewModelProvider(this).get(StudytipsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_studytips, container, false);
        Button b = (Button) root.findViewById(R.id.todcontent);
        b.setOnClickListener(this);
        TextView pom = (TextView) root.findViewById(R.id.pomodoro_top);
        TextView tod = (TextView) root.findViewById(R.id.todtitle);
        pom.setTextColor(Color.BLACK);
        tod.setTextColor(Color.BLACK);
        final TextView textView = root.findViewById(R.id.studytipstitle);
        studytipsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {
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
        Button cur = (Button) v;
        cur.setEnabled(false);
        cur.setText(tip);
    }
}

package com.example.actualfayeapp.ui.stats;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.actualfayeapp.R;
import com.example.actualfayeapp.ui.stats.StatsViewModel;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatsFragment extends Fragment implements View.OnClickListener {
    private StatsViewModel statsViewModel;
    public final String STATS_SHARED = "com.example.actualfayeapp.statsSharedPref";
    private static final DecimalFormat df = new DecimalFormat("#0.00");
    TextView hours, enterTitle;
    Button add, remove, select;
    Context context;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        statsViewModel =
                new ViewModelProvider(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        enterTitle = (TextView) root.findViewById(R.id.enterHoursTwo);
        enterTitle.setText("Enter how many hours you have studied today");
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(STATS_SHARED, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int toPut = sharedPrefs.getInt(getDate(), 0);
        hours = (TextView) root.findViewById(R.id.hoursStudied);
        hours.setText(Integer.toString(toPut));
        add = (Button) root.findViewById(R.id.addHour);
        add.setOnClickListener(this);
        remove = (Button) root.findViewById(R.id.subtractHour);
        remove.setOnClickListener(this);
        select = (Button) root.findViewById(R.id.enterHours);
        select.setOnClickListener(this);
        //show bar graph
        HorizontalBarChart chart = (HorizontalBarChart) root.findViewById(R.id.hoursChart);
        BarDataSet bds = getData();
        ArrayList<Integer> colors=  new ArrayList<Integer>();
        colors.add(Color.RED);colors.add(Color.YELLOW);colors.add(Color.GREEN);colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);colors.add(Color.GRAY);colors.add(Color.BLACK);
        bds.setColors(colors);
        BarData data = new BarData(bds);
        data.setBarWidth(0.5f);
        chart.setData(data);
        //set x-axis
        ArrayList<String> xVals=  getX();
        XAxis x = chart.getXAxis();
        x.setTextSize(12f);
        x.setValueFormatter(new IndexAxisValueFormatter(xVals));
        Description des = new Description();
        des.setText("Hours Studied Each Day for Last Week");
        chart.setDescription(des);
        chart.animateXY(2000, 2000);
        chart.invalidate();
        //get average of past 7 days
        int totalHours = 0;
        int totalDays = 0;
        int[] days = getPastWeek();
        for (int i = 0; i<7; i++) {
            totalDays++;
            if (days[i]==0){
                totalDays--;
            }else{
                totalHours += days[i];
            }
        }
        TextView showAvg = (TextView) root.findViewById(R.id.averageStudy);
        if (totalDays==0){
            showAvg.setText("Your average cannot be displayed as you have not entered data in the past 7 days.");
        }else{
            double avg = (1.0*totalHours)/totalDays;
            showAvg.setText("You have studied for a daily average of "+df.format(avg)+" hours in the past week.");
        }
//        final TextView textView = root.findViewById(R.id.enterHoursTitle);
//        statsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    //arr[0] gives seven days ago (i.e. goes from past to present)
    public int[] getPastWeek(){
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATS_SHARED, context.MODE_PRIVATE);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        cal.add(Calendar.DATE, -7);
        int[] past = new int[7];
        for (int i = 0; i<7; i++){
            int onThis = sharedPrefs.getInt(dateFormat.format(cal.getTime()), -1);
            past[i] = Math.max(0, onThis);
            cal.add(Calendar.DATE, 1);
        }
        return past;
    }
    public ArrayList<String> getX(){
        ArrayList<String> xVals = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        cal.add(Calendar.DATE, -7);
        for (int i = 0; i<7; i++){
            xVals.add(dateFormat.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
        return xVals;
    }
    public BarDataSet getData(){
        ArrayList<BarEntry> vset = new ArrayList<BarEntry>();
        int[] days = getPastWeek();
        int max = 0;
        for (int item: days){
            max = Math.max(item, max);
        }
        max /= 6;
        if (max%6!=0){
            max++;
        }
        for (int i = 0; i<7; i++){
            vset.add(new BarEntry(i, days[i]));
        }
        BarDataSet bds = new BarDataSet(vset, "Hours Studied");
        return bds;
    }
    public String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(cal.getTime());
    }
    public void addHour(){
        //update TextView
        int curHours = Integer.parseInt(hours.getText().toString());
        if (curHours>=0){
            remove.setEnabled(true);
        }
        curHours++;
        if (curHours>=24){
            curHours=24;
            add.setEnabled(false);
        }
        hours.setText(Integer.toString(curHours));
    }
    public void removeHour(){
        //update TextView
        int curHours = Integer.parseInt(hours.getText().toString());
        if (curHours<=24){
            add.setEnabled(true);
        }
        curHours--;
        if (curHours<=0){
            curHours = 0;
            remove.setEnabled(false);
        }
        hours.setText(Integer.toString(curHours));
    }
    public void enterHours(View v){
        //update SharedPreferences
        int curHours = Integer.parseInt(hours.getText().toString());
        SharedPreferences sharedPreferences = context.getSharedPreferences(STATS_SHARED, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getDate(), curHours);
        editor.commit();
        Toast.makeText(v.getRootView().getContext(), "Entered", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addHour:
                addHour();
                break;
            case R.id.subtractHour:
                removeHour();
                break;
            case R.id.enterHours:
                enterHours(v);
                break;
        }
    }
}

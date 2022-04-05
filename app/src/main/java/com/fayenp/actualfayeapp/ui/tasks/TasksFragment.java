package com.fayenp.actualfayeapp.ui.tasks;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fayenp.actualfayeapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TasksFragment extends Fragment implements View.OnClickListener, TaskAdapter.OnTaskListener {
    private TasksViewModel tasksViewModel;
    public final String TASKS_SHARED = "com.fayenp.actualfayeapp.tasksSharedPref";
    RecyclerView list;
    TaskAdapter ta;
    ArrayList<String> tasks;
    String curDate;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        Context context = getActivity();
        //initialize recyclerview
        list = (RecyclerView) root.findViewById(R.id.taskList);
        tasks = new ArrayList<String>();
        curDate = getDate();
        SharedPreferences sharedPrefs = context.getSharedPreferences(TASKS_SHARED, Context.MODE_PRIVATE);
        Set<String> set = sharedPrefs.getStringSet(curDate, null);
        if (set!=null){
            tasks.addAll(set);
        }
        ta = new TaskAdapter(context, tasks, this);
        list.setAdapter(ta);
        list.setLayoutManager(new LinearLayoutManager(context));
        //initialize other buttons
        Button b = (Button) root.findViewById(R.id.selectDate);
        b.setOnClickListener(this);
        b = (Button) root.findViewById(R.id.addTask);
        b.setOnClickListener(this);
        EditText et = (EditText) root.findViewById(R.id.chooseDate);
        et.setError(null);
        et = (EditText) root.findViewById(R.id.addTaskDate);
        et.setError(null);
        TextView textView = root.findViewById(R.id.taskTitle);
        tasksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
    public String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(cal.getTime());
    }
    public void addT(View v){
        EditText et = (EditText) getView().findViewById(R.id.addTaskDate);
        String newTask = et.getText().toString();
        Set<String> set = new HashSet<String>();
        tasks.add(newTask);
        set.addAll(tasks);
        ta.addTask(newTask);
        Context context = getActivity();
        SharedPreferences sharedPrefs = context.getSharedPreferences(TASKS_SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putStringSet(curDate, set);
        editor.commit();
        et.setText("");
        Toast.makeText(v.getRootView().getContext(), "Task added", Toast.LENGTH_LONG).show();
    }

    public void select(View v){
        EditText ed = (EditText) getView().findViewById(R.id.chooseDate);
        String input = ed.getText().toString();
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //verify date
        try {
            Date cur = dateFormat.parse(input);
            cal2.setTime(cur);
            int month = cal2.get(Calendar.DAY_OF_YEAR);
            int year = cal2.get(Calendar.YEAR);
            int curMonth = cal.get(Calendar.DAY_OF_YEAR);
            int curYear = cal.get(Calendar.YEAR);
            if (year<curYear) {
                ed.setError("Please enter a year during or after "+curYear);
                return;
            }else if (year==curYear) {
                if (month<curMonth) {
                    ed.setError("Please enter a date during or after today");
                    return;
                }
            }
        } catch (ParseException e) {
            ed.setError("Please enter in the format MM/dd/yyyy");
            return;
        }
        ed.setError(null);
        //load tasks for that date
        curDate = dateFormat.format(cal2.getTime());
        TextView listTitle = (TextView) getView().findViewById(R.id.taskListTitle);
        listTitle.setText("Tasks for "+curDate);
        Context context = getActivity();
        SharedPreferences sharedPrefs = context.getSharedPreferences(TASKS_SHARED, Context.MODE_PRIVATE);
        Set<String> set = sharedPrefs.getStringSet(curDate, null);
        ArrayList<String> updatedTasks = new ArrayList<>();
        if (set!=null){
            updatedTasks.addAll(set);
        }
        tasks = updatedTasks;
        ta.changeTasks(updatedTasks);
    }
    @Override
    public void onClick(View v) {
        //select, add, remove, load tasks
        switch (v.getId()){
            case R.id.selectDate:
                select(v);
                break;
            case R.id.addTask:
                addT(v);
                break;
        }
    }
    @Override
    public void onTaskClick(int position) {
        //given position clicked, remove item
        tasks.remove(position);
        //Retrieve
        //Set
        Context context = getActivity();
        SharedPreferences sharedPrefs = context.getSharedPreferences(TASKS_SHARED, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Set<String> set = new HashSet<String>();
        if (tasks!=null){
            set.addAll(tasks);
        }else{
            set = null;
        }
        editor.putStringSet(curDate, set);
        editor.commit();
        ta.removeTask(position);
    }
}

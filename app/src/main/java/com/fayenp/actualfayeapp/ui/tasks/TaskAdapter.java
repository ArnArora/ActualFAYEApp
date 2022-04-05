package com.fayenp.actualfayeapp.ui.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fayenp.actualfayeapp.R;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    Context context;
    private ArrayList<String> tasks;
    private OnTaskListener otl;
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public TaskAdapter(Context ct, ArrayList<String> arr, OnTaskListener otl2){
        context = ct;
        tasks = arr;
        otl = otl2;
    }
    public void addTask(String newTask){
        notifyDataSetChanged();
    }
    public void removeTask(int position){
        notifyDataSetChanged();
        notifyItemRangeChanged(position, tasks.size());
    }
    public void changeTasks(ArrayList<String> newTasks){
        tasks = newTasks;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, tasks.size());
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_row, parent, false);
        return new TaskViewHolder(view, otl);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String curTask = tasks.get(position);
        holder.task.setText(curTask);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView task;
        Button remove;
        OnTaskListener onTask;
        public TaskViewHolder(@NonNull View itemView, OnTaskListener onTaskListen) {
            super(itemView);
            task = itemView.findViewById(R.id.taskName);
            remove = itemView.findViewById(R.id.removeTask);
            onTask = onTaskListen;
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTask.onTaskClick(getAdapterPosition());
        }
    }
    public interface OnTaskListener{
        void onTaskClick(int position);
    }

}

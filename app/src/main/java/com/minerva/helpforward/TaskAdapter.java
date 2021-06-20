package com.minerva.helpforward;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.inflater = LayoutInflater.from(context);
        this.tasks = tasks;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.taskTitle.setText(task.getName());

        if (task.getDescription().length() <= 80){
            holder.taskDescription.setText(task.getDescription());
        } else {
            holder.taskDescription.setText(task.getDescription().substring(0, 80) + "...");
        }
//        holder.taskDescription.setText(task.getDescription());
        holder.showInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.information_task_dialog, null);

                TextView title = view.findViewById(R.id.title_task_text);
                TextView description = view.findViewById(R.id.description_task_text);

                title.setText(task.getName());
                description.setText(task.getDescription());

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext())
                        .setView(view)
                        .setNeutralButton("Show map", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Log.d("MAP", "I'll show you da way to the map! But later...");
                                TasksFragment.setTask(task);
                                HomeActivity.setChoosedItem(R.id.navigation_map);

                            }
                        }).setCancelable(true)
                        .setNegativeButton("Hide", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                builder.create().show();
            }
        });
        holder.showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksFragment.setTask(task);
                HomeActivity.setChoosedItem(R.id.navigation_map);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView taskTitle, taskDescription;
        final Button showMapButton, showInfoButton;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            taskTitle = (TextView) itemView.findViewById(R.id.task_title);
            taskDescription = (TextView) itemView.findViewById(R.id.task_description);
            showInfoButton = (Button) itemView.findViewById(R.id.show_info_button);
            showMapButton = (Button) itemView.findViewById(R.id.show_map_button);
        }
    }
}

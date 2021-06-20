package com.minerva.helpforward;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.lang.String;

public class TasksViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<Task> currentTask = new MutableLiveData<Task>();

    public TasksViewModel() {
        currentTask.setValue(null);//new Task(null, null, 0.0f, 0.0f, 0));
    }


    public LiveData<Task> getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task task){
        currentTask.setValue(task);
    }
}
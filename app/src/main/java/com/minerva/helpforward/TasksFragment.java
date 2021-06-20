package com.minerva.helpforward;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    public TasksFragment() {
    }

    private static TasksViewModel mViewModel;

    private IsCompanyViewModel mCompanyViewModel;
    private boolean isCompany;

    private MapView mapFragment;
    private GoogleMap map;

    RecyclerView recyclerView;
    FirebaseDatabase _db = FirebaseDatabase.getInstance();
    FloatingActionButton addTaskButton;

    ArrayList<Task> tasks = new ArrayList<Task>();
    TaskAdapter tasksAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        addTaskButton = (FloatingActionButton) rootView.findViewById(R.id.floating_add_task);
        mCompanyViewModel = new ViewModelProvider(getActivity()).get(IsCompanyViewModel.class);
        mCompanyViewModel.getIsCompany().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isCompany = aBoolean;
                if (isCompany) {
                    addTaskButton.setVisibility(View.VISIBLE);
                } else {
                    addTaskButton.setVisibility(View.GONE);
                }
            }
        });
        DatabaseReference reference = _db.getReference().child("tasks");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                tasks.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    tasks.add(snap.getValue(Task.class));
                }
                tasksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tasks_recycler_view);

        tasksAdapter = new TaskAdapter(getContext(), tasks);
        recyclerView.setAdapter(tasksAdapter);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTaskActivity.class));
            }
        });

        return rootView;
    }

    public static void setTask(Task task){
        mViewModel.setCurrentTask(task);
        Log.d("LIVE_DATA", mViewModel.getCurrentTask().getValue().toString());
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);
    }
}


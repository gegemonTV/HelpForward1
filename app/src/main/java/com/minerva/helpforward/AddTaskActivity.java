package com.minerva.helpforward;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AddTaskActivity extends AppCompatActivity {
    /*_db.getReference("tasks").child(taskName.getText().toString()).child("description").setValue(taskDescription.getText().toString());
                            _db.getReference("tasks").child(taskName.getText().toString()).child("gems").setValue(Long.parseLong(taskPrice.getText().toString()));
                            _db.getReference("tasks").child(taskName.getText().toString()).child("lat").setValue(markerLatLng[0].latitude);
                            _db.getReference("tasks").child(taskName.getText().toString()).child("lon").setValue(markerLatLng[0].longitude);
                            _db.getReference("tasks").child(taskName.getText().toString()).child("name").setValue(taskName.getText().toString());*/
    FirebaseDatabase _db = FirebaseDatabase.getInstance();
    GoogleMap map;
    SupportMapFragment mapFragment;
    LatLng taskMarkerLL;
    EditText taskName, taskDescription, taskPrice;
    Button addTaskButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskName = findViewById(R.id.task_name_add);
        taskDescription = findViewById(R.id.task_description_add);
        taskPrice = findViewById(R.id.task_price_add);
        addTaskButton = findViewById(R.id.add_task_button);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_add_task);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                }
                map.setMyLocationEnabled(true);
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull @NotNull LatLng latLng) {
                        map.clear();
                        taskMarkerLL = latLng;
                        map.addMarker(new MarkerOptions().position(taskMarkerLL));
                    }
                });
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskMarkerLL == null){
                    Snackbar.make(v, "Set the marker on map", Snackbar.LENGTH_LONG).show();
                }else if (taskDescription.getText().toString() == ""){
                    Snackbar.make(v, "Set correct task description", Snackbar.LENGTH_LONG).show();
                }else if (taskName.getText().toString() == ""){
                    Snackbar.make(v, "Set correct task name", Snackbar.LENGTH_LONG).show();
                }else if (taskPrice.getText().toString() == ""){
                    Snackbar.make(v, "Set correct task price", Snackbar.LENGTH_LONG).show();
                } else {
                    _db.getReference("tasks").child(taskName.getText().toString()).child("description").setValue(taskDescription.getText().toString());
                    _db.getReference("tasks").child(taskName.getText().toString()).child("gems").setValue(Long.parseLong(taskPrice.getText().toString()));
                    _db.getReference("tasks").child(taskName.getText().toString()).child("lat").setValue(taskMarkerLL.latitude);
                    _db.getReference("tasks").child(taskName.getText().toString()).child("lon").setValue(taskMarkerLL.longitude);
                    _db.getReference("tasks").child(taskName.getText().toString()).child("name").setValue(taskName.getText().toString());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        });

    }
}
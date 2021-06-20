package com.minerva.helpforward;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.lang.String;

public class MapFragment extends Fragment {


    private static final int PERMISSION_ID = 44;
    private GoogleMap map;

    private SupportMapFragment mapView;

    private TasksViewModel mViewModel;

    private FirebaseDatabase _db = FirebaseDatabase.getInstance();

    private DatabaseReference _ref = _db.getReference().child("tasks");

    private ArrayList<MarkerOptions> markers = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;


    private LocationManager mLocationManager;

    private double latitude, longitude;

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(TasksViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mapView = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                _ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        googleMap.clear();
                        markers.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            LatLng coordinates = new LatLng((double) snap.child("lat").getValue(), (double) snap.child("lon").getValue());

                            MarkerOptions marker = new MarkerOptions().position(coordinates).title((String) snap.child("name").getValue());

                            markers.add(marker);
                            googleMap.addMarker(marker);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Log.d("ERRMAP", error.getMessage());
                    }
                });
                getLastLocation();
                final LatLng[] t = new LatLng[]{new LatLng(latitude, longitude)};
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                    return;
                }
                map.setMyLocationEnabled(true);
                mViewModel.getCurrentTask().observe(getViewLifecycleOwner(), new Observer<Task>() {
                    @Override
                    public void onChanged(Task task) {
                        if (!(task == null)){
                            t[0] = new LatLng(task.getLat(), task.getLon());
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(t[0],16, 0, 0)));
                        }
                        else{
                            getLastLocation();
                            t[0] = new LatLng(latitude, longitude);
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(t[0],16, 0, 0)));
                            Log.d("MAP", "Lat now is " + t[0].latitude);
                        }
                    }
                });



            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static MapFragment newInstance(double lat, double lon) {
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putDouble("lat", lat);
        args.putDouble("lon", lon);
        mapFragment.setArguments(args);
        return mapFragment;
    }

    private void loadMapScene(){

        /*platformPositioningProvider.startLocating(new PlatformPositioningProvider.PlatformLocationListener() {
            @Override
            public void onLocationUpdated(android.location.Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
                Log.d("Location", currentLat + " " + currentLon);
            }
        });*/
        /*mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable @org.jetbrains.annotations.Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null){
                    if (t[0] == null){
                        mapView.getCamera().setTarget(new GeoCoordinates(currentLat, currentLon));
                        Log.d("TASK_DATA", String.valueOf(currentLat));
                        mapView.getCamera().setZoomLevel(6);
                    } else {
                        setCameraTarget(t[0].getLat(), t[0].getLon());
                    }
                }else{
                    Log.d("MAP", "onLoadScene failed: " + errorCode.toString());
                }
            }
        });*/



    }

    @SuppressLint("MissingPermission")

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<Location> task) {
                        Location location = task.getResult();

                        if (location == null) {

                            requestNewLocationData();

                        }

                        else {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 16, 0, 0)));
                        }

                    }

                });


            } else {
               Toast.makeText(getContext(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        }

        else {
            requestPermissions();

        }

    }
    @SuppressLint("MissingPermission")

    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setInterval(5);

        mLocationRequest.setFastestInterval(0);

        mLocationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            latitude = mLastLocation.getLatitude();

            longitude = mLastLocation.getLongitude();
        }
    };

    private boolean checkPermissions() {

        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLastLocation();

            }
        }
    }

    @Override

    public void onResume() {
        super.onResume();

        if (checkPermissions()) {

            getLastLocation();

        }

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

}

package com.drumond.ubiquouscomputing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.os.ResultReceiver;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Location variables
    private static final int REQUEST_CODE_PERMISSION = 1;
    private ProgressBar pbLoading;
    private TextView tvLatitude, tvLongitude, tvAltitude;
    private String latitudeTemp, LongitudeTemp, altitudeTemp;
    private ResultReceiver resultReceiver;
    //ExpandableListView variables
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildList();
        manipulateExpandableListView();
        pbLoading = findViewById(R.id.pb_loading);
    }

    public void manipulateExpandableListView() {
        ExpandableListView expandableListView = findViewById(R.id.elv_expandableListView);
        expandableListView.setAdapter(new ExpandableAdapter(MainActivity.this, listGroup, listData));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(MainActivity.this, "Group: " + groupPosition + " | Item: " + childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Notification message variables
                String on = getResources().getString(R.string.on);
                String accelerometer = getResources().getString(R.string.elv_h_accelerometer);
                    if (groupPosition == 0) {
                        //Request permission
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
                            Toast.makeText(MainActivity.this, "GPS " + on, Toast.LENGTH_SHORT).show();
                        } else {
                            getCurrentLocation();
                        }
                    } else if (groupPosition == 1)
                        Toast.makeText(MainActivity.this, accelerometer + " " + on, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Notification message
                String off = getResources().getString(R.string.off);
                String accelerometer = getResources().getString(R.string.elv_h_accelerometer);
                if (groupPosition == 0)
                Toast.makeText(MainActivity.this, "GPS " + off, Toast.LENGTH_SHORT).show();
                else if (groupPosition == 1)
                    Toast.makeText(MainActivity.this, accelerometer + " " + off, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setGroupIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_group, null));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        pbLoading.setVisibility(View.VISIBLE);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            double altitude = locationResult.getLocations().get(latestLocationIndex).getAltitude();
                            tvLatitude.setText(String.valueOf(latitude));
                            tvLongitude.setText(String.valueOf(longitude));
                            tvAltitude.setText(String.valueOf(altitude));

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatidudeLongitude(location);

                        } else {
                            pbLoading.setVisibility(View.GONE);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatidudeLongitude(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    //Load Expandable list View Resource values
    public void buildList() {
        listGroup = new ArrayList<>();
        listData = new HashMap<>();

        //Resources
        String location = getResources().getString(R.string.elv_h_location);
        String accelerometer = getResources().getString(R.string.elv_h_accelerometer);
        String deviceSensors = getResources().getString(R.string.elv_h_devicesensors);

        String latitude = getResources().getString(R.string.elv_i_latitude);
        String longitude = getResources().getString(R.string.elv_i_longitude);
        String altitude = getResources().getString(R.string.elv_i_altitude);
        String address = getResources().getString(R.string.elv_i_address);

        String xAxis = getResources().getString(R.string.elv_i_xaxis);
        String yAxis = getResources().getString(R.string.elv_i_yaxis);
        String zAxis = getResources().getString(R.string.elv_i_zaxis);

        String sensorsList = getResources().getString(R.string.elv_i_sensorslist);

        String valueLatitude = getResources().getString(R.string.valueLatitude);
        String valueLongitude = getResources().getString(R.string.valueLongitude);
        String valueAltitude = getResources().getString(R.string.valueAltitude);

        String valueXAxis = getResources().getString(R.string.valueXAxis);
        String valueYAxis = getResources().getString(R.string.valueYAxis);
        String valueZAxis = getResources().getString(R.string.valueZAxis);

        //GROUP
        listGroup.add(location);
        listGroup.add(accelerometer);
        listGroup.add(deviceSensors);

        //ITEM
        List<String> auxiliaryList = new ArrayList<>();
        auxiliaryList.add(latitude + ": " + valueLatitude);
        auxiliaryList.add(longitude + ": " + valueLongitude);
        auxiliaryList.add(altitude + ": " + valueAltitude);
        auxiliaryList.add(address);
        listData.put(listGroup.get(0), auxiliaryList);

        auxiliaryList = new ArrayList<>();
        auxiliaryList.add(xAxis + ": " + valueXAxis);
        auxiliaryList.add(yAxis + ": " + valueYAxis);
        auxiliaryList.add(zAxis + ": " + valueZAxis);
        listData.put(listGroup.get(1), auxiliaryList);

        auxiliaryList = new ArrayList<>();
        auxiliaryList.add(sensorsList);
        listData.put(listGroup.get(2), auxiliaryList);
    }
}
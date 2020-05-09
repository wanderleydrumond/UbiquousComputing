package com.drumond.ubiquouscomputing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Location variables
    private static final int REQUEST_CODE_PERMISSION = 1;
    private ProgressBar pbLoading;
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
        initialise();
    }

    private void initialise() {
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
                String on = getResources().getString(R.string.on);
                String accelerometer = getResources().getString(R.string.elv_h_accelerometer);
                    if (groupPosition == 0)
                        Toast.makeText(MainActivity.this, "GPS " + on, Toast.LENGTH_SHORT).show();
                    else if (groupPosition == 1)
                        Toast.makeText(MainActivity.this, accelerometer + " " + on, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(MainActivity.this, "Group: " + groupPosition + " closed", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setGroupIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_group, null));
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

        //GROUP
        listGroup.add(location);
        listGroup.add(accelerometer);
        listGroup.add(deviceSensors);

        //ITEM
        List<String> auxiliaryList = new ArrayList<>();
        auxiliaryList.add(latitude);
        auxiliaryList.add(longitude);
        auxiliaryList.add(altitude);
        auxiliaryList.add(address);
        listData.put(listGroup.get(0), auxiliaryList);

        auxiliaryList= new ArrayList<>();
        auxiliaryList.add(xAxis);
        auxiliaryList.add(yAxis);
        auxiliaryList.add(zAxis);
        listData.put(listGroup.get(1), auxiliaryList);

        auxiliaryList= new ArrayList<>();
        auxiliaryList.add(sensorsList);
        listData.put(listGroup.get(2), auxiliaryList);
    }
}
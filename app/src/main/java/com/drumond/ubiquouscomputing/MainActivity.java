package com.drumond.ubiquouscomputing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildList();
        manipulateExpandablelistView();
    }

    public void manipulateExpandablelistView() {
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
                Toast.makeText(MainActivity.this, "Group: " + groupPosition + " opened", Toast.LENGTH_SHORT).show();
                /*if (groupPosition == 0) {
                    Toast.makeText(MainActivity.this, location + " opened", Toast.LENGTH_SHORT).show();
                } else {
                    if (groupPosition == 1) {
                        Toast.makeText(MainActivity.this, accelerometer + " opened", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, deviceSensors + " opened", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(MainActivity.this, "Group: " + groupPosition + " closed", Toast.LENGTH_SHORT).show();
            }
        });

//        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));
        expandableListView.setGroupIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.icon_group, null));
    }

    public void buildList() {
        listGroup = new ArrayList<>();
        listData = new HashMap<>();

        //    Resources
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
        listGroup.add("Group 4");

        //ITEM
        List<String> auxiliaryList= new ArrayList<>();
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

        auxiliaryList= new ArrayList<>();
        auxiliaryList.add("Item 13");
        auxiliaryList.add("Item 14");
        auxiliaryList.add("Item 15");
        auxiliaryList.add("Item 16");
        listData.put(listGroup.get(3), auxiliaryList);
    }
}
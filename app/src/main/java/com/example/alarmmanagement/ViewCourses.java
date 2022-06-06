package com.example.alarmmanagement;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewCourses extends AppCompatActivity {

    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<AlarmModal> AlarmModalArrayList;
    private DBHandler dbHandler;
    private AlarmRVAdapter AlarmRVAdapter;
    private RecyclerView AlarmRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alarms);

        // initializing our all variables.
        AlarmModalArrayList = new ArrayList<>();
        dbHandler = new DBHandler(ViewCourses.this);

        // getting our course array
        // list from db handler class.
        AlarmModalArrayList = dbHandler.readCourses();

        // on below line passing our array lost to our adapter class.
        AlarmRVAdapter = new AlarmRVAdapter(AlarmModalArrayList, ViewCourses.this);
        AlarmRV = findViewById(R.id.idRVCourses);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewCourses.this, RecyclerView.VERTICAL, false);
        AlarmRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        AlarmRV.setAdapter(AlarmRVAdapter);




    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
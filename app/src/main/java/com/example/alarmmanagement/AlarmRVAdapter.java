package com.example.alarmmanagement;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmRVAdapter extends RecyclerView.Adapter<AlarmRVAdapter.ViewHolder>{

    // variable for our array list and context
    private ArrayList<AlarmModal> AlarmModalArrayList;
    private Context context;
    private DBHandler dbHandler;



    // constructor
    public AlarmRVAdapter(ArrayList<AlarmModal> AlarmModalArrayList, Context context) {
        this.AlarmModalArrayList = AlarmModalArrayList;
        this.context = context;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_alarms_activity, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // on below line we are setting data
        // to our views of recycler view item.
        AlarmModal modal = AlarmModalArrayList.get(position);
        holder.date.setText(modal.getDate());
        holder.description.setText(modal.getDescription());
        holder.minutes.setText(modal.getMinutes());
        holder.hours.setText(modal.getHour());
        dbHandler = new DBHandler(this.context);

        holder.itemView.findViewById(R.id.bin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHandler.deleteCourse(String.valueOf(holder.hours.getText()));
                Intent i = new Intent(context, ViewCourses.class);
                context.startActivity(i);
            }
        });
        holder.itemView.findViewById(R.id.switchAlarm1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        // below line is to add on click listener for our recycler view item.
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // on below line we are calling an intent.
                Intent i = new Intent(context, UpdateCourseActivity.class);

                // below we are passing all our values.
                i.putExtra("name", modal.getCourseName());
                i.putExtra("description", modal.getCourseDescription());
                i.putExtra("duration", modal.getCourseDuration());
                i.putExtra("tracks", modal.getCourseTracks());

                // starting our activity.
                context.startActivity(i);
            }
        });

         */
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list
        return AlarmModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        private TextView date, description, minutes, hours;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            date = itemView.findViewById(R.id.idDate);
            description = itemView.findViewById(R.id.description);
            minutes = itemView.findViewById(R.id.minutes);
            hours = itemView.findViewById(R.id.hour);
        }
    }

}
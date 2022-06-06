package com.example.alarmmanagement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmmanagement.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private ArrayList<Integer> alarms;
    private int counterAlarms;

    private ActivityMainBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private ImageView bin;
    private Button historyBtn;
    private String hour1;
    private String minute1 ;
    private RelativeLayout relative1;
    private RelativeLayout relative2;
    private RelativeLayout relative3;

    private SwitchCompat switchCompat;
    private TextView date,minutes,hours,description;
    private DBHandler dbHandler;
    private SharedPreferences sharedPreferences = null;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");
    LocalDate localDate = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();
        hour1="";
        minute1="";


        date = findViewById(R.id.idDate);
        minutes=findViewById(R.id.minutes);
        hours=findViewById(R.id.hour);
        description=findViewById(R.id.description);
        historyBtn=findViewById(R.id.HISTORY);
        dbHandler = new DBHandler(MainActivity.this);
        // DARK MODE
        switchCompat = findViewById(R.id.switchCompat);
        sharedPreferences = getSharedPreferences("night", 0);
        boolean booleanValue = sharedPreferences.getBoolean("night_mode", true);
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            switchCompat.setChecked(true);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchCompat.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", true);
                    editor.commit();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchCompat.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", false);
                    editor.commit();
                }
            }
        });

        alarms = new ArrayList();
        counterAlarms = 0;
        relative1 = (RelativeLayout) findViewById(R.id.relative1);
        initializeRelativeLayouts(1);
        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAction(view, 1);
            }
        });
        relative2 = (RelativeLayout) findViewById(R.id.relative2);
        initializeRelativeLayouts(2);
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAction(view, 2);
            }
        });
        relative3 = (RelativeLayout) findViewById(R.id.relative3);
        initializeRelativeLayouts(3);
        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAction(view, 3);
            }
        });

/*
        // Set timer correctly
        Calendar currentTime = Calendar.getInstance();
        String currentTimer;
        String minutes;
        if (currentTime.get(Calendar.MINUTE) < 10) {
            minutes = "0" + currentTime.get(Calendar.MINUTE);
        } else {
            minutes = "" + currentTime.get(Calendar.MINUTE);
        }
        if (currentTime.get(Calendar.HOUR_OF_DAY) <= 12) {
            if (currentTime.get(Calendar.HOUR_OF_DAY) == 12)
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) + " : " + minutes + " PM";
            else
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) + " : " + minutes + " AM";
        } else {
            if (currentTime.get(Calendar.HOUR_OF_DAY) == 24)
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) - 12+ " : " + minutes + " AM";
            else
                currentTimer = currentTime.get(Calendar.HOUR_OF_DAY) - 12+ " : " + minutes + " PM";
        }
        binding.selectedTime1.setText(currentTimer);
*/
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewCourses.class);
                startActivity(i);

            }
        });

        binding.selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select Alarm Time")
                        .build();

                picker.show(getSupportFragmentManager(),"foxandroid");

                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (picker.getHour() > 12){
                            hour1 = String.format("%02d",(picker.getHour()-12));
                            minute1 = String.format("%02d",picker.getMinute())+" PM";
                            /*
                            selectedTime.setText(
                                    String.format("%02d",(picker.getHour())-12)+" : "+String.format("%02d",picker.getMinute())+" PM"
                            );

                             */

                        }else {
                            hour1 = String.format("%02d",(picker.getHour()));
                            minute1 = String.format("%02d",picker.getMinute())+" AM";
                            /*
                            selectedTime.setText( String.format("%02d",(picker.getHour()))+" : "+String.format("%02d",picker.getMinute())+" AM");

                             */

                        }


                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                        calendar.set(Calendar.MINUTE,picker.getMinute());
                        calendar.set(Calendar.SECOND,0);
                        calendar.set(Calendar.MILLISECOND,0);
                        if(hour1.equals("") && minute1.equals("") ){
                            Toast.makeText(MainActivity.this, "Please set the time..", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbHandler.addNewAlarm(dtf.format(localDate),minute1,"alarm",hour1);
                        Toast.makeText(MainActivity.this, "inserted successfully", Toast.LENGTH_SHORT).show();


                    }
                });



            }

        });


/*
        binding.setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAlarm();

            }
        });

        binding.cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelAlarm();

            }
        });
*/
    }

    private void initializeRelativeLayouts(int relativeIndex) { // setting up every object before getting clicked
        if (relativeIndex == 1) {
            TextView alarmTime = (TextView) relative1.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative1.getChildAt(1);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(alarmTime);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm();
                    }
                }
            });

            ImageView bin = (ImageView) relative1.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative1.setVisibility(View.GONE);
                }
            });
        } else if (relativeIndex == 2) {
            TextView alarmTime = (TextView) relative2.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative2.getChildAt(1);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(alarmTime);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm();
                    }
                }
            });

            ImageView bin = (ImageView) relative2.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative1.setVisibility(View.GONE);
                }
            });
        } else if (relativeIndex == 3) {
            TextView alarmTime = (TextView) relative3.getChildAt(0);
            SwitchCompat switchAlarm = (SwitchCompat) relative3.getChildAt(1);
            switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) { // ενεργοποιηση ειδοποιησης
                        setAlarm(alarmTime);
                    } else { // απενεργοποιηση ειδοποιησης
                        cancelAlarm();
                    }
                }
            });

            ImageView bin = (ImageView) relative3.getChildAt(2);
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relative1.setVisibility(View.GONE);
                }
            });
        }
    }


    private void checkAction(View view, int relativeIndex) { // mouse clicked on relative layout
        if (relativeIndex == 1) {
            SwitchCompat switchAlarm = (SwitchCompat) relative1.getChildAt(1);

            ImageView bin = (ImageView) relative1.getChildAt(2);

        }
    }

    private void cancelAlarm() {
/*
        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null) {

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }

        alarmManager.cancel(pendingIntent);
 */
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm(TextView alarmTime) {
/*
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
 */
        String alarm = (String) alarmTime.getText();
        int hour;
        int minute;
        if (alarm.contains("PM")) {
            int separate1 = alarm.indexOf(" : ");
            int separate2 = alarm.indexOf(" PM");
            hour = Integer.parseInt(alarm.substring(0,separate1)) + 12;
            minute = Integer.parseInt(alarm.substring(separate1+3, separate2));
            if (hour != 12) // 12.00 PM == 12.00 PM BUT 1 PM == 13 PM
                hour += 12;
        } else {
            int separate1 = alarm.indexOf(" : ");
            int separate2 = alarm.indexOf(" AM");
            hour = Integer.parseInt(alarm.substring(0,separate1)) + 12;
            minute = Integer.parseInt(alarm.substring(separate1+3, separate2));
            if (hour == 12) // 12.00 AM == 00.00 AM
                hour -= 12;
        }

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        //startActivity(intent);
        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();


    }



    private int getCorrectRelativeLayout() {
        if (relative1.getVisibility() == View.GONE) {
            alarms.add(1);
            counterAlarms++;
            relative1.setVisibility(View.VISIBLE);
            return 1;
        } else if (relative2.getVisibility() == View.GONE) {
            alarms.add(2);
            counterAlarms++;
            relative1.setVisibility(View.VISIBLE);
            return 2;
        } else if (relative3.getVisibility() == View.GONE) {
            alarms.add(3);
            counterAlarms++;
            relative1.setVisibility(View.VISIBLE);
            return 3;
        }
        return 4;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


}
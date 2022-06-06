package com.example.alarmmanagement;

public class AlarmModal {
    // variables for our coursename,
    // description, tracks and duration, id.
    private String date;
    private String minutes;
    private String hour;
    private String description;
    private int id;

    // creating getter and setter methods
    public String getDate() {
        return date;
    }

    public void setCourseName(String courseName) {
        this.date = courseName;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setCourseDuration(String courseDuration) {
        this.minutes = courseDuration;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // constructor
    public AlarmModal(String courseName, String courseDuration, String courseTracks, String courseDescription) {
        this.date = courseName;
        this.minutes = courseDuration;
        this.hour = courseTracks;
        this.description = courseDescription;
    }
}

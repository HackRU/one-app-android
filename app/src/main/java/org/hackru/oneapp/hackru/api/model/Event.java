package org.hackru.oneapp.hackru.api.model;

public class Event {
    public String title;
    public String message;
    public String time;
    public String place;

    public Event(String title, String message, String time, String place) {
        this.title = title;
        this.message = message;
        this.time = time;
        this.place = place;
    }
}

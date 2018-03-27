package org.hackru.oneapp.hackru;

/**
 * Created by Sean on 12/11/2017.
 */

public class Announcement {
    private String date, message;

    public Announcement(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public String getDate() {
        return this.date;
    }

    public String getMessage() {
        return this.message;
    }


}

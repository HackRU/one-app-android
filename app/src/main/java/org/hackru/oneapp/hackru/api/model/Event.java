package org.hackru.oneapp.hackru.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("start")
    @Expose
    private String start;

    @SerializedName("summary")
    @Expose
    private String summary;


    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

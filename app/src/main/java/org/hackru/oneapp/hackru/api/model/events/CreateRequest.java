package org.hackru.oneapp.hackru.api.model.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateRequest {

    @SerializedName("conferenceSolutionKey")
    @Expose
    private ConferenceSolutionKey conferenceSolutionKey;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("status")
    @Expose
    private Status status;

    public ConferenceSolutionKey getConferenceSolutionKey() {
        return conferenceSolutionKey;
    }

    public void setConferenceSolutionKey(ConferenceSolutionKey conferenceSolutionKey) {
        this.conferenceSolutionKey = conferenceSolutionKey;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}

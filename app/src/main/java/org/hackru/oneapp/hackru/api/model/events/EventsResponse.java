package org.hackru.oneapp.hackru.api.model.events;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsResponse {

    @SerializedName("body")
    @Expose
    private List<EventBody> body = null;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public List<EventBody> getBody() {
        return body;
    }

    public void setBody(List<EventBody> body) {
        this.body = body;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}

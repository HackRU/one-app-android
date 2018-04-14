
package org.hackru.oneapp.hackru.api.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnnouncementsResponse {

    @SerializedName("body")
    @Expose
    private List<Announcement> body = null;
//    @SerializedName("headers")
//    @Expose
//    private Headers headers;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public List<Announcement> getBody() {
        return body;
    }

    public void setBody(List<Announcement> body) {
        this.body = body;
    }

//    public Headers getHeaders() {
//        return headers;
//    }
//
//    public void setHeaders(Headers headers) {
//        this.headers = headers;
//    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder();
        for(Announcement a : getBody()) {
            repr.append(a.toString()).append("\n");
        }
        return repr.toString();
    }
}


package org.hackru.oneapp.hackru.api.model.events;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Headers {

    @SerializedName("Access-Control-Allow-Origin")
    @Expose
    private List<String> accessControlAllowOrigin = null;
    @SerializedName("Access-Control-Allow-Headers")
    @Expose
    private List<String> accessControlAllowHeaders = null;
    @SerializedName("Access-Control-Allow-Credentials")
    @Expose
    private List<Boolean> accessControlAllowCredentials = null;

    public List<String> getAccessControlAllowOrigin() {
        return accessControlAllowOrigin;
    }

    public void setAccessControlAllowOrigin(List<String> accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public List<String> getAccessControlAllowHeaders() {
        return accessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(List<String> accessControlAllowHeaders) {
        this.accessControlAllowHeaders = accessControlAllowHeaders;
    }

    public List<Boolean> getAccessControlAllowCredentials() {
        return accessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(List<Boolean> accessControlAllowCredentials) {
        this.accessControlAllowCredentials = accessControlAllowCredentials;
    }

}

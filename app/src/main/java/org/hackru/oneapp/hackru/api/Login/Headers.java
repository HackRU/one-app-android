package org.hackru.oneapp.hackru.api.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Headers {

    @SerializedName("Content-Type")
    @Expose
    private String contentType;
    @SerializedName("Access-Control-Allow-Origin")
    @Expose
    private String accessControlAllowOrigin;
    @SerializedName("Access-Control-Allow-Headers")
    @Expose
    private String accessControlAllowHeaders;
    @SerializedName("Access-Control-Allow-Credentials")
    @Expose
    private Boolean accessControlAllowCredentials;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAccessControlAllowOrigin() {
        return accessControlAllowOrigin;
    }

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public String getAccessControlAllowHeaders() {
        return accessControlAllowHeaders;
    }

    public void setAccessControlAllowHeaders(String accessControlAllowHeaders) {
        this.accessControlAllowHeaders = accessControlAllowHeaders;
    }

    public Boolean getAccessControlAllowCredentials() {
        return accessControlAllowCredentials;
    }

    public void setAccessControlAllowCredentials(Boolean accessControlAllowCredentials) {
        this.accessControlAllowCredentials = accessControlAllowCredentials;
    }

}
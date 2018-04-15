package org.hackru.oneapp.hackru.api.model.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceSolution {

    @SerializedName("iconUri")
    @Expose
    private String iconUri;
    @SerializedName("key")
    @Expose
    private Key key;
    @SerializedName("name")
    @Expose
    private String name;

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

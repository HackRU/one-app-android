package org.hackru.oneapp.hackru.api.model.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reminders {

    @SerializedName("useDefault")
    @Expose
    private Boolean useDefault;

    public Boolean getUseDefault() {
        return useDefault;
    }

    public void setUseDefault(Boolean useDefault) {
        this.useDefault = useDefault;
    }

}

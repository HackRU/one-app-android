package org.hackru.oneapp.hackru.api.model.events;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceData {

    @SerializedName("conferenceSolution")
    @Expose
    private ConferenceSolution conferenceSolution;
    @SerializedName("createRequest")
    @Expose
    private CreateRequest createRequest;
    @SerializedName("entryPoints")
    @Expose
    private List<EntryPoint> entryPoints = null;
    @SerializedName("signature")
    @Expose
    private String signature;

    public ConferenceSolution getConferenceSolution() {
        return conferenceSolution;
    }

    public void setConferenceSolution(ConferenceSolution conferenceSolution) {
        this.conferenceSolution = conferenceSolution;
    }

    public CreateRequest getCreateRequest() {
        return createRequest;
    }

    public void setCreateRequest(CreateRequest createRequest) {
        this.createRequest = createRequest;
    }

    public List<EntryPoint> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<EntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}

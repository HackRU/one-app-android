package org.hackru.oneapp.hackru.api.service;

import com.google.gson.JsonObject;

import org.hackru.oneapp.hackru.api.model.AnnouncementsResponse;
import org.hackru.oneapp.hackru.api.model.AuthorizeRequest;
import org.hackru.oneapp.hackru.api.model.Login;
import org.hackru.oneapp.hackru.api.model.ReadRequest;
import org.hackru.oneapp.hackru.api.model.events.Event;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Sean on 3/21/2018.
 */

public interface HackRUService {
    @POST("authorize")
    Call<Login> authorize(@Body AuthorizeRequest body);

    @POST("read")
    Call<JsonObject> read(@Body ReadRequest body);

    @POST("update")
    Call<JsonObject> update(@Body JsonObject body);
  
    @GET("dayof-slack")
    Call<AnnouncementsResponse> getAnnouncements();

    @GET("dayof-events")
    Call<JsonObject> getEvents();

    @GET("{email}")
    Call<String> printLabel(@Path("email") String email);

}

package org.hackru.oneapp.hackru.api.service;

import org.hackru.oneapp.hackru.api.model.Announcement;
import org.hackru.oneapp.hackru.api.model.AuthTokenRequest;
import org.hackru.oneapp.hackru.api.model.AuthorizeRequest;
import org.hackru.oneapp.hackru.api.model.Event;
import org.hackru.oneapp.hackru.api.model.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

/**
 * Created by Sean on 3/21/2018.
 */

public interface HackRUService {
    @POST("authorize")
    Call<Login> authorize(@Body AuthorizeRequest body);

    @HTTP(method="GET", path="announcements", hasBody = true)
    Call<List<Announcement>> getAnnouncements(@Body AuthTokenRequest body);

    @HTTP(method="GET", path="events", hasBody = true)
    Call<List<Event>> getEvents(@Body AuthTokenRequest body);
}

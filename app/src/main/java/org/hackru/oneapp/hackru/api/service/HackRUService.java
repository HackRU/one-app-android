package org.hackru.oneapp.hackru.api.service;

import org.hackru.oneapp.hackru.api.model.Announcement;
import org.hackru.oneapp.hackru.api.model.AuthorizeRequest;
import org.hackru.oneapp.hackru.api.model.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Sean on 3/21/2018.
 */

public interface HackRUService {
    @POST("authorize")
    Call<Login> authorize(@Body AuthorizeRequest body);

    @GET("announcements")
    Call<List<Announcement>> getAnnouncements();
}

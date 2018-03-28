package org.hackru.oneapp.hackru.api.service;

import org.hackru.oneapp.hackru.api.model.AuthorizeRequest;
import org.hackru.oneapp.hackru.api.model.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sean on 3/21/2018.
 */

public interface HackRUService {
    @POST("authorize")
    Call<Login> authorize(@Body AuthorizeRequest body);
}

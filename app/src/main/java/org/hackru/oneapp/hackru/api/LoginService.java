package org.hackru.oneapp.hackru.api;

import org.hackru.oneapp.hackru.api.Login.AuthorizeRequest;
import org.hackru.oneapp.hackru.api.Login.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sean on 3/21/2018.
 */

public interface LoginService {
    @POST("authorize")
    Call<Login> authorize(@Body AuthorizeRequest body);
}

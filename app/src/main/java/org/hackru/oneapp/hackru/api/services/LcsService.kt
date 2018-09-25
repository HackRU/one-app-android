package org.hackru.oneapp.hackru.api.services

import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.api.models.AuthorizeModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface LcsService {

    @GET("dayof-slack")
    fun getAnnouncements(): Call<AnnouncementsModel.Response>

    @POST("authorize")
    fun authorize(): Call<AuthorizeModel.Response>
}
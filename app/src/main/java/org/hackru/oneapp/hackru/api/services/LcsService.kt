package org.hackru.oneapp.hackru.api.services

import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.api.models.AuthorizeModel
import org.hackru.oneapp.hackru.api.models.ReadModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LcsService {

    @GET("dayof-slack")
    fun getAnnouncements(): Call<AnnouncementsModel.Response>

    @POST("authorize")
    fun authorize(@Body request: AuthorizeModel.Request): Call<AuthorizeModel.Response>

    @POST("read")
    fun read(@Body request: ReadModel.Request): Call<ReadModel.Response>
}
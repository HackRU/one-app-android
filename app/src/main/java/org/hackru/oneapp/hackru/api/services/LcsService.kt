package org.hackru.oneapp.hackru.api.services

import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.api.models.AuthorizeModel
import org.hackru.oneapp.hackru.api.models.EventsModel
import org.hackru.oneapp.hackru.api.models.RoleModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LcsService {

    @GET("dayof-slack")
    fun getAnnouncements(): Call<AnnouncementsModel.Response>

    @GET("dayof-events")
    fun getEvents(): Call<EventsModel.Response>

    @POST("authorize")
    fun authorize(@Body request: AuthorizeModel.Request): Call<AuthorizeModel.Response>

    @POST("read")
    fun getRole(@Body request: RoleModel.Request): Call<RoleModel.Response>
}
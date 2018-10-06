package org.hackru.oneapp.hackru.api.services

import android.arch.persistence.room.Update
import org.hackru.oneapp.hackru.api.models.*
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

    @POST("read")
    fun getUserDayOf(@Body request: DayOfModel.Request): Call<DayOfModel.Response>

    @POST("update")
    fun updateUserDayOf(@Body request: UpdateModel.Request): Call<UpdateModel.Response>
}
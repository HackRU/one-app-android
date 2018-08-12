package org.hackru.oneapp.hackru.api.services

import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import retrofit2.Call
import retrofit2.http.GET

interface LcsService {

    @GET("dayof-slack")
    fun getAnnouncements(): Call<AnnouncementsModel.Response>
}
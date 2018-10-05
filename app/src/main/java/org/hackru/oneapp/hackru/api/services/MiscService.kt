package org.hackru.oneapp.hackru.api.services

import retrofit2.Call
import retrofit2.http.GET

interface MiscService {
    @GET("events.txt")
    fun getScannerEvents(): Call<String>
}
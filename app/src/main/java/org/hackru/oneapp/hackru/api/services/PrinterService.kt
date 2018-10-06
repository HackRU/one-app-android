package org.hackru.oneapp.hackru.api.services

import org.hackru.oneapp.hackru.api.models.PrinterModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PrinterService {

    @POST(".")
    fun print(@Body request: PrinterModel.Request): Call<Unit>
}
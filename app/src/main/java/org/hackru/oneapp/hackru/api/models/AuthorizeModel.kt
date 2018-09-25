package org.hackru.oneapp.hackru.api.models

import com.google.gson.annotations.SerializedName

object AuthorizeModel {

    data class Request(val email: String,
                       val password: String)

    data class Response(val statusCode: Int,
                        val body: String)

    data class Body(val auth: Auth)

    data class Auth(val token: String,
                    @SerializedName("valid_until") val validUntil: String,
                    val email: String)

}
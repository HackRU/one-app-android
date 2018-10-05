package org.hackru.oneapp.hackru.api.models

import com.google.gson.annotations.SerializedName

object DayOfReadModel {

    data class Request(val email: String,
                       val token: String,
                       val query: Query)

    data class Query(val email: String)

    data class Response(val statusCode: Int,
                        val body: List<User>)

    data class User(val email: String,
                    @SerializedName("day_of") val dayOf: Map<String, Any>,
                    @SerializedName("first_name") val firstName: String,
                    @SerializedName("last_name") val lastName: String)


}
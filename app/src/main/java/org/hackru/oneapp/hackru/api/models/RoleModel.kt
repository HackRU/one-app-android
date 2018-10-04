package org.hackru.oneapp.hackru.api.models

import com.google.gson.annotations.SerializedName

object RoleModel {

    data class Request(val email: String,
                       val token: String,
                       val query: Query)

    data class Query(val email: String)

    data class Response(val statusCode: Int,
                       val body: List<User>)

    data class User(val email: String,
                    val role: Role,
                    @SerializedName("first_name") val firstName: String,
                    @SerializedName("last_name") val lastName: String)

    data class Role(val organizer: Boolean,
                    val director: Boolean)

}
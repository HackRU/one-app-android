package org.hackru.oneapp.hackru.api.models

object DayOfModel {
    class Request(val email: String,
                  val token: String,
                  val query: Query)
    class Query(val email: String)

    class Response(val statusCode: Int,
                   val body: List<User>)
    class User(val day_of: Map<String, Any>,
               val registration_status: String)
}
package org.hackru.oneapp.hackru.api.models

object UpdateModel {
    data class Request(val auth_email: String,
                       val auth: String,
                       val user_email: String,
                       val event: String) {
        val updates: HashMap<String, Map<String, Any>> = HashMap()
        init {
            if(event.equals("check-in")) {
                val map = HashMap<String, Boolean>()
                map.put("day_of.check-in", true)
                updates.put("\$set", map.toMap())
            } else {
                val map = HashMap<String, Int>()
                map.put("day_of.$event", 1)
                updates.put("\$inc", map.toMap())
            }
        }
    }

    data class Response(val statusCode: Int,
                        val body: String)
}
package org.hackru.oneapp.hackru.api.models

object EventsModel {
    data class Event(val time: String,
                     val title: String,
                     val details: String)
}
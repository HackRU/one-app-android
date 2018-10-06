package org.hackru.oneapp.hackru.api.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object EventsModel {

    data class Response(val statusCode: Int,
                        val body: List<GoogleCalendarEvent>)

    data class GoogleCalendarEvent(val summary: String?,
                                   val location: String?,
                                   val start: Start?,
                                   val end: End?,
                                   val id: String) {
        data class Start(val date: String?,
                         val dateTime: String?)
        data class End(val date: String?,
                       val dateTime: String?)
    }

    /**
     * The data class that represents an event that is provided to UI controllers (i.e.
     * Activities and Fragments). This class is made from Google Calendar objects from LCS.
     *
     * @param title The title of the event
     * @param details Any details of the event that the UI controller should display
     * @param time A string that represents the time of the event (for example: "10:30 AM")
     * @param timeStart A Long that represents starting time in epoch time (in milliseconds)
     * @param timeEnd A Long that represents ending time in epoch time (in milliseconds)
     */
    @Entity
    data class Event(@PrimaryKey val id: String,
                     val title: String,
                     val details: String,
                     val time: String,
                     val timeStart: Long,
                     val timeEnd: Long)
}
package org.hackru.oneapp.hackru.api.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object AnnouncementsModel {

    data class Response(val statusCode: String, val body: List<SlackMessage>) {
        data class SlackMessage(val ts: String?,
                                val subtype: String?,
                                val text: String?)
    }

    /**
     * The data class that represents an announcement that is provided to UI controllers (i.e.
     * Activities and Fragments). This class is made from appropriate SlackMessage objects from slack.
     *
     * Note: Although announcements on slack may have an empty body (i.e. file uploads), only announcements
     * with a non-empty body are created into {@code Announcement} objects. So, {@code text} will
     * never be an empty string (and neither will {@code ts}).
     *
     * @param ts A Long that represents when the message was sent on slack in epoch time (milliseconds)
     * @param text The text of the announcement
     * @param time A string that represents the time of the event (for example: "10:30 AM")
     */
    @Entity
    data class Announcement(@PrimaryKey val ts: Long,
                            val text: String,
                            val time: String)

}
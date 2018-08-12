package org.hackru.oneapp.hackru.api.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

object AnnouncementsModel {

    data class Response(val statusCode: String, val body: List<SlackMessage>) {
        data class SlackMessage(val ts: String?,
                                val subType: String?,
                                val text: String?)
    }

    @Entity
    data class Announcement(val ts: String,
                            val text: String) {
        // A guaranteed non-null and unique key is required for persisting an object with Room
        @PrimaryKey(autoGenerate = true) var databaseKey: Int? = null
    }

}
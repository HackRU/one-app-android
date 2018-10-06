package org.hackru.oneapp.hackru.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.api.models.EventsModel

@Database(entities = [AnnouncementsModel.Announcement::class, EventsModel.Event::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun announcementsDao(): AnnouncementsDao
    abstract fun eventsDao(): EventsDao
}
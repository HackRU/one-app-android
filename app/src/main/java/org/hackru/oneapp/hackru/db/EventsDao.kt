package org.hackru.oneapp.hackru.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import org.hackru.oneapp.hackru.api.models.EventsModel

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(events: List<EventsModel.Event>)

    @Query("SELECT * FROM Event")
    fun loadAll(): LiveData<List<EventsModel.Event>>
}
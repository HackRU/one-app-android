package org.hackru.oneapp.hackru.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel

@Dao
interface AnnouncementsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(announcements: List<AnnouncementsModel.Announcement>)

    @Query("SELECT * FROM Announcement")
    fun loadAll(): LiveData<List<AnnouncementsModel.Announcement>>
}
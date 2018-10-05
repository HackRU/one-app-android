package org.hackru.oneapp.hackru.repositories

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.AsyncTask
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.api.services.LcsService
import org.hackru.oneapp.hackru.db.AnnouncementsDao
import org.hackru.oneapp.hackru.ui.main.announcements.MessageParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AnnouncementsRepository @Inject constructor(val announcementsDao: AnnouncementsDao, val lcsService: LcsService, val context: Context) {

    fun loadAnnouncementsFromDatabase(): MediatorLiveData<Resource<List<AnnouncementsModel.Announcement>>> {
        val result = MediatorLiveData<Resource<List<AnnouncementsModel.Announcement>>>()
        result.value = Resource.loading(emptyList())
        val dbSource = announcementsDao.loadAll()
        result.addSource(dbSource) { data ->
            if(data?.isEmpty() == false) {
                result.value = Resource.success(data)
            }
            result.removeSource(dbSource)
            fetchAnnouncementsFromNetwork(result)
        }
        return result
    }

    fun refreshAnnouncements(result: MediatorLiveData<Resource<List<AnnouncementsModel.Announcement>>>?) {
        val dbSource = announcementsDao.loadAll()
        result?.addSource(dbSource) { data ->
            if(data?.isEmpty() == false) {
                result.value = Resource.success(data)
            }
            result.removeSource(dbSource)
            fetchAnnouncementsFromNetwork(result)
        }
    }

    fun fetchAnnouncementsFromNetwork(result: MediatorLiveData<Resource<List<AnnouncementsModel.Announcement>>>?) {
        lcsService.getAnnouncements().enqueue(object : Callback<AnnouncementsModel.Response> {
            override fun onResponse(call: Call<AnnouncementsModel.Response>?, response: Response<AnnouncementsModel.Response>?) {
                val data: List<AnnouncementsModel.Response.SlackMessage>? = response?.body()?.body
                if (data != null && response.isSuccessful) {
                    val announcements = mutableListOf<AnnouncementsModel.Announcement>()
                    data.forEach {
                        if(it.text != null && it.text.isNotEmpty() && it.ts != null && it.subtype == null) {
                            val text: String = MessageParser.stringParser(it.text)
                            announcements.add(AnnouncementsModel.Announcement(it.ts, text))
                        }
                    }
                    result?.value = Resource.success(announcements.toList())
                    SaveToDatabaseAsyncTask(announcementsDao).execute(announcements)
                } else {
                    result?.value = Resource.failure(context.getString(R.string.network_error_unknown), emptyList())
                }
            }

            override fun onFailure(call: Call<AnnouncementsModel.Response>?, t: Throwable?) {
                if(t is IOException) {
                    result?.value = Resource.failure(context.getString(R.string.network_error_no_internet), emptyList())
                } else {
                    result?.value = Resource.failure(context.getString(R.string.network_error_deserialization), emptyList())
                }
            }
        })
    }

    private class SaveToDatabaseAsyncTask(val announcementsDao: AnnouncementsDao) : AsyncTask<List<AnnouncementsModel.Announcement>, Unit, Unit>() {
        override fun doInBackground(vararg params: List<AnnouncementsModel.Announcement>) {
            announcementsDao.save(params[0])
        }
    }

}
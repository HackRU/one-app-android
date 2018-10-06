package org.hackru.oneapp.hackru.repositories

import android.arch.lifecycle.MediatorLiveData
import android.content.Context
import android.os.AsyncTask
import org.hackru.oneapp.hackru.R
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.EventsModel
import org.hackru.oneapp.hackru.api.services.LcsService
import org.hackru.oneapp.hackru.db.EventsDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventsRepository @Inject constructor(val eventsDao: EventsDao, val lcsService: LcsService, val context: Context) {

    private val TIME_SUNDAY_MIDNIGHT: Long = 1538884800000

    fun loadEventsFromDatabase(): MediatorLiveData<Resource<List<List<EventsModel.Event>>>> {
        val result = MediatorLiveData<Resource<List<List<EventsModel.Event>>>>()
        result.value = Resource.loading(emptyList())
        val dbSource = eventsDao.loadAll()
        result.addSource(dbSource) { data ->
            if(data?.isEmpty() == false) {
                result.value = Resource.success(sortEventsByDay(data))
            }
            result.removeSource(dbSource)
            fetchEventsFromNetwork(result)
        }
        return result
    }

    fun refreshEvents(result: MediatorLiveData<Resource<List<List<EventsModel.Event>>>>?) {
        val dbSource = eventsDao.loadAll()
        result?.addSource(dbSource) { data ->
            if(data?.isEmpty() == false) {
                result.value = Resource.success(sortEventsByDay(data))
            }
            result.removeSource(dbSource)
            fetchEventsFromNetwork(result)
        }
    }

    fun fetchEventsFromNetwork(result: MediatorLiveData<Resource<List<List<EventsModel.Event>>>>?) {
        lcsService.getEvents().enqueue(object : Callback<EventsModel.Response> {
            override fun onResponse(call: Call<EventsModel.Response>?, response: Response<EventsModel.Response>?) {
                val data: List<EventsModel.GoogleCalendarEvent>? = response?.body()?.body
                if (data != null && response.isSuccessful) {
                    val events = mutableListOf<EventsModel.Event>()
                    val toMillisFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                    val toStringFormatter = SimpleDateFormat("h:mm a")
                    data.forEach {
                        val title: String = it.summary ?: ""
                        val details: String = it.location ?: ""
                        val startDate: String? = it.start?.dateTime ?: it.start?.date
                        val endDate: String? = it.end?.dateTime ?: it.end?.date
                        val timeStart = toMillisFormatter.parse(startDate).time
                        val timeEnd = toMillisFormatter.parse(endDate).time
                        val time: String = toStringFormatter.format(Date(timeStart)).toString()
                        events.add(EventsModel.Event(it.id, title, details, time, timeStart, timeEnd))
                    }
                    result?.value = Resource.success(sortEventsByDay(events))
                    SaveToDatabaseAsyncTask(eventsDao).execute(events)
                } else {
                    result?.value = Resource.failure(context.getString(R.string.network_error_unknown), emptyList())
                }
            }

            override fun onFailure(call: Call<EventsModel.Response>?, t: Throwable?) {
                if(t is IOException) {
                    result?.value = Resource.failure(context.getString(R.string.network_error_no_internet), emptyList())
                } else {
                    result?.value = Resource.failure(context.getString(R.string.network_error_deserialization), emptyList())
                }
            }
        })
    }

    private class SaveToDatabaseAsyncTask(val eventsDao: EventsDao) : AsyncTask<List<EventsModel.Event>, Unit, Unit>() {
        override fun doInBackground(vararg params: List<EventsModel.Event>) {
            eventsDao.save(params[0])
        }
    }

    private fun sortEventsByDay(events: List<EventsModel.Event>): List<List<EventsModel.Event>> {
        val saturdayList: MutableList<EventsModel.Event> = mutableListOf()
        val sundayList: MutableList<EventsModel.Event> = mutableListOf()

        events.forEach {
            if(it.timeStart < TIME_SUNDAY_MIDNIGHT) {
                saturdayList.add(it)
            } else {
                sundayList.add(it)
            }
        }

        return listOf(saturdayList, sundayList)
    }

}
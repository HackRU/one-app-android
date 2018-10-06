package org.hackru.oneapp.hackru.ui.main.events

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.api.models.EventsModel
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import org.hackru.oneapp.hackru.repositories.EventsRepository
import javax.inject.Inject

class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {

    var events: MediatorLiveData<Resource<List<List<EventsModel.Event>>>>? = null
        get() {
            if(field == null) {
                field = eventsRepository.loadEventsFromDatabase()
            } else {
                eventsRepository.refreshEvents(field)
            }
            return field
        }

}
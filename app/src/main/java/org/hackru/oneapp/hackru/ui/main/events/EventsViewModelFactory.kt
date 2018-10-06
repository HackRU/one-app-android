package org.hackru.oneapp.hackru.ui.main.events

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import org.hackru.oneapp.hackru.repositories.EventsRepository
import javax.inject.Inject

class EventsViewModelFactory @Inject constructor(val eventsRepository: EventsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.equals(EventsViewModel::class.java)) {
            return EventsViewModel(eventsRepository) as T
        } else {
            throw RuntimeException("Unable to create ${modelClass::class.simpleName} with ${this::class.simpleName}")
        }
    }

}
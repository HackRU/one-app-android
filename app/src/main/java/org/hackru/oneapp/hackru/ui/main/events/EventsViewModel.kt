package org.hackru.oneapp.hackru.ui.main.events

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.EventsModel

class EventsViewModel : ViewModel() {
    private val satTimes = arrayOf("10:00 AM", "11:30 AM", "12:00 PM",
            "12:30 PM", "01:00 PM", "01:00 PM", "06:00 PM")
    private val sunTimes = arrayOf("12:00 AM", "02:00 AM", "07:30 AM",
            "11:30 AM", "11:30 AM", "12:00 PM", "03:00 PM")

    private val satTitles = arrayOf("Check-In", "Opening Ceremonies", "Lunch",
            "Keynote Speaker", "Hacking Begins", "Team Building", "Dinner")
    private val sunTitles = arrayOf("Midnight Surprise", "Midnight Snack", "Breakfast",
            "Hacking Ends", "Lunch", "Demos Begin", "Closing Ceremonies")

    private val details = arrayOf("Welcome to HackRU Fall 2018! Have a good one!",
            "Welcome to HackRU Fall 2018! Have a good three!", "Welcome to HackRU Fall 2018! Have a good four!",
            "Welcome to HackRU Fall 2018! Have a good five!", "Welcome to HackRU Fall 2018! Have a good six!",
            "Welcome to HackRU Fall 2018! Have a good seven!", "Welcome to HackRU Fall 2018! Have a good two!")


    private val saturdayEventsList = mutableListOf<EventsModel.Event>()

    private val sundayEventsList = mutableListOf<EventsModel.Event>()

    init {
        for(i in 0 until satTitles.size) {
            saturdayEventsList.add(EventsModel.Event(satTimes[i], satTitles[i], details[i]))
            sundayEventsList.add(EventsModel.Event(sunTimes[i], sunTitles[i], details[i]))
        }
    }

        val saturdayEvents: MutableLiveData<Resource<List<EventsModel.Event>>> = MutableLiveData()
        get() {
            field.value = Resource.success(saturdayEventsList.toList())
            return field
        }

        val sundayEvents: MutableLiveData<Resource<List<EventsModel.Event>>> = MutableLiveData()
        get() {
            field.value = Resource.success(sundayEventsList.toList())
            return field
        }
}
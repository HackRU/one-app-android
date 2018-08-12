package org.hackru.oneapp.hackru.ui.main.announcements

<<<<<<< HEAD
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(val announcementsRepository: AnnouncementsRepository) : ViewModel() {

    val announcements: LiveData<Resource<List<AnnouncementsModel.Announcement>>> get() = announcementsRepository.loadAllAnnouncements()
=======
import android.arch.lifecycle.ViewModel
import android.util.Log
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(private val announcementsRepository: AnnouncementsRepository) : ViewModel() {

    // TODO: Implement time stamp check on when announcements were last cached
    val announcements by lazy {
        announcementsRepository.loadAllAnnouncements()
    }
>>>>>>> d69d810010cf862c0eddee67b3a7d58691f5f572

}
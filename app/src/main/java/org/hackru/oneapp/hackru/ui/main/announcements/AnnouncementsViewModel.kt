package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(val announcementsRepository: AnnouncementsRepository) : ViewModel() {

    val announcements: LiveData<Resource<List<AnnouncementsModel.Announcement>>> get() = announcementsRepository.loadAllAnnouncements()

}
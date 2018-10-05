package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.hackru.oneapp.hackru.api.Resource
import org.hackru.oneapp.hackru.api.models.AnnouncementsModel
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(private val announcementsRepository: AnnouncementsRepository) : ViewModel() {

    var announcements: MediatorLiveData<Resource<List<AnnouncementsModel.Announcement>>>? = null
        get() {
            if(field == null) {
                field = announcementsRepository.loadAnnouncementsFromDatabase()
            } else {
                announcementsRepository.refreshAnnouncements(field)
            }
            return field
        }

}
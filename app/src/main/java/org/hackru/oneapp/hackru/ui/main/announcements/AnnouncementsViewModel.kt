package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.ViewModel
import android.util.Log
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(private val announcementsRepository: AnnouncementsRepository) : ViewModel() {

    // TODO: Implement time stamp check on when announcements were last cached
    val announcements by lazy {
        announcementsRepository.loadAllAnnouncements()
    }

}
package org.hackru.oneapp.hackru.ui.main.announcements

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import org.hackru.oneapp.hackru.repositories.AnnouncementsRepository
import javax.inject.Inject

class AnnouncementsViewModelFactory @Inject constructor(val announcementsRepository: AnnouncementsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.equals(AnnouncementsViewModel::class.java)) {
            return AnnouncementsViewModel(announcementsRepository) as T
        } else {
            throw RuntimeException("Unable to create ${modelClass::class.simpleName} with ${this::class.simpleName}")
        }
    }

}
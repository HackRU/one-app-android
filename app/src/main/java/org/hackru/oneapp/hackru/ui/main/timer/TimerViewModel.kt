package org.hackru.oneapp.hackru.ui.main.timer

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.hackru.oneapp.hackru.api.Resource

class TimerViewModel : ViewModel() {

    val deadline: LiveData<Resource<Long>> = kotlin.run {
        val livedata = MutableLiveData<Resource<Long>>()
        livedata.value = Resource.success(1537406058)
        livedata
    }

}
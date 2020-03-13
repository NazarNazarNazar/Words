package com.antnzr.words.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antnzr.words.MainApplication
import com.antnzr.words.data.SubFilesRepository
import com.antnzr.words.workers.Coroutines
import kotlinx.coroutines.Job

class SubFilesViewModel(
    private val repository: SubFilesRepository
) : ViewModel() {

    private lateinit var job: Job
    private val _subFiles = MutableLiveData<ArrayList<String>>()

    val subFiles: LiveData<ArrayList<String>>
        get() = _subFiles

    fun getSubFiles() {
        job = Coroutines.ioThenMain(
            { repository.getLocalSubtitles(MainApplication.applicationContext()) },
            { _subFiles.value = it }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}

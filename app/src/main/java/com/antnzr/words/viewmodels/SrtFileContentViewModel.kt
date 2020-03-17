package com.antnzr.words.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antnzr.words.MainApplication
import com.antnzr.words.data.Srt
import com.antnzr.words.data.SrtFileContentRepository
import com.antnzr.words.workers.Coroutines
import kotlinx.coroutines.Job

class SrtFileContentViewModel(
    private val repository: SrtFileContentRepository,
    private val subName: String
) : ViewModel() {

    private lateinit var job: Job
    private val _srts = MutableLiveData<ArrayList<Srt>>()

    val srts: LiveData<ArrayList<Srt>>
        get() = _srts

    fun getSrts() {
        job = Coroutines.ioThenMain(
            { repository.getSrtContent(MainApplication.applicationContext(), subName) },
            { _srts.value = it }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}

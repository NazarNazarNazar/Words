package com.antnzr.words.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antnzr.words.MainApplication
import com.antnzr.words.data.LocalSubFilesRepository
import com.antnzr.words.data.SubtitleRepository
import com.antnzr.words.domain.Subtitle
import com.antnzr.words.workers.Coroutines
import kotlinx.coroutines.Job

class SubtitlesViewModel(
    private val repository: SubtitleRepository,
    private val subName: String
) : ViewModel() {

    private lateinit var job: Job
    private val _subtitles = MutableLiveData<ArrayList<Subtitle>>()

    val subtitles: LiveData<ArrayList<Subtitle>>
        get() = _subtitles

    fun getSubtitles() {
        job = Coroutines.ioThenMain(
            { repository.getSubtitles(MainApplication.applicationContext(), subName) },
            { _subtitles.value = it }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}

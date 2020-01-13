package com.antnzr.words

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antnzr.words.utils.Coroutines
import kotlinx.coroutines.Job

class WordsViewModel(
    private val service: LocalTsvWords
) : ViewModel() {

    private lateinit var job: Job
    private val _words = MutableLiveData<Collection<WordPair>>()

    val words: LiveData<Collection<WordPair>>
        get() = _words

    fun getWords() {
        job = Coroutines.ioThenMain(
            { service.getWords(MainApplication.applicationContext().applicationContext) },
            { _words.value = it }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}

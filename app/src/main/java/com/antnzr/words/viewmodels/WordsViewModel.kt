package com.antnzr.words.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antnzr.words.data.LocalTsvWordsRepository
import com.antnzr.words.MainApplication
import com.antnzr.words.data.WordPair
import com.antnzr.words.workers.Coroutines
import kotlinx.coroutines.Job

class WordsViewModel(
    private val repository: LocalTsvWordsRepository
) : ViewModel() {

    private lateinit var job: Job
    private val _words = MutableLiveData<Collection<WordPair>>()

    val words: LiveData<Collection<WordPair>>
        get() = _words

    fun getWords() {
        job = Coroutines.ioThenMain(
            { repository.getWords(MainApplication.applicationContext()) },
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

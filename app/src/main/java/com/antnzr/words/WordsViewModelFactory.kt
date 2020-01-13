package com.antnzr.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WordsViewModelFactory(
    private val service: LocalTsvWords
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordsViewModel(service) as T
    }
}
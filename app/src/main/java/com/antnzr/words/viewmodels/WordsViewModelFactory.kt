package com.antnzr.words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antnzr.words.data.LocalTsvWords

class WordsViewModelFactory(
    private val service: LocalTsvWords
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordsViewModel(service) as T
    }
}
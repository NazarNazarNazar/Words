package com.antnzr.words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antnzr.words.data.LocalTsvWordsRepository

class WordsViewModelFactory(
    private val repository: LocalTsvWordsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordsViewModel(repository) as T
    }
}
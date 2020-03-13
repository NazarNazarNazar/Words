package com.antnzr.words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antnzr.words.data.LocalSubFilesRepository
import com.antnzr.words.data.SubFilesRepository
import com.antnzr.words.data.SubtitleRepository

class SubtitlesViewModelFactory(
    private val repository: SubtitleRepository,
    private val subName: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SubtitlesViewModel(repository, subName) as T
    }
}
package com.antnzr.words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antnzr.words.data.LocalSubFilesRepository
import com.antnzr.words.data.SrtFileContentRepository
import com.antnzr.words.data.SubFilesRepository
import com.antnzr.words.data.SubtitleRepository

class SrtFleContentViewModelFactory(
    private val repository: SrtFileContentRepository,
    private val subName: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SrtFileContentViewModel(repository, subName) as T
    }
}
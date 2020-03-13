package com.antnzr.words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.antnzr.words.data.SubFilesRepository

class SubFilesViewModelFactory(
    private val repository: SubFilesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SubFilesViewModel(repository) as T
    }
}
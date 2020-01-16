package com.antnzr.words.utils

import android.view.View
import com.antnzr.words.data.WordPair


interface RecyclerViewClickListener {
    fun onClick(view: View, wordPair: WordPair)
}
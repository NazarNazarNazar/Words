package com.antnzr.words.utils

import android.view.View


interface RecyclerViewClickListener<T> {
    fun onClick(view: View, data: T)
}
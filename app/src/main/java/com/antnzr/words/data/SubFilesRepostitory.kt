package com.antnzr.words.data

import android.content.Context
import com.antnzr.words.utils.SUBTITLE_EXTENSION
import com.antnzr.words.utils.SUB_LOCAL_DIR
import java.io.File

interface SubFilesRepository {
    fun getLocalSubtitles(context: Context): ArrayList<String>
}

class LocalSubFilesRepository : SubFilesRepository {

    override fun getLocalSubtitles(context: Context): ArrayList<String> {
        val subDir: File? = context.getExternalFilesDir(SUB_LOCAL_DIR)

        return getSubNames(subDir)
    }

    private fun getSubNames(file: File?): ArrayList<String> {
        if (file == null || !file.exists()) {
            return ArrayList()
        }

        return ArrayList(file.walkTopDown()
            .filter { f -> f.name.endsWith(SUBTITLE_EXTENSION) }
            .map { f -> f.name }
            .toList())
    }
}
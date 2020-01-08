package com.antnzr.words

import android.content.Context
import com.antnzr.words.WordPair.Companion.FROM_INDEX
import com.antnzr.words.WordPair.Companion.TO_INDEX
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

interface WordsService<E> {
    fun getWords(context: Context): List<E>
}

class LocalTsvWords : WordsService<WordPair> {

    override fun getWords(context: Context): List<WordPair> {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.google_translate_words)
        val reader = BufferedReader(InputStreamReader(inputStream, Charset.defaultCharset()))

        val list = arrayListOf<WordPair>()
        reader.forEachLine { line ->
            val tokens = line.split("\t")
            val wordPair = WordPair(tokens[FROM_INDEX], tokens[TO_INDEX])
            list.add(wordPair)
        }

        return list
    }

}

data class WordPair(var from: String, var to: String) {
    companion object {
        const val FROM_INDEX: Int = 2
        const val TO_INDEX: Int = 3
    }
}
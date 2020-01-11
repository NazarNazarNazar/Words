package com.antnzr.words

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import kotlin.random.Random

interface WordsService<E> {
    fun getWords(context: Context): Collection<E>
    fun getWord(context: Context): E
}

class LocalTsvWords : WordsService<WordPair> {

    companion object {
        val list = linkedSetOf<WordPair>()
    }

    override fun getWords(context: Context): Collection<WordPair> {
        if (list.size != 0) {
            return list
        }

        try {
            val inputStream: InputStream =
                context.resources.openRawResource(R.raw.google_translate_words)
            val reader = BufferedReader(InputStreamReader(inputStream, Charset.defaultCharset()))

            reader.forEachLine { line ->
                val tokens = line.split("\t")

                if (tokens.size == TOKENS_SIZE) {
                    val wordPair = WordPair(tokens[FROM_INDEX], tokens[TO_INDEX])
                    list.add(wordPair)
                }
            }

            return list
        } catch (exception: Exception) {
            throw RuntimeException("Something went wrong. Exception: ${exception.message}")
        }
    }

    override fun getWord(context: Context): WordPair {
        val wordPairs = getWords(context)

        return wordPairs.elementAt(Random.nextInt(0, wordPairs.size))
    }
}

data class WordPair(var from: String, var to: String, var isDisplay: Boolean = true) {
    override fun toString(): String {
        return "$from | $to"
    }
}
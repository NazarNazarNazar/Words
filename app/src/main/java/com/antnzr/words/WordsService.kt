package com.antnzr.words

import android.content.Context
import java.nio.charset.Charset
import kotlin.random.Random

interface WordsService<E> {
    fun getWords(context: Context): Collection<E>
    fun getWord(context: Context): E
}

class LocalTsvWords : WordsService<WordPair> {

    companion object {
        val wordPairs = linkedSetOf<WordPair>()
    }

    override fun getWords(context: Context): Collection<WordPair> {
        if (wordPairs.size != 0) {
            return wordPairs
        }

        try {
            context.resources.openRawResource(R.raw.google_translate_words)
                .bufferedReader(Charset.defaultCharset())
                .use {
                    it.forEachLine { line ->
                        val tokens = line.split("\t")
                        if (tokens.size == TOKENS_SIZE) {
                            wordPairs.add(WordPair(tokens[FROM_INDEX], tokens[TO_INDEX]))
                        }
                    }
                }

            return wordPairs
        } catch (exception: Exception) {
            return emptySet()
        }
    }

    override fun getWord(context: Context): WordPair {
        val wordPairs: Collection<WordPair> = getWords(context)
        return try {
            wordPairs.elementAt(Random.nextInt(0, wordPairs.size))
        } catch (exception: Exception) {
            WordPair("", "")
        }
    }
}

data class WordPair(var from: String, var to: String, var isDisplay: Boolean = true) {
    override fun toString(): String {
        return "$from | $to"
    }
}
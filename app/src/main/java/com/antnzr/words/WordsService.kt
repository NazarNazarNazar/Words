package com.antnzr.words

import android.content.Context
import com.antnzr.words.utils.*
import java.nio.charset.Charset
import kotlin.random.Random

interface WordsService<E> {
    fun getWords(context: Context): Collection<E>
    fun getRandomWord(context: Context): E?
    fun getNextWord(context: Context?): E?
    fun getPreviousWord(context: Context?): E?
    fun saveCurrentWord(context: Context?, word: String?)
    fun getCurrentWord(context: Context?): E?
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

    override fun getRandomWord(context: Context): WordPair? {
        val wordPairs: Collection<WordPair> = getWords(context)
        return try {
            wordPairs.elementAt(Random.nextInt(0, wordPairs.size))
        } catch (exception: Exception) {
            WordPair("", "")
        }
    }

    override fun getNextWord(context: Context?): WordPair? {
        val words: Collection<WordPair>? = context?.let { getWords(it) }
        val currentWordPairIndex = currentWordPairIndex(words, currentWordFromPref(context))

        if (words?.size == currentWordPairIndex) {
            return words?.first()
        }

        return currentWordPairIndex?.plus(1)?.let { words?.elementAt(it) }
    }

    override fun getPreviousWord(context: Context?): WordPair? {
        val words: Collection<WordPair>? = context?.let { getWords(it) }
        val currentWordPairIndex = currentWordPairIndex(words, currentWordFromPref(context))

        if (currentWordPairIndex == 0) {
            return words?.last()
        }

        return currentWordPairIndex?.minus(1)?.let { words?.elementAt(it) }
    }

    override fun saveCurrentWord(context: Context?, word: String?) {
        val prefs = context?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = prefs?.edit()

        editor?.putString(CURRENT_WORD, word)
        editor?.apply()
    }

    override fun getCurrentWord(context: Context?): WordPair? {
        val words: Collection<WordPair>? = context?.let { getWords(it) }

        return words?.firstOrNull { wordPair -> wordPair.from == currentWordFromPref(context) }
    }
}

private fun currentWordPairIndex(words: Collection<WordPair>?, word: String?): Int? {
    return words?.indexOfFirst { pair -> pair.from == word }
}

private fun currentWordFromPref(context: Context?): String? {
    val prefs = context?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    return prefs?.getString(CURRENT_WORD, "")
}

data class WordPair(var from: String, var to: String, var isDisplay: Boolean = true) {
    override fun toString(): String {
        return "$from | $to"
    }
}
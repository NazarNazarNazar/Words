package com.antnzr.words.utils

import com.antnzr.words.data.WordPair
import java.util.*


fun contextReversoUrl(wordPair: WordPair): String {
    return "https://context.reverso.net/перевод/" +
            "${wordPair.langFrom.toLowerCase(Locale.getDefault())}-" +
            "${wordPair.langTo.toLowerCase(Locale.getDefault())}/" +
            wordPair.from.split("\\s".toRegex()).joinToString(separator = "+")
}

fun googleTranslateUrl(wordPair: WordPair): String {
    return "https://translate.google.com/" +
            "#view=home" +
            "&op=translate" +
            "&sl=${getLang(wordPair.langFrom)}" +
            "&tl=${getLang(wordPair.langTo)}" +
            "&text=${wordPair.from}"
}

private fun getLang(str: String): String {
    when (str.toLowerCase(Locale.getDefault())) {
        "english",
        "английский",
        "ingles" -> return "en"
        "russian",
        "русский",
        "ruso" -> return "ru"
    }
    return "en"
}

package com.antnzr.words.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.antnzr.words.R
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.CONTEXT_REVERSO
import com.antnzr.words.utils.GOOGLE_TRANSLATE
import com.antnzr.words.utils.WORD_NEED_DETAILS
import java.util.*


class WordDetailsActivity : AppCompatActivity() {

    private val TAG = WordDetailsActivity::class.java.simpleName

    private var wordWebView: WebView? = null
    private var spinner: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: started...")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_details)

        spinner = findViewById(R.id.spinner)

        wordWebView = findViewById(R.id.word_web_view)
        prepareWebView(wordWebView, spinner as ProgressBar)

        if (intent.hasExtra(GOOGLE_TRANSLATE) &&
            intent.hasExtra(WORD_NEED_DETAILS)
        ) {
            wordWebView?.loadUrl(
                googleTranslateUrl(intent.getParcelableExtra<Parcelable>(WORD_NEED_DETAILS) as WordPair)
            )
        } else if (intent.hasExtra(CONTEXT_REVERSO) &&
            intent.hasExtra(WORD_NEED_DETAILS)
        ) {
            wordWebView?.loadUrl(
                contextReversoUrl(intent.getParcelableExtra<Parcelable>(WORD_NEED_DETAILS) as WordPair)
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun prepareWebView(webView: WebView?, spinner: ProgressBar) {
    val settings = webView?.settings
    settings?.javaScriptEnabled = true
    settings?.javaScriptCanOpenWindowsAutomatically = false
    settings?.setSupportMultipleWindows(true)
    settings?.setSupportZoom(true)

    webView?.webViewClient = MyWebClient(spinner)
    webView?.isVerticalScrollBarEnabled = true
    webView?.isHorizontalScrollBarEnabled = true
}

private class MyWebClient(spinner: ProgressBar) : WebViewClient() {
    private var spinner: ProgressBar? = spinner

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        spinner?.visibility = View.GONE
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        view.loadUrl(url)
        return true
    }
}

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

fun getLang(str: String): String {
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

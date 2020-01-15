package com.antnzr.words.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.antnzr.words.R
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.*


class WordDetailsActivity : AppCompatActivity() {

    private var wordWebView: WebView? = null
    private var spinner: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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
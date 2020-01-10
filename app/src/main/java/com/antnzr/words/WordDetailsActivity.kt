package com.antnzr.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity


class WordDetailsActivity : AppCompatActivity() {

    private val TAG = WordDetailsActivity::class.java.simpleName
    private val url =
        "https://translate.google.com/?hl=ru#view=home&op=translate&sl=auto&tl=ru&text="

    private var wordWebView: WebView? = null
    private var spinner: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: started...")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_details)

        spinner = findViewById(R.id.spinner)

        wordWebView = findViewById(R.id.word_web_view)
        prepareWebView(wordWebView, spinner as ProgressBar)

        if (intent.hasExtra(WordWidget.WORD)) {
            val word = intent.getStringExtra(WordWidget.WORD)
            wordWebView?.loadUrl(url + word)
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

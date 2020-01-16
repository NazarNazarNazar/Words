package com.antnzr.words.view.wordDetails

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.antnzr.words.R
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.*

class WordWebFragment : Fragment() {
    private var wordPair: WordPair? = null
    private var resource: String? = null

    private var listener: OnWordWebFragmentInteractionListener? = null

    private var wordWebView: WebView? = null
    private var spinner: ProgressBar? = null

    companion object {
        @JvmStatic
        fun newInstance(resource: String, wordPair: Parcelable) =
            WordWebFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WORD_NEED_DETAILS, wordPair)
                    putString(WEB_RESOURCE, resource)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wordPair = it.getParcelable(WORD_NEED_DETAILS)
            resource = it.getString(WEB_RESOURCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_word_web, container, false)
        wordWebView = view.findViewById(R.id.word_web_view)
        spinner = view.findViewById(R.id.spinner)
        prepareWebView(wordWebView, spinner)

        when (resource) {
            GOOGLE_TRANSLATE -> wordWebView?.loadUrl(googleTranslateUrl(wordPair))
            CONTEXT_REVERSO -> wordWebView?.loadUrl(contextReversoUrl(wordPair))
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWordWebFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnWordWebFragmentInteractionListener {
        fun onFragmentInteraction()
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun prepareWebView(webView: WebView?, spinner: ProgressBar?) {
    val settings = webView?.settings
    settings?.javaScriptEnabled = true
    settings?.javaScriptCanOpenWindowsAutomatically = false
    settings?.setSupportMultipleWindows(true)
    settings?.setSupportZoom(true)

    webView?.webViewClient = MyWebClient(spinner)
    webView?.isVerticalScrollBarEnabled = true
    webView?.isHorizontalScrollBarEnabled = true
}

package com.antnzr.words.view.subtitle

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.antnzr.words.MainApplication
import com.antnzr.words.R
import com.antnzr.words.srt.Srt
import com.antnzr.words.srt.SrtFileContentHandler
import com.antnzr.words.utils.SUBTITLE_FILE_NAME

private val TAG = SubtitleFragment::class.java.simpleName

class SubtitleFragment : Fragment() {
    private var sub: String? = null

    private lateinit var subView: TextView

    private var listener: OnSubtitleFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sub = it.getString(SUBTITLE_FILE_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subView = view.findViewById(R.id.sub)
        subView.movementMethod = LinkMovementMethod.getInstance()

        val contentHandler: SrtFileContentHandler = SrtFileContentHandler()
        val content: ArrayList<Srt> =
            contentHandler.getSrtContent(MainApplication.applicationContext(), sub!!)

        subView.text = SrtFileContentHandler.makeSpannable(content)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subtitle, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(subtitleFileName: String) =
            SubtitleFragment().apply {
                arguments = Bundle().apply {
                    putString(SUBTITLE_FILE_NAME, subtitleFileName)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSubtitleFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSubtitleFragmentInteractionListener {
        fun onFragmentInteraction()
    }
}

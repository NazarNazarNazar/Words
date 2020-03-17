package com.antnzr.words.view.subtitle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.antnzr.words.R
import com.antnzr.words.adapters.SrtFileContentAdapter
import com.antnzr.words.data.SrtFileContentRepositoryImpl
import com.antnzr.words.utils.SUBTITLE_FILE_NAME
import com.antnzr.words.viewmodels.SrtFileContentViewModel
import com.antnzr.words.viewmodels.SrtFleContentViewModelFactory
import kotlinx.android.synthetic.main.fragment_srt_content.*

private val TAG = SubtitleFragment::class.java.simpleName

class SubtitleFragment : Fragment() {
    private var sub: String? = null

    private lateinit var viewModelFactory: SrtFleContentViewModelFactory
    private lateinit var viewModel: SrtFileContentViewModel

    private val repository = SrtFileContentRepositoryImpl()

    private var listener: OnSubtitleFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sub = it.getString(SUBTITLE_FILE_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelFactory = SrtFleContentViewModelFactory(repository, sub!!)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SrtFileContentViewModel::class.java)

        viewModel.getSrts()

        viewModel.srts.observe(viewLifecycleOwner, Observer { srts ->
            sub_text_recycler.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = SrtFileContentAdapter(srts)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_srt_content, container, false)
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

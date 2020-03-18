package com.antnzr.words.view.subtitle

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.antnzr.words.R
import com.antnzr.words.adapters.SrtFileContentAdapter
import com.antnzr.words.data.Srt
import com.antnzr.words.data.SrtFileContentRepositoryImpl
import com.antnzr.words.utils.RecyclerViewClickListener
import com.antnzr.words.utils.SUBTITLE_FILE_NAME
import com.antnzr.words.viewmodels.SrtFileContentViewModel
import com.antnzr.words.viewmodels.SrtFleContentViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_srt_content.*

private val TAG = SrtContentFragment::class.java.simpleName

class SrtContentFragment : Fragment(),
    RecyclerViewClickListener<Srt> ,
    SrtFileContentAdapter.OnSrtTextClickListener {

    private var srtFileName: String? = null

    private var subWordEdit: TextInputLayout? = null

    private lateinit var viewModelFactory: SrtFleContentViewModelFactory
    private lateinit var viewModel: SrtFileContentViewModel

    private val repository = SrtFileContentRepositoryImpl()

    private var listener: OnSrtContentFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            srtFileName = it.getString(SUBTITLE_FILE_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelFactory = SrtFleContentViewModelFactory(repository, srtFileName!!)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SrtFileContentViewModel::class.java)

        viewModel.getSrts()

        viewModel.srts.observe(viewLifecycleOwner, Observer { srts ->
            sub_text_recycler.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = SrtFileContentAdapter(srts, this, this)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_srt_content, container, false)

        subWordEdit = view.findViewById(R.id.sub_word_edit)
        subWordEdit?.clearFocus()
        subWordEdit?.error = null
        setTextWatcher(subWordEdit!!)

        return view
    }

    private fun setTextWatcher(til: TextInputLayout) {
        val mTextWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                til.isErrorEnabled = s.length < 0
            }

            override fun afterTextChanged(s: Editable) {}
        }
        if (til.editText != null) {
            til.editText!!.addTextChangedListener(mTextWatcher)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(srtFileName: String) =
            SrtContentFragment().apply {
                arguments = Bundle().apply {
                    putString(SUBTITLE_FILE_NAME, srtFileName)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSrtContentFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSrtContentFragmentInteractionListener {
        fun onFragmentInteraction()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onClick(view: View, data: Srt) {
        println(data.timeCode.beginning)
    }

    override fun onTextClick(view: View, word: String) {
        val value: String? = word
            .replace("[\n\r]".toRegex(), " ")
            .replace("(<.*?>)|(&.*?;)".toRegex(), "")
            .replace("\\s{2,}".toRegex(), " ")+ " "
//            .filter { it.isLetterOrDigit() || it.isWhitespace() }

        subWordEdit?.editText?.append(value)
    }
}

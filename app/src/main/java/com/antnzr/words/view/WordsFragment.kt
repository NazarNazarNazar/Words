package com.antnzr.words.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antnzr.words.R
import com.antnzr.words.adapters.RecyclerViewClickListener
import com.antnzr.words.adapters.WordAdapter
import com.antnzr.words.data.LocalTsvWordsRepository
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.CONTEXT_REVERSO
import com.antnzr.words.utils.GOOGLE_TRANSLATE
import com.antnzr.words.utils.WORD_NEED_DETAILS
import com.antnzr.words.viewmodels.WordsViewModel
import com.antnzr.words.viewmodels.WordsViewModelFactory
import kotlinx.android.synthetic.main.words_fragment.*


class WordsFragment : Fragment(), RecyclerViewClickListener {
    private val TAG = WordsFragment::class.java.simpleName

    companion object {
        fun newInstance() = WordsFragment()
    }

    private lateinit var viewModelFactory: WordsViewModelFactory
    private lateinit var viewModel: WordsViewModel

    private val repository = LocalTsvWordsRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.words_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModelFactory = WordsViewModelFactory(repository)
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(WordsViewModel::class.java)

        viewModel.getWords()

        viewModel.words.observe(viewLifecycleOwner, Observer { words ->
            words_list.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = WordAdapter(words, this)
                it.layoutManager
                    ?.scrollToPosition(words.indexOf(repository.getCurrentWord(context)))
            }
        })
    }

    override fun onClick(view: View, wordPair: WordPair) {
        when (view.id) {
            R.id.word -> {
                repository.saveCurrentWord(requireContext(), wordPair.from)
                words_list.adapter?.notifyDataSetChanged()
                updateWordWidget(context)
            }
            R.id.google_translate_btn -> {
                showWordDetailsWith(GOOGLE_TRANSLATE, wordPair)
            }
            R.id.context_reverso_btn -> {
                showWordDetailsWith(CONTEXT_REVERSO, wordPair)
            }
        }
    }

    private fun showWordDetailsWith(resource: String, wordPair: WordPair) {
        val intent = Intent(context, WordDetailsActivity::class.java)
        intent.putExtra(WORD_NEED_DETAILS, wordPair)
        intent.putExtra(resource, resource)

        startActivity(intent)
    }
}


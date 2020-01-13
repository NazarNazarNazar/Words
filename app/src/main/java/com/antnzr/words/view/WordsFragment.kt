package com.antnzr.words.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.antnzr.words.*
import com.antnzr.words.utils.WordAdapter
import kotlinx.android.synthetic.main.words_fragment.*


class WordsFragment : Fragment() {
    private val TAG = WordsFragment::class.java.simpleName

    companion object {
        fun newInstance() = WordsFragment()
    }

    private lateinit var viewModelFactory: WordsViewModelFactory
    private lateinit var viewModel: WordsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.words_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated: started")

        viewModelFactory =
            WordsViewModelFactory(LocalTsvWords())
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(WordsViewModel::class.java)

        viewModel.getWords()

        viewModel.words.observe(viewLifecycleOwner, Observer { words ->
            words_list.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = WordAdapter(words)
            }
        })
    }
}

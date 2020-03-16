package com.antnzr.words.view

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.antnzr.words.R
import com.antnzr.words.adapters.SearchSubtitleAdapter
import com.antnzr.words.data.SubtitleRepository
import com.antnzr.words.data.SubtitleRepositoryImpl
import com.antnzr.words.domain.Subtitle
import com.antnzr.words.utils.RecyclerViewClickListener
import com.antnzr.words.utils.bind
import com.antnzr.words.viewmodels.SubtitlesViewModel
import com.antnzr.words.viewmodels.SubtitlesViewModelFactory
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), RecyclerViewClickListener<Subtitle> {
    private val TAG = this.javaClass.simpleName

    private val emptyView: TextView by bind(R.id.search_empty)

    private lateinit var viewModelFactory: SubtitlesViewModelFactory
    private lateinit var viewModel: SubtitlesViewModel

    private val repository: SubtitleRepository = SubtitleRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {

        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            viewModelFactory = SubtitlesViewModelFactory(repository, query)
            viewModel = ViewModelProvider(this, viewModelFactory)
                .get(SubtitlesViewModel::class.java)

            viewModel.getSubtitles()

            viewModel.subtitles.observe(this, Observer { subtitles ->
                checkIfEmpty(subtitles)

                search_list.also {
                    it.layoutManager = LinearLayoutManager(this)
                    it.setHasFixedSize(true)
                    it.adapter = SearchSubtitleAdapter(subtitles, this)
                }
            })
        }
    }

    private fun checkIfEmpty(subtitles: ArrayList<Subtitle>) {
        if (subtitles.isNullOrEmpty()) {
            emptyView.visibility = View.VISIBLE
            search_list.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            search_list.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View, data: Subtitle) {
//        Toast.makeText(this, data.subFileName, Toast.LENGTH_LONG).show()
        when (view.id) {
            R.id.search_res_item -> {
                repository.downloadSubtitle(this@SearchActivity, data.zipDownloadLink)
            }
        }
    }
}
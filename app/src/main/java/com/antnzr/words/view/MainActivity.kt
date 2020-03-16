package com.antnzr.words.view

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.antnzr.words.R
import com.antnzr.words.adapters.SubFilesAdapter
import com.antnzr.words.data.LocalSubFilesRepository
import com.antnzr.words.utils.RecyclerViewClickListener
import com.antnzr.words.utils.SUBTITLE_FILE_NAME
import com.antnzr.words.view.subtitle.SubtitleActivity
import com.antnzr.words.viewmodels.SubFilesViewModel
import com.antnzr.words.viewmodels.SubFilesViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    RecyclerViewClickListener<String> {
    private val TAG = this.javaClass.simpleName

    private lateinit var viewModelFactory: SubFilesViewModelFactory
    private lateinit var viewModel: SubFilesViewModel

    private val repository: LocalSubFilesRepository = LocalSubFilesRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModelFactory = SubFilesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(SubFilesViewModel::class.java)

        viewModel.getSubFiles()

        viewModel.subFiles.observe(this, Observer { subtitles ->
            Log.d(TAG, "${subtitles.size}")
            main_list.also {
                it.layoutManager = LinearLayoutManager(this)
                it.setHasFixedSize(true)
                it.adapter = SubFilesAdapter(subtitles, this@MainActivity)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(applicationContext, SearchActivity::class.java)))
        }

        menu.findItem(R.id.search).let {
            val searchView = it?.actionView as SearchView

            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent: Intent = Intent(applicationContext, SearchActivity::class.java)
                        intent.putExtra(SearchManager.QUERY, query)
                        startActivity(intent)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        currentFocus?.clearFocus()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> true
            else -> false
        }
    }

    override fun onClick(view: View, data: String) {
        val intent = Intent(this, SubtitleActivity::class.java)
        intent.putExtra(SUBTITLE_FILE_NAME, data)
        startActivity(intent)
    }
}



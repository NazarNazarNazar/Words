package com.antnzr.words.view

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.adapters.SubFilesAdapter
import com.antnzr.words.data.LocalSubFilesRepository
import com.antnzr.words.utils.RecyclerViewClickListener
import com.antnzr.words.utils.SUBTITLE_FILE_NAME
import com.antnzr.words.view.subtitle.SrtContentActivity
import com.antnzr.words.viewmodels.SubFilesViewModel
import com.antnzr.words.viewmodels.SubFilesViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

private val TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity(),
    RecyclerViewClickListener<String> {

    private var adapter: SubFilesAdapter? = null
    private val paint = Paint()

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
                adapter = SubFilesAdapter(subtitles, this@MainActivity)
                it.adapter = adapter
                enableSwipe(main_list)
            }
        })
    }

    private fun enableSwipe(recyclerView: RecyclerView) {
        val simpleItemTouchCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition

                    if (direction == ItemTouchHelper.LEFT) {
                        adapter!!.removeItem(position)
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        adapter!!.removeItem(position)
                    }
                }

                override fun onChildDraw(
                    canvas: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    paint.color =
                        ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryLight)
                    val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.delete)

                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        val view = viewHolder.itemView
                        val height = view.bottom.toFloat() - view.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            val background =
                                RectF(
                                    view.left.toFloat(),
                                    view.top.toFloat(),
                                    dX,
                                    view.bottom.toFloat()
                                )
                            canvas.drawRect(background, paint)

                            val iconDest = RectF(
                                view.left.toFloat() + width,
                                view.top.toFloat() + width,
                                view.left.toFloat() + 2 * width,
                                view.bottom.toFloat() - width
                            )
                            canvas.drawBitmap(icon, null, iconDest, paint)
                        } else {
                            val background =
                                RectF(
                                    view.right.toFloat() + dX,
                                    view.top.toFloat(),
                                    view.right.toFloat(),
                                    view.bottom.toFloat()
                                )
                            canvas.drawRect(background, paint)

                            val iconDest = RectF(
                                view.right.toFloat() - 2 * width,
                                view.top.toFloat() + width,
                                view.right.toFloat() - width,
                                view.bottom.toFloat() - width
                            )
                            canvas.drawBitmap(icon, null, iconDest, paint)
                        }
                    }

                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(applicationContext, SearchActivity::class.java)
                )
            )
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
        val intent = Intent(this, SrtContentActivity::class.java)
        intent.putExtra(SUBTITLE_FILE_NAME, data)
        startActivity(intent)
    }
}



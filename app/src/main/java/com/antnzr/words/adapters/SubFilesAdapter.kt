package com.antnzr.words.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.MainApplication
import com.antnzr.words.R
import com.antnzr.words.data.LocalSubFilesRepository
import com.antnzr.words.utils.RecyclerViewClickListener

class SubFilesAdapter(
    private var subtitles: ArrayList<String>,
    private val listener: RecyclerViewClickListener<String>
) :
    RecyclerView.Adapter<SubFilesAdapter.SearchSubtitleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSubtitleViewHolder {
        return SearchSubtitleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        )
    }

    override fun getItemCount(): Int = subtitles.size

    override fun onBindViewHolder(holder: SearchSubtitleViewHolder, position: Int) {
        holder.subFileItem.text = subtitles.elementAt(position)

        holder.subFileItem.setOnClickListener {
            listener.onClick(holder.subFileItem, subtitles.elementAt(position))
        }
    }

    fun restoreItem(string: String, position: Int) {
        subtitles.add(position, string)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        val repository = LocalSubFilesRepository()

        val isSuccessfullyDeleted = repository.deleteSrtFile(
            MainApplication.applicationContext(),
            subtitles.elementAt(position))

        if (isSuccessfullyDeleted) {
            subtitles.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, subtitles.size)
        }
    }

    inner class SearchSubtitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subFileItem: TextView = view.findViewById(R.id.file_name)
    }
}
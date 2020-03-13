package com.antnzr.words.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.domain.Subtitle
import com.antnzr.words.utils.RecyclerViewClickListener

class SearchSubtitleAdapter(
    private val subtitles: List<Subtitle>,
    private val listener: RecyclerViewClickListener<Subtitle>
) :
    RecyclerView.Adapter<SearchSubtitleAdapter.SearchSubtitleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSubtitleViewHolder {
        return SearchSubtitleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int = subtitles.size

    override fun onBindViewHolder(holder: SearchSubtitleViewHolder, position: Int) {
        holder.searchSubItem.text = subtitles.elementAt(position).subFileName

        holder.searchSubItem.setOnClickListener {
            listener.onClick(holder.searchSubItem, subtitles.elementAt(position))
        }
    }

    inner class SearchSubtitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val searchSubItem: TextView = view.findViewById(R.id.search_res_item)
    }
}
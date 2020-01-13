package com.antnzr.words.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.WordPair

class WordAdapter(
    private val words: Collection<WordPair>
) : RecyclerView.Adapter<WordAdapter.WordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        return WordsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {

        holder.wordTextView.text = holder.itemView.context
            .getString(
                R.string.word_list,
                words.elementAt(position).from,
                words.elementAt(position).to
            )
    }

    override fun getItemCount(): Int {
        return words.size
    }

    inner class WordsViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        val wordTextView: TextView = view.findViewById(R.id.word)
    }
}
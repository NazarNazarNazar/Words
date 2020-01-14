package com.antnzr.words.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.data.LocalTsvWordsRepository
import com.antnzr.words.data.WordPair

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

        val context = holder.itemView.context

        holder.wordTextView.text = context.getString(
            R.string.word_list,
            words.elementAt(position).from,
            words.elementAt(position).to
        )

        if (words.indexOf(LocalTsvWordsRepository().getCurrentWord(context)) == position) {
            holder.wordTextView.setTextColor(context.getColor(R.color.colorAccent))
        } else {
            holder.wordTextView.setTextColor(context.getColor(R.color.colorPrimaryDark))
        }
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
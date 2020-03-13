package com.antnzr.words.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.data.LocalTsvWordsRepository
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.RecyclerViewClickListener

class WordAdapter(
    private val words: Collection<WordPair>,
    private val listener: RecyclerViewClickListener<WordPair>
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
            holder.wordTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        } else {
            holder.wordTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        }

        holder.wordTextView.setOnClickListener {
            listener.onClick(holder.wordTextView, words.elementAt(position))
        }

        holder.googleTranslateBtn.setOnClickListener {
            listener.onClick(holder.googleTranslateBtn, words.elementAt(position))
        }

        holder.contextReversoBtn.setOnClickListener {
            listener.onClick(holder.contextReversoBtn, words.elementAt(position))
        }
    }

    override fun getItemCount(): Int = words.size

    inner class WordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val wordTextView: TextView = view.findViewById(R.id.word)
        val googleTranslateBtn: ImageButton = view.findViewById(R.id.google_translate_btn)
        val contextReversoBtn: ImageButton = view.findViewById(R.id.context_reverso_btn)
    }
}
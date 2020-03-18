package com.antnzr.words.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.data.Srt
import com.antnzr.words.srt.ClickableTextView
import com.antnzr.words.utils.RecyclerViewClickListener

class SrtFileContentAdapter(
    private var srts: ArrayList<Srt>,
    private val listener: RecyclerViewClickListener<Srt>,
    private val wordListener: OnSrtTextClickListener
) :
    RecyclerView.Adapter<SrtFileContentAdapter.SrtFileContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SrtFileContentViewHolder {
        return SrtFileContentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        )
    }

    override fun getItemCount(): Int = srts.size

    override fun onBindViewHolder(holder: SrtFileContentViewHolder, position: Int) {
        holder.srtClickableText.setTextWithClickableWords(srts.elementAt(position).text)

        holder.srtClickableText.setOnClickListener {
            holder.srtClickableText.word?.let { data -> wordListener.onTextClick(holder.srtClickableText, data) }
            listener.onClick(holder.srtClickableText, srts.elementAt(position))
        }
    }

    inner class SrtFileContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val srtClickableText: ClickableTextView = view.findViewById(R.id.clickable_text)
    }

    interface OnSrtTextClickListener {
        fun onTextClick(view: View, word: String)
    }
}
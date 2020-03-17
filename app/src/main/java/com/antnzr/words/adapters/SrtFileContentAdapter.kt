package com.antnzr.words.adapters

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antnzr.words.R
import com.antnzr.words.data.Srt

class SrtFileContentAdapter(
    private var srts: ArrayList<Srt>
) :
    RecyclerView.Adapter<SrtFileContentAdapter.SrtFileContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SrtFileContentViewHolder {
        return SrtFileContentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        )
    }

    override fun getItemCount(): Int = srts.size

    override fun onBindViewHolder(holder: SrtFileContentViewHolder, position: Int) {
        holder.srtText.movementMethod = LinkMovementMethod.getInstance()
        holder.srtText.text = srts.elementAt(position).spannableText
    }

    inner class SrtFileContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val srtText: TextView = view.findViewById(R.id.text)
    }
}
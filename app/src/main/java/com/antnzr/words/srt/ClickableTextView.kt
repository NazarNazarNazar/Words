package com.antnzr.words.srt

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView


class ClickableTextView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {

    var word: String? = null

    fun setTextWithClickableWords(text: String) {
        movementMethod = LinkMovementMethod.getInstance()
        setText(addClickablePart(text), BufferType.SPANNABLE)
    }

    private fun addClickablePart(string: String): SpannableStringBuilder? {
        val ssb = SpannableStringBuilder()

        string.split("\\s+".toRegex())
            .map { str ->
                val ss = SpannableString(str)

                ss.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        widget as TextView

                        val s = widget.text as Spanned
                        val start = s.getSpanStart(this)
                        val end = s.getSpanEnd(this)

                        word = s.subSequence(start, end).toString()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                    }
                }, 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                ssb.append(ss).append(" ")
            }
        ssb
            .appendln()
            .appendln()

        return ssb
    }
}
package com.antnzr.words.data

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import com.antnzr.words.srt.SrtTimeFormat
import java.io.File
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


private const val NUMBER_GROUP = 1
private const val TIME_CODE_BEGINNING_GROUP = 2
private const val TIME_CODE_END_GROUP = 4
private const val TEXT_GROUP = 6

private const val NUMBER_PATTERN = "(\\d+\\s)"
private const val TIME_CODE_PATTERN = "([\\d:,]+)"
private const val TIME_CODE_DELIMITER_PATTERN = "( --> )"
private const val BLANK_SPACE_PATTERN = "(\\s)"
private const val TEXT_PATTERN = "([\\s\\S]*)"

private const val SRT_PATTERN =
    "$NUMBER_PATTERN$TIME_CODE_PATTERN$TIME_CODE_DELIMITER_PATTERN$TIME_CODE_PATTERN$BLANK_SPACE_PATTERN$TEXT_PATTERN"

private const val WHITESPACE = "\\s+"

interface SrtFileContentRepository {
    fun getSrtContent(context: Context, subName: String): ArrayList<Srt>
}

private val TAG = SrtFileContentRepositoryImpl::class.simpleName

class SrtFileContentRepositoryImpl :
    SrtFileContentRepository {

    override fun getSrtContent(context: Context, subName: String): ArrayList<Srt> {
        val repository: SubFilesRepository = LocalSubFilesRepository()
        val subFile: File? = repository.findSubtitle(context, subName)

        return read(subFile)
    }

    private fun read(file: File?): ArrayList<Srt> {
        val result = ArrayList<Srt>()

        file?.let {
            val sb: StringBuilder = StringBuilder()

            it.forEachLine { str ->
                if (str.trim().isNotEmpty()) {
                    sb.append("$str\n")
                    return@forEachLine
                }

                parseSrt(sb)?.let { srt -> result.add(srt) }
                sb.clear()
            }
        }

        return result
    }

    private fun parseSrt(sb: StringBuilder): Srt? {
        try {
            val matcher: Matcher = Pattern
                .compile(SRT_PATTERN)
                .matcher(sb)

            while (matcher.find()) {
                val number: String = matcher.group(NUMBER_GROUP)
                val beginning: String = matcher.group(TIME_CODE_BEGINNING_GROUP)
                val end: String = matcher.group(TIME_CODE_END_GROUP)
                val text: String = matcher.group(TEXT_GROUP)
//                    println("num: $num")
//                    println("start: $start")
//                    println("end: $end")
//                    println("content: $content")

                val timeCode: TimeCode = TimeCode(
                    SrtTimeFormat.parse(beginning),
                    SrtTimeFormat.parse(end)
                )

                return Srt(number, timeCode, text, mutateToSpannable(text))
            }
        } catch (exception: Exception) {
            Log.d(TAG, exception.message)
            // IGNORE
        }

        return null
    }

    private fun mutateToSpannable(string: String): SpannableStringBuilder {
        val ssb = SpannableStringBuilder()

        val textArr: List<String> = string.split(WHITESPACE.toRegex())

        textArr.map { str ->
            val ss = SpannableString(str)

            ss.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    widget as TextView

                    val s = widget.text as Spanned
                    val start = s.getSpanStart(this)
                    val end = s.getSpanEnd(this)


                    println("Click on: [ ${s.subSequence(start, end)} ]")
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

   /* companion object {
        fun makeSpannable(content: ArrayList<Srt>): SpannableStringBuilder {
            val ssb = SpannableStringBuilder()

            content.forEach { srt ->
                val textArr: List<String> = srt.text.split(WHITESPACE.toRegex())
                textArr.map { str ->
                    val ss = SpannableString(str)

                    ss.setSpan(object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            widget as TextView

                            println("Click on: [ $ss ]")
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
            }

            return ssb
        }
    }*/
}

class ClickHandler(
    private val handler: () -> Unit,
    private val drawUnderline: Boolean
) : ClickableSpan() {
    override fun onClick(widget: View?) {
        handler()
    }

    override fun updateDrawState(ds: TextPaint?) {
        if (drawUnderline) {
            super.updateDrawState(ds)
        } else {
            ds?.isUnderlineText = false
        }
    }
}

data class Srt(
    val number: String,
    val timeCode: TimeCode,
    val text: String,
    val spannableText: SpannableStringBuilder = SpannableStringBuilder()
)

data class TimeCode(
    val beginning: Date?,
    val end: Date?
)


package com.antnzr.words.srt

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val TIME_FORMAT = "HH:mm:ss,SSS"

class SrtTimeFormat {
    val TIME_DELIMITER = " --> "
    val HOUR_FORMAT = "HH"
    val MINUTE_FORMAT = "mm"
    val SECOND_FORMAT = "ss"
    val MILLISECOND_FORMAT = "SSS"

    enum class Type {
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND
    }

    class SRTTime(val hour: Int, val minute: Int, val second: Int, val millisecond: Int)

    companion object {
        /**
         * Formats the date into SRT time format.
         */
        @JvmStatic
        fun format(date: Date?): String? {
            return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(date)
        }

        /**
         * Parses the SRT time format into date.
         */
        @JvmStatic
        @Throws(ParseException::class)
        fun parse(srtTime: String?): Date? {
            return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).parse(srtTime)
        }
    }
}
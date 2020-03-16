package com.antnzr.words.srt

import java.io.BufferedReader
import java.io.IOException

class SrtReader {

    private class BufferedLineReader(private val reader: BufferedReader) {

        var lineNumber: Long = 0
            private set

        @Throws(IOException::class)
        fun readLine(): String? {
            val line: String = reader.readLine()
            lineNumber++
            // remove the BOM
            return if (lineNumber == 1L) {
                line.replace("\uFEFF", "")
            } else line
        }
    }

}
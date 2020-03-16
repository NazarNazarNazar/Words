package com.antnzr.words.srt

import java.lang.RuntimeException

class SrtException : RuntimeException {

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(message: String?) : super(message)

    constructor(cause: Throwable?) : super(cause)
}
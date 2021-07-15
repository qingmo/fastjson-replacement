package io.github.qingmo.json

import java.util.Calendar
import java.util.Locale

import java.util.TimeZone







abstract class JSONLexerBase: JSONLexer {
    protected var bp = 0
    protected var ch = 0.toChar()
    protected var calendar: Calendar? = null
    protected var timeZone: TimeZone = TimeZone.getDefault()
    protected var locale: Locale = Locale.getDefault()
    protected var token = 0
    abstract operator fun next(): Char
    abstract fun charAt(index: Int): Char
    abstract fun subString(offset: Int, count: Int): String
    open fun getCurrentCalendar(): Calendar? {
        return calendar
    }
}
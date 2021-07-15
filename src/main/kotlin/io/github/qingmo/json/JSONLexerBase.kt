/*
 * Copyright (c) 2020-2021, chaos (eagleqingluo@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
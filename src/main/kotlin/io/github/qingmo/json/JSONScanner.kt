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

import io.github.qingmo.json.JSONLexer.Companion.EOI
import io.github.qingmo.json.JSONLexer.Companion.LITERAL_ISO8601_DATE
import java.util.Calendar
import java.util.SimpleTimeZone
import java.util.TimeZone

/**
 * this class is copied from fastjson to support ISO8601Date
 */
class JSONScanner(private val text: String) : JSONLexerBase() {
    private var len = 0

    init {
        len = text.length
        bp = -1
        next()
        if (ch == 65279.toChar()) { // utf-8 bom
            next()
        }
    }

    fun scanISO8601DateIfMatch(): Boolean {
        return scanISO8601DateIfMatch(true)
    }

    fun scanISO8601DateIfMatch(strict: Boolean): Boolean {
        val rest: Int = len - bp
        return scanISO8601DateIfMatch(strict, rest)
    }

    override fun next(): Char {
        val index = ++bp
        ch = if (index >= len) EOI else text[index]
        return ch
    }

    override fun charAt(index: Int): Char {
        if (index >= len) {
            return EOI
        }

        return text.get(index)
    }

    override fun subString(offset: Int, count: Int): String {
        return text.substring(offset, offset + count)
    }

    private fun scanISO8601DateIfMatch(strict: Boolean, rest: Int): Boolean {
        if (rest < 8) {
            return false
        }
        val c0: Char = charAt(bp)
        val c1: Char = charAt(bp + 1)
        val c2: Char = charAt(bp + 2)
        val c3: Char = charAt(bp + 3)
        val c4: Char = charAt(bp + 4)
        val c5: Char = charAt(bp + 5)
        val c6: Char = charAt(bp + 6)
        val c7: Char = charAt(bp + 7)
        if (!strict && rest > 13) {
            val c_r0: Char = charAt(bp + rest - 1)
            val c_r1: Char = charAt(bp + rest - 2)
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/' && c_r1 == ')') {
                var plusIndex = -1
                for (i in 6 until rest) {
                    val c: Char = charAt(bp + i)
                    if (c == '+') {
                        plusIndex = i
                    } else if (c < '0' || c > '9') {
                        break
                    }
                }
                if (plusIndex == -1) {
                    return false
                }
                val offset = bp + 6
                val numberText: String = this.subString(offset, bp + plusIndex - offset)
                val millis = numberText.toLong()
                calendar = Calendar.getInstance(timeZone, locale)
                calendar!!.timeInMillis = millis
                token = LITERAL_ISO8601_DATE
                return true
            }
        }
        var c10: Char
        if (rest == 8 || rest == 14 || rest == 16 && (charAt(bp + 10).also { c10 = it } == 'T' || c10 == ' ')
            || rest == 17 && charAt(bp + 6) != '-') {
            if (strict) {
                return false
            }
            val y0: Char
            val y1: Char
            val y2: Char
            val y3: Char
            val M0: Char
            val M1: Char
            val d0: Char
            val d1: Char
            val c8: Char = charAt(bp + 8)
            val c_47 = c4 == '-' && c7 == '-'
            val sperate16 = c_47 && rest == 16
            val sperate17 = c_47 && rest == 17
            if (sperate17 || sperate16) {
                y0 = c0
                y1 = c1
                y2 = c2
                y3 = c3
                M0 = c5
                M1 = c6
                d0 = c8
                d1 = charAt(bp + 9)
            } else if (c4 == '-' && c6 == '-') {
                y0 = c0
                y1 = c1
                y2 = c2
                y3 = c3
                M0 = '0'
                M1 = c5
                d0 = '0'
                d1 = c7
            } else {
                y0 = c0
                y1 = c1
                y2 = c2
                y3 = c3
                M0 = c4
                M1 = c5
                d0 = c6
                d1 = c7
            }
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0.code, d1.code)) {
                return false
            }
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1)
            var hour = 0
            var minute = 0
            var seconds = 0
            var millis = 0
            if (rest != 8) {
                val c9: Char = charAt(bp + 9)
                c10 = charAt(bp + 10)
                val c11: Char = charAt(bp + 11)
                val c12: Char = charAt(bp + 12)
                val c13: Char = charAt(bp + 13)
                val h0: Char
                val h1: Char
                val m0: Char
                val m1: Char
                val s0: Char
                val s1: Char
                if (sperate17 && c10 == 'T' && c13 == ':' && charAt(bp + 16) == 'Z'
                    || sperate16 && (c10 == ' ' || c10 == 'T') && c13 == ':'
                ) {
                    h0 = c11
                    h1 = c12
                    m0 = charAt(bp + 14)
                    m1 = charAt(bp + 15)
                    s0 = '0'
                    s1 = '0'
                } else {
                    h0 = c8
                    h1 = c9
                    m0 = c10
                    m1 = c11
                    s0 = c12
                    s1 = c13
                }
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false
                }
                if (rest == 17 && !sperate17) {
                    val S0: Char = charAt(bp + 14)
                    val S1: Char = charAt(bp + 15)
                    val S2: Char = charAt(bp + 16)
                    if (S0 < '0' || S0 > '9') {
                        return false
                    }
                    if (S1 < '0' || S1 > '9') {
                        return false
                    }
                    if (S2 < '0' || S2 > '9') {
                        return false
                    }
                    millis = (S0 - '0') * 100 + (S1 - '0') * 10 + (S2 - '0')
                }
                hour = (h0 - '0') * 10 + (h1 - '0')
                minute = (m0 - '0') * 10 + (m1 - '0')
                seconds = (s0 - '0') * 10 + (s1 - '0')
            }
            calendar!!.set(Calendar.HOUR_OF_DAY, hour)
            calendar!!.set(Calendar.MINUTE, minute)
            calendar!!.set(Calendar.SECOND, seconds)
            calendar!!.set(Calendar.MILLISECOND, millis)
            token = LITERAL_ISO8601_DATE
            return true
        }
        if (rest < 9) {
            return false
        }
        val c8: Char = charAt(bp + 8)
        val c9: Char = charAt(bp + 9)
        var date_len = 10
        val y0: Char
        val y1: Char
        val y2: Char
        val y3: Char
        val M0: Char
        val M1: Char
        val d0: Char
        val d1: Char
        if (c4 == '-' && c7 == '-' // cn
            || c4 == '/' && c7 == '/' // tw yyyy/mm/dd
        ) {
            y0 = c0
            y1 = c1
            y2 = c2
            y3 = c3
            M0 = c5
            M1 = c6
            if (c9 == ' ') {
                d0 = '0'
                d1 = c8
                date_len = 9
            } else {
                d0 = c8
                d1 = c9
            }
        } else if (c4 == '-' && c6 == '-' // cn yyyy-m-dd
        ) {
            y0 = c0
            y1 = c1
            y2 = c2
            y3 = c3
            M0 = '0'
            M1 = c5
            if (c8 == ' ') {
                d0 = '0'
                d1 = c7
                date_len = 8
            } else {
                d0 = c7
                d1 = c8
                date_len = 9
            }
        } else if (c2 == '.' && c5 == '.' // de dd.mm.yyyy
            || c2 == '-' && c5 == '-' // in dd-mm-yyyy
        ) {
            d0 = c0
            d1 = c1
            M0 = c3
            M1 = c4
            y0 = c6
            y1 = c7
            y2 = c8
            y3 = c9
        } else if (c8 == 'T') {
            y0 = c0
            y1 = c1
            y2 = c2
            y3 = c3
            M0 = c4
            M1 = c5
            d0 = c6
            d1 = c7
            date_len = 8
        } else {
            if (c4 == '年' || c4 == '년') {
                y0 = c0
                y1 = c1
                y2 = c2
                y3 = c3
                if (c7 == '月' || c7 == '월') {
                    M0 = c5
                    M1 = c6
                    if (c9 == '日' || c9 == '일') {
                        d0 = '0'
                        d1 = c8
                    } else if (charAt(bp + 10) == '日' || charAt(bp + 10) == '일') {
                        d0 = c8
                        d1 = c9
                        date_len = 11
                    } else {
                        return false
                    }
                } else if (c6 == '月' || c6 == '월') {
                    M0 = '0'
                    M1 = c5
                    if (c8 == '日' || c8 == '일') {
                        d0 = '0'
                        d1 = c7
                    } else if (c9 == '日' || c9 == '일') {
                        d0 = c7
                        d1 = c8
                    } else {
                        return false
                    }
                } else {
                    return false
                }
            } else {
                return false
            }
        }
        if (!checkDate(y0, y1, y2, y3, M0, M1, d0.code, d1.code)) {
            return false
        }
        setCalendar(y0, y1, y2, y3, M0, M1, d0, d1)
        val t: Char = charAt(bp + date_len)
        if (t == 'T' && rest == 16 && date_len == 8 && charAt(bp + 15) == 'Z') {
            val h0: Char = charAt(bp + date_len + 1)
            val h1: Char = charAt(bp + date_len + 2)
            val m0: Char = charAt(bp + date_len + 3)
            val m1: Char = charAt(bp + date_len + 4)
            val s0: Char = charAt(bp + date_len + 5)
            val s1: Char = charAt(bp + date_len + 6)
            if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                return false
            }
            setTime(h0, h1, m0, m1, s0, s1)
            calendar!!.set(Calendar.MILLISECOND, 0)
            if (calendar!!.timeZone.rawOffset != 0) {
                val timeZoneIDs = TimeZone.getAvailableIDs(0)
                if (timeZoneIDs.isNotEmpty()) {
                    val timeZone = TimeZone.getTimeZone(timeZoneIDs[0])
                    calendar!!.timeZone = timeZone
                }
            }
            token = LITERAL_ISO8601_DATE
            return true
        } else if (t == 'T' || t == ' ' && !strict) {
            if (rest < date_len + 9) { // "0000-00-00T00:00:00".length()
                return false
            }
        } else if (t == '"' || t == EOI || t == '日' || t == '일') {
            calendar!!.set(Calendar.HOUR_OF_DAY, 0)
            calendar!!.set(Calendar.MINUTE, 0)
            calendar!!.set(Calendar.SECOND, 0)
            calendar!!.set(Calendar.MILLISECOND, 0)
            ch = charAt(date_len.let { bp += it; bp })
            token = LITERAL_ISO8601_DATE
            return true
        } else if (t == '+' || t == '-') {
            if (len == date_len + 6) {
                if (charAt(bp + date_len + 3) != ':' //
                    || charAt(bp + date_len + 4) != '0' //
                    || charAt(bp + date_len + 5) != '0'
                ) {
                    return false
                }
                setTime('0', '0', '0', '0', '0', '0')
                calendar!!.set(Calendar.MILLISECOND, 0)
                setTimeZone(t, charAt(bp + date_len + 1), charAt(bp + date_len + 2))
                return true
            }
            return false
        } else {
            return false
        }
        if (charAt(bp + date_len + 3) != ':') {
            return false
        }
        if (charAt(bp + date_len + 6) != ':') {
            return false
        }
        val h0: Char = charAt(bp + date_len + 1)
        val h1: Char = charAt(bp + date_len + 2)
        val m0: Char = charAt(bp + date_len + 4)
        val m1: Char = charAt(bp + date_len + 5)
        val s0: Char = charAt(bp + date_len + 7)
        val s1: Char = charAt(bp + date_len + 8)
        if (!checkTime(h0, h1, m0, m1, s0, s1)) {
            return false
        }
        setTime(h0, h1, m0, m1, s0, s1)
        val dot: Char = charAt(bp + date_len + 9)
        var millisLen = -1 // 有可能没有毫秒区域，没有毫秒区域的时候下一个字符位置有可能是'Z'、'+'、'-'
        var millis = 0
        if (dot == '.') { // 0000-00-00T00:00:00.000
            if (rest < date_len + 11) {
                return false
            }
            val S0: Char = charAt(bp + date_len + 10)
            if (S0 < '0' || S0 > '9') {
                return false
            }
            millis = S0 - '0'
            millisLen = 1
            if (rest > date_len + 11) {
                val S1: Char = charAt(bp + date_len + 11)
                if (S1 in '0'..'9') {
                    millis = millis * 10 + (S1 - '0')
                    millisLen = 2
                }
            }
            if (millisLen == 2) {
                val currentS2: Char = charAt(bp + date_len + 12)
                if (currentS2 in '0'..'9') {
                    millis = millis * 10 + (currentS2 - '0')
                    millisLen = 3
                }
            }
        }
        calendar!!.set(Calendar.MILLISECOND, millis)
        var timzeZoneLength = 0
        var timeZoneFlag: Char = charAt(bp + date_len + 10 + millisLen)
        if (timeZoneFlag == ' ') {
            millisLen++
            timeZoneFlag = charAt(bp + date_len + 10 + millisLen)
        }
        if (timeZoneFlag == '+' || timeZoneFlag == '-') {
            val t0: Char = charAt(bp + date_len + 10 + millisLen + 1)
            if (t0 < '0' || t0 > '1') {
                return false
            }
            val t1: Char = charAt(bp + date_len + 10 + millisLen + 2)
            if (t1 < '0' || t1 > '9') {
                return false
            }
            val t2: Char = charAt(bp + date_len + 10 + millisLen + 3)
            var t3 = '0'
            var t4 = '0'
            if (t2 == ':') { // ThreeLetterISO8601TimeZone
                t3 = charAt(bp + date_len + 10 + millisLen + 4)
                t4 = charAt(bp + date_len + 10 + millisLen + 5)
                if (t3 == '4' && t4 == '5') {
                    // handle some special timezones like xx:45
                    if (t0 == '1' && (t1 == '2' || t1 == '3')) {
                        // NZ-CHAT          => +12:45
                        // Pacific/Chatham  => +12:45
                        // NZ-CHAT          => +13:45 (DST)
                        // Pacific/Chatham  => +13:45 (DST)
                    } else if (t0 == '0' && (t1 == '5' || t1 == '8')) {
                        // Asia/Kathmandu   => +05:45
                        // Asia/Katmandu    => +05:45
                        // Australia/Eucla  => +08:45
                    } else {
                        return false
                    }
                } else {
                    //handle normal timezone like xx:00 and xx:30
                    if (t3 != '0' && t3 != '3') {
                        return false
                    }
                    if (t4 != '0') {
                        return false
                    }
                }
                timzeZoneLength = 6
            } else if (t2 == '0') { // TwoLetterISO8601TimeZone
                t3 = charAt(bp + date_len + 10 + millisLen + 4)
                if (t3 != '0' && t3 != '3') {
                    return false
                }
                timzeZoneLength = 5
            } else if (t2 == '3' && charAt(bp + date_len + 10 + millisLen + 4) == '0') {
                t3 = '3'
                t4 = '0'
                timzeZoneLength = 5
            } else if (t2 == '4' && charAt(bp + date_len + 10 + millisLen + 4) == '5') {
                t3 = '4'
                t4 = '5'
                timzeZoneLength = 5
            } else {
                timzeZoneLength = 3
            }
            setTimeZone(timeZoneFlag, t0, t1, t3, t4)
        } else if (timeZoneFlag == 'Z') { // UTC
            timzeZoneLength = 1
            if (calendar!!.timeZone.rawOffset != 0) {
                val timeZoneIDs = TimeZone.getAvailableIDs(0)
                if (timeZoneIDs.isNotEmpty()) {
                    val timeZone = TimeZone.getTimeZone(timeZoneIDs[0])
                    calendar!!.timeZone = timeZone
                }
            }
        }
        val end: Char = charAt(bp + (date_len + 10 + millisLen + timzeZoneLength))
        if (end != EOI && end != '"') {
            return false
        }
        ch = charAt((date_len + 10 + millisLen + timzeZoneLength).let { bp += it; bp })
        token = LITERAL_ISO8601_DATE
        return true
    }

    private fun checkDate(y0: Char, y1: Char, y2: Char, y3: Char, M0: Char, M1: Char, d0: Int, d1: Int): Boolean {
        if (y0 < '0' || y0 > '9') {
            return false
        }
        if (y1 < '0' || y1 > '9') {
            return false
        }
        if (y2 < '0' || y2 > '9') {
            return false
        }
        if (y3 < '0' || y3 > '9') {
            return false
        }
        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false
            }
        } else if (M0 == '1') {
            if (M1 != '0' && M1 != '1' && M1 != '2') {
                return false
            }
        } else {
            return false
        }
        if (d0 == '0'.code) {
            if (d1 < '1'.code || d1 > '9'.code) {
                return false
            }
        } else if (d0 == '1'.code || d0 == '2'.code) {
            if (d1 < '0'.code || d1 > '9'.code) {
                return false
            }
        } else if (d0 == '3'.code) {
            if (d1 != '0'.code && d1 != '1'.code) {
                return false
            }
        } else {
            return false
        }
        return true
    }

    private fun setCalendar(y0: Char, y1: Char, y2: Char, y3: Char, M0: Char, M1: Char, d0: Char, d1: Char) {
        calendar = Calendar.getInstance(timeZone, locale)
        val year = (y0 - '0') * 1000 + (y1 - '0') * 100 + (y2 - '0') * 10 + (y3 - '0')
        val month = (M0 - '0') * 10 + (M1 - '0') - 1
        val day = (d0 - '0') * 10 + (d1 - '0')
        calendar!!.set(Calendar.YEAR, year)
        calendar!!.set(Calendar.MONTH, month)
        calendar!!.set(Calendar.DAY_OF_MONTH, day)
    }

    private fun checkTime(h0: Char, h1: Char, m0: Char, m1: Char, s0: Char, s1: Char): Boolean {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false
            }
        } else if (h0 == '2') {
            if (h1 < '0' || h1 > '4') {
                return false
            }
        } else {
            return false
        }
        if (m0 in '0'..'5') {
            if (m1 < '0' || m1 > '9') {
                return false
            }
        } else if (m0 == '6') {
            if (m1 != '0') {
                return false
            }
        } else {
            return false
        }
        if (s0 in '0'..'5') {
            if (s1 < '0' || s1 > '9') {
                return false
            }
        } else if (s0 == '6') {
            if (s1 != '0') {
                return false
            }
        } else {
            return false
        }
        return true
    }

    protected fun setTime(h0: Char, h1: Char, m0: Char, m1: Char, s0: Char, s1: Char) {
        val hour = (h0 - '0') * 10 + (h1 - '0')
        val minute = (m0 - '0') * 10 + (m1 - '0')
        val seconds = (s0 - '0') * 10 + (s1 - '0')
        calendar!![Calendar.HOUR_OF_DAY] = hour
        calendar!![Calendar.MINUTE] = minute
        calendar!![Calendar.SECOND] = seconds
    }

    private fun setTimeZone(timeZoneFlag: Char, t0: Char, t1: Char) {
        setTimeZone(timeZoneFlag, t0, t1, '0', '0')
    }

    private fun setTimeZone(timeZoneFlag: Char, t0: Char, t1: Char, t3: Char, t4: Char) {
        var timeZoneOffset = ((t0 - '0') * 10 + (t1 - '0')) * 3600 * 1000
        timeZoneOffset += ((t3 - '0') * 10 + (t4 - '0')) * 60 * 1000
        if (timeZoneFlag == '-') {
            timeZoneOffset = -timeZoneOffset
        }
        if (calendar!!.timeZone.rawOffset != timeZoneOffset) {
            calendar!!.timeZone = SimpleTimeZone(timeZoneOffset, timeZoneOffset.toString())
        }
    }
}
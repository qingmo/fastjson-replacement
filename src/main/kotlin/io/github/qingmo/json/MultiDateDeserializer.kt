/*
 * MIT License
 *
 * Copyright (c) 2021 qingmo(eagleqingluo@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.qingmo.json

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.joda.time.format.ISODateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class MultiDateDeserializer(vc: Class<*>?) : StdDeserializer<Date>(vc) {


    companion object {
        private const val ISO8601DateTimeBasicFormat = "yyyyMMdd'T'HHmmss"

        // the sequence must change carefully, the best match must be the first one
        private val DATE_FORMATS: List<String> = listOf(
            JSON.STANDARD_PATTERN,
            JSON.DATE_PATTERN,
            ISO8601DateTimeBasicFormat
        )
    }

    constructor() : this(null)

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Date {
        val node: JsonNode? = p?.codec?.readTree(p)
        val dateStr = node?.textValue()
        if (dateStr.isNullOrBlank()) {
            throw JsonParseException(
                p,
                "Unparseable date with empty value."
            )
        }
        try {
            val parseDateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(dateStr)
            return parseDateTime.toDate()
        } catch (ignore: Exception) {

        }
        for (DATE_FORMAT in DATE_FORMATS) {
            try {
                return SimpleDateFormat(DATE_FORMAT).parse(dateStr)
            } catch (ignore: ParseException) {
                // ignore
            }
        }
        throw JsonParseException(
            p,
            "Unparseable date: \"" + dateStr + "\". Supported formats: " + DATE_FORMATS
        )

    }

}
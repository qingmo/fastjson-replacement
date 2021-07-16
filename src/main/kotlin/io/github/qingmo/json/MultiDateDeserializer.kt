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
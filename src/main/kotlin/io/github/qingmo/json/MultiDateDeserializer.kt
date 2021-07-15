package io.github.qingmo.json

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class MultiDateDeserializer(vc: Class<*>?) : StdDeserializer<Date>(vc) {

    companion object {
        // the sequence must change carefully, the best match must be the first one
        private val DATE_FORMATS: List<String> = listOf(
            JSON.STANDARD_PATTERN,
            JSON.DATE_PATTERN
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
        val longVal: Long
        val dateLexer = JSONScanner(dateStr)
        if (dateLexer.scanISO8601DateIfMatch()) {
            longVal = dateLexer.getCurrentCalendar()!!.timeInMillis
            return Date(longVal)
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
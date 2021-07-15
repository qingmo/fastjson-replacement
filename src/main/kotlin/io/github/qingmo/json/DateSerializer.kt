package io.github.qingmo.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.text.SimpleDateFormat
import java.util.Date

class DateSerializer(t: Class<Date>?) : StdSerializer<Date>(t) {
    companion object {
        private val CUSTOM_FORMAT = SimpleDateFormat(JSON.STANDARD_PATTERN)
    }
    constructor() : this(null)

    override fun serialize(value: Date?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value == null) {
            return
        }
        val formattedDataValue = CUSTOM_FORMAT.format(value)
        gen!!.writeString(formattedDataValue)
    }
}
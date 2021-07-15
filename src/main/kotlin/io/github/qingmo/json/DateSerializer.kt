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
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
import com.fasterxml.jackson.core.io.IOContext
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser
import org.junit.jupiter.api.Test
import java.util.Calendar
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

internal class MultiDateDeserializerTest {


    @Test
    fun `test illegelArgument with deserialize`() {
        val deserializer = MultiDateDeserializer()
        assertFailsWith(
            exceptionClass = JsonParseException::class,
            message = "Unparseable date with empty value",
            block = {
                deserializer.deserialize(null, null)
            }
        )
        val jsonParser = UTF8DataInputJsonParser(
            IOContext(null, null, false),
            0,
            null,
            null,
            null,
            0
        )
        jsonParser.codec = null
        assertFailsWith(
            exceptionClass = JsonParseException::class,
            message = "Unparseable date with empty value",
            block = {
                deserializer.deserialize(jsonParser, null)
            }
        )
    }

    @Test
    fun `test with ISO8601 formats`() {
        var data = """
            {"haha":"2016-11-01"}
        """.trimIndent()
        var ret = JSON.parseObject(data, TestDate::class.java)
        assertNotNull(ret)
        val calendar = Calendar.getInstance()
        calendar.time = ret.haha
        assertEquals(2016, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH))
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY))

        data = """
            {"haha":"2000-01-01T01:01:01"}
        """.trimIndent()
        ret = JSON.parseObject(data, TestDate::class.java)
        assertNotNull(ret)
        calendar.time = ret.haha
        assertEquals(2000, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH))
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY))

        data = """
            {"haha":"2004-05-03T17:30:08+08:00"}
        """.trimIndent()
        ret = JSON.parseObject(data, TestDate::class.java)
        assertNotNull(ret)
        calendar.time = ret.haha
        assertEquals(2004, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.MAY, calendar.get(Calendar.MONTH))
        assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH))

        data = """
            {"haha":"20040605T183008"}
        """.trimIndent()
        ret = JSON.parseObject(data, TestDate::class.java)
        assertNotNull(ret)
        calendar.time = ret.haha
        assertEquals(2004, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH))
        assertEquals(5, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(18, calendar.get(Calendar.HOUR_OF_DAY))
    }

    data class TestDate(val haha: Date)
}
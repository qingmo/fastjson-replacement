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
import com.fasterxml.jackson.core.io.IOContext
import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import io.github.qingmo.json.datas.TestDate
import io.github.qingmo.json.exception.JSONException
import io.github.qingmo.json.internal.MultiDateDeserializer
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

internal class MultiDateDeserializerTest {


    @Test
    fun `test illegelArgument with deserialize`() {
        val deserializer = MultiDateDeserializer()
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Unparseable date with empty JsonParser.",
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
            exceptionClass = JSONException::class,
            message = "Unparseable date with empty ObjectCodec.",
            block = {
                deserializer.deserialize(jsonParser, null)
            }
        )
        val objectMapper = ObjectMapper()
        val createParser = objectMapper.createParser("")
        assertFailsWith(
            exceptionClass = JsonParseException::class,
            message = "Unparseable date with empty value",
            block = {
                deserializer.deserialize(createParser, null)
            }
        )
        val createParser2 = objectMapper.createParser(NullNode.getInstance().toString())
        assertFailsWith(
            exceptionClass = JsonParseException::class,
            message = "Unparseable date with empty value",
            block = {
                deserializer.deserialize(createParser2, null)
            }
        )
        val createParser3 = objectMapper.createParser(TextNode.valueOf("").toString())
        assertFailsWith(
            exceptionClass = JsonParseException::class,
            message = "Unparseable date with empty value",
            block = {
                deserializer.deserialize(createParser3, null)
            }
        )

        val createParser4 = objectMapper.createParser(TextNode.valueOf("   ").toString())
        assertFailsWith(
            exceptionClass = JsonParseException::class,
            message = "Unparseable date with empty value",
            block = {
                deserializer.deserialize(createParser4, null)
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
}
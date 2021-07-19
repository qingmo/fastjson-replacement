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

import io.github.qingmo.json.exception.JSONException
import org.intellij.lang.annotations.Language
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class MutilDateFormatTest {

    @Test
    fun `test java Date with multi format`() {
        // yyyy-MM-dd HH:mm:ss
        @Language("JSON") val data = """{
          "haha": "共产党100周年快乐",
          "time": "2021-07-01 00:00:00"
        }"""
        var result = JSON.parseObject(data, DataWithDate::class.java)
        assertNotNull(result)
        assertEquals("共产党100周年快乐", result.haha)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        assertEquals(simpleDateFormat.parse("2021-07-01 00:00:00"), result.time)

        // yyyy-MM-dd
        @Language("JSON") val data2 = """{
          "haha": "共产党100周年快乐",
          "time": "2021-07-01"
        }"""
        result = JSON.parseObject(data2, DataWithDate::class.java)
        assertNotNull(result)
        assertEquals("共产党100周年快乐", result.haha)
        assertEquals(simpleDateFormat.parse("2021-07-01 00:00:00"), result.time)
    }

    @Test
    fun `test java Date serializer with null value`() {
        val data = DataWithDate()
        data.haha = "共产党100周年快乐"
        data.time = null
        val result = JSON.toJSONString(data)
        assertNotNull(result)
        //language=JSON
        assertEquals("{\"haha\":\"共产党100周年快乐\"}", result)
    }

    @Test
    fun `test java Date deserializer with blank date value`() {
        @Language("JSON") val data = """{
          "haha": "共产党100周年快乐",
          "time": ""
        }"""
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Unparseable date with empty value.",
            block = {
                JSON.parseObject(data, DataWithDate::class.java)
            }
        )
    }

    @Test
    fun `test java Date deserializer best match sequence`() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val expectDate = simpleDateFormat.parse("2021-07-01 12:01:00")
        @Language("JSON") val data = """{
          "haha": "共产党100周年快乐",
          "time": "2021-07-01 12:01:00"
        }"""
        val parseRet = JSON.parseObject(data, DataWithDate::class.java)
        assertNotNull(parseRet)
        assertTrue(expectDate.equals(parseRet.time))
    }

    @Test
    fun `test java Date deserializer with unsupport format`() {
        @Language("JSON") val data = """{
          "haha": "共产党100周年快乐",
          "time": "20210701 12:01:00"
        }"""
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Unparseable date",
            block = {
                 JSON.parseObject(data, DataWithDate::class.java)
            }
        )

    }

    private class DataWithDate {
        var haha: String? = null
        var time: Date? = null
    }
}
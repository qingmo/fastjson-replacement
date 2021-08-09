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
import java.time.LocalDateTime
import java.util.*
import kotlin.test.*


class ConvertTest {

    internal class TestDomain {
        var id: Int? = null
        var name: String? = null
        var da: Double? = null
        var ldt: Map<String, Any>? = null
        var ld: Map<String, Any>? = null
        var lt: Map<String, Any>? = null
        var jsn: JSONObject? = null
        var jsa: JSONArray? = null
    }

    @Test
    fun testIssue15() {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = "zpj"
        map["id"] = 111
        map["jsa"] =
            "[1, 34, 32, \"zpj\", {\"age\": 18, \"name\": \"zpj\", \"child\": {\"name\": \"zzy\", \"gender\": \"nan\"}}, {\"url\": \"test\", \"name\": \"suhu\"}]"
        val json: String = JSON.toJSONString(map)
        try {
            JSON.parseObject(json, TestDomain::class.java)
            fail("Should not pass")
        } catch (e: JSONException) {
            TestUtils.verifyException(e, "Cannot deserialize value of type")
            TestUtils.verifyException(e, "io.github.qingmo.json.JSONArray")
            TestUtils.verifyException(e, "VALUE_STRING")
        }
    }

    @Test
    fun `test java LocalDateTime`() {
        val data = DataWithLocalDateTime()
        data.haha = "共产党100周年快乐"
        data.time = LocalDateTime.of(2021, 7, 1, 0, 0, 0)

        val result = JSON.toJSONString(data)
        assertNotNull(result)
        assertTrue(result.contains("共产党100周年快乐"))
        assertTrue(result.contains("2021-07-01 00:00:00"))
        val retObj = JSON.parseObject(result, DataWithLocalDateTime::class.java)
        assertEquals(data.time, retObj.time)
    }

    @Test
    fun `test java Date`() {
        @Language("JSON") val data = """{
          "haha": "共产党100周年快乐",
          "time": "2021-07-01 00:00:00"
        }"""
        val result = JSON.parseObject(data, DataWithDate::class.java)
        assertNotNull(result)
        assertEquals("共产党100周年快乐", result.haha)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        assertEquals(simpleDateFormat.parse("2021-07-01 00:00:00"), result.time)
    }

    private class DataWithLocalDateTime {
        var haha: String? = null
        var time: LocalDateTime? = null
    }

    private class DataWithDate {
        var haha: String? = null
        var time: Date? = null
    }
}
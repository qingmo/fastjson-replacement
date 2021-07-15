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

import io.github.qingmo.json.exception.JSONException
import org.intellij.lang.annotations.Language
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

import java.util.HashMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail


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
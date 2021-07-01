package io.github.qingmo.json

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.github.qingmo.json.exception.JSONException
import java.lang.Exception
import java.time.LocalDateTime

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
        val data = DataWithTime()
        data.haha = "共产党100周年快乐"
        data.time = LocalDateTime.of(2021, 7, 1, 0, 0, 0)

        val result = JSON.toJSONString(data)
        assertNotNull(result)
        assertTrue(result.contains("共产党100周年快乐"))
        assertTrue(result.contains("2021-07-01 00:00:00"))
        val retObj = JSON.parseObject(result, DataWithTime::class.java)
        assertEquals(data.time, retObj.time)
    }

    private class DataWithTime {
        var haha: String? = null
        var time: LocalDateTime? = null
    }
}
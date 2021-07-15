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
    fun `test java Date deserializer best match sequence`() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val expectDate = simpleDateFormat.parse("2021-07-01 12:01:00")
        @Language("JSON") val data = """{
          "haha": "共产党100周年快乐",
          "time": "2021-07-01 12:01:00"
        }"""
        val parseRet = JSON.parseObject(data, DataWithDate::class.java)
        assertNotNull(parseRet)
        println(expectDate.time)
        println(parseRet.time!!.time)
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
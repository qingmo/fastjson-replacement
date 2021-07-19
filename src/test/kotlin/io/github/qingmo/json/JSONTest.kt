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

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.NullNode
import io.github.qingmo.json.exception.JSONException
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class JSONTest {

    @Test
    fun `test toJSONString with invalid argument`() {

        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "null",
            block = {
                JSON.toJSONString(mapOf(null to "123"))
            }
        )
        assertEquals("null", JSON.toJSONString(null))
    }

    @Test
    fun `test parse with different argument`() {

        assertTrue(JSON.parse("""{}""") is JSONObject)
        assertTrue(JSON.parse("""[]""") is JSONArray)
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Unrecognized token",
            block = {
                JSON.parse("""haha""")
            }
        )

        assertTrue(JSON.parse("""null""") is NullNode)
        assertEquals(IntNode(1), JSON.parse("""1"""))
    }

    @Test
    fun `test parseObject with invalid argument`() {
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Unrecognized token",
            block = {
                JSON.parseObject("""haha""")
            }
        )
    }

    @Test
    fun `test parseArray with invalid argument`() {
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Unrecognized token",
            block = {
                JSON.parseArray("""haha""")
            }
        )
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Cannot deserialize value of type",
            block = {
                JSON.parseArray("""["haha","heihei"]""", Int::class.java)
            }
        )
    }

    @Test
    fun `test isJson`() {
        assertTrue(JSON.isJson("""{}"""))
        assertTrue(JSON.isJson("""[]"""))
        assertFalse(JSON.isJson("""haha"""))
        assertFalse(JSON.isJson("""{haha"""))
        assertFalse(JSON.isJson("""[haha"""))
        assertFalse(JSON.isJson(null))
    }

    @Test
    fun `test isJsonObj`() {
        assertTrue(JSON.isJsonObj("""{}"""))
        assertFalse(JSON.isJsonObj("""     """))
        assertFalse(JSON.isJsonObj("""   1  """))
    }

    @Test
    fun `test isJsonArray`() {
        assertTrue(JSON.isJsonArray("""[]"""))
        assertFalse(JSON.isJsonArray("""     """))
        assertFalse(JSON.isJsonArray("""   1  """))
        assertFalse(JSON.isJsonArray("""  \t 1 \t """))
    }

    @Test
    fun `test ReadObject`() {
        val ob: JSONObject = JSON.parseObject("{\"a\":{\"b\":3}, \"c\":[9, -4], \"d\":null, \"e\":true}")
        assertEquals(4, ob.size)
        val ob2 = ob.getJSONObject("a")
        assertEquals(1, ob2!!.size)
        assertEquals(3, ob2.getIntValue("b"))
        val array = ob.getJSONArray("c")
        assertEquals(2, array.size)
        assertEquals(9, array.getIntValue(0))
        assertEquals(-4, array.getIntValue(1))
        assertNull(ob["d"])
        assertTrue(ob.getBoolean("e") ?: false)
    }

    @Test
    fun `test ReadArray`() {
        val jacksonParsedArray: JSONArray = JSON.parseArray(
            "[null, 13, false, 1.25, \"abc\", {\"a\":13}, [ ] ]"
        )
        assertEquals(7, jacksonParsedArray.size)
        assertNull(jacksonParsedArray[0])
        assertEquals(13, jacksonParsedArray.getIntValue(1))
        assertFalse(jacksonParsedArray.getBoolean(2) ?: true)
        assertEquals(java.lang.Double.valueOf(1.25), jacksonParsedArray.getDouble(3))
        assertEquals("abc", jacksonParsedArray.getString(4))
        val ob = jacksonParsedArray.getJSONObject(5)
        assertEquals(1, ob!!.size)
        assertEquals(13, ob.getIntValue("a"))
        val array2 = jacksonParsedArray.getJSONArray(6)
        assertEquals(0, array2!!.size)
    }

    @Test
    fun `test ReadList`() {
        val jsonString = "[1, 2, 3]"
        val result = JSON.parseArray(jsonString, String::class.java)
        assertNotNull(result)
        assertEquals(3, result.size)
        assertEquals("1", result.get(0))
        val result2 = JSON.parseArray(jsonString, Int::class.java)
        assertEquals(3, result2.size)
        assertEquals(1, result2.get(0))
    }

    @Test
    fun `test WriteObject`() {
        val jsonString = "{\"a\":{\"b\":3}}"
        val jsonObject = JSON.parseObject(jsonString)
        assertEquals(jsonString, JSON.toJSONString(jsonObject))
    }

    @Test
    fun `test WriteArray`() {
        val jsonString = "[1,true,\"text\",[null,3],{\"a\":[1.25]}]"
        val jsonArray = JSON.parseArray(jsonString)
        assertEquals(jsonString, JSON.toJSONString(jsonArray))
    }

    @Test
    fun `test withISO8601DateFormat`() {
        val gmtCale = Calendar.getInstance()
        gmtCale.clear()
        gmtCale.timeZone = TimeZone.getTimeZone("GMT")
        gmtCale[2018, Calendar.MAY, 31, 19, 13] = 42
        val gmtDate: Date = gmtCale.time
        val gmtText = "[\"2018-05-31T19:13:42Z\",\"2018-05-31T19:13:42.000Z\"]"
        val gmtList: List<Date> = JSON.parseArray(gmtText, Date::class.java)
        assertEquals(gmtList[0], gmtDate)
        assertEquals(gmtList[1], gmtDate)
        val gmt7Cale = Calendar.getInstance()
        gmt7Cale.clear()
        gmt7Cale.timeZone = TimeZone.getTimeZone("GMT+7")
        gmt7Cale[2018, Calendar.MAY, 31, 19, 13] = 42
        val gmt7Date: Date = gmt7Cale.time
        val gmt7Text = "[\"2018-05-31T19:13:42+07:00\",\"2018-05-31T19:13:42.000+07:00\"]"
        val gmt7List: List<Date> = JSON.parseArray(gmt7Text, Date::class.java)
        assertEquals(gmt7Date, gmt7List[0])
        assertEquals(gmt7Date, gmt7List[1])
    }

    @Test
    fun `test ErrorOnEnumNotMatchEnums`() {
        val text = "[\"M\", \"MATCH\"]"
        val list: List<ErrorOnEnumNotMatchEnums> =
            JSON.parseArray(text, ErrorOnEnumNotMatchEnums::class.java)
        assertNull(list[0])
        assertEquals(ErrorOnEnumNotMatchEnums.MATCH, list[1])
    }

    enum class ErrorOnEnumNotMatchEnums {
        MATCH
    }

    @Test
    fun `test withAnnotation`() {
        val person = Person()
        person.name = "abc"
        person.age = 123
        val unwrappedIsFalseBean = UnwrappedIsFalseBean()
        unwrappedIsFalseBean.person = person
        assertEquals("{\"person\":{\"name\":\"abc\",\"age\":123}}", JSON.toJSONString(unwrappedIsFalseBean))
        val unwrappedIsTrueBean = UnwrappedIsTrueBean()
        unwrappedIsTrueBean.person = person
        assertEquals("{\"name\":\"abc\",\"age\":123}", JSON.toJSONString(unwrappedIsTrueBean))
    }

    @Test
    fun `test LocalTime`() {
       val data = """{"haha":"12:11:01"}"""
        val ret = JSON.parseObject(data, LocalTimeTest::class.java)
        assertNotNull(ret)
        assertEquals(12, ret.haha.hour)
        assertEquals(11, ret.haha.minute)
        assertEquals(1, ret.haha.second)
        val test = LocalTimeTest(LocalTime.of(ret.haha.hour, ret.haha.minute, 2, 0))
        val teststr = JSON.toJSONString(test)
        assertEquals("""{"haha":"12:11:02"}""", teststr)

    }

    data class LocalTimeTest(val haha: LocalTime)
    @JsonDeserialize
    class UnwrappedIsFalseBean {

        var person: Person? = null
    }

    class UnwrappedIsTrueBean {
        @JsonUnwrapped
        var person: Person? = null
    }

    class Person {
        @JsonProperty(index = 0)
        var name: String? = null

        @JsonProperty(index = 1)
        var age: Int? = null
    }
}
package com.github.qingmo.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import java.util.Calendar

import java.util.TimeZone

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.Date
import com.fasterxml.jackson.databind.DeserializationFeature

import com.fasterxml.jackson.core.JsonProcessingException

import java.io.IOException





class JSONTest {

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
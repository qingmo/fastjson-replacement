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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

internal class JSONObjectTest {
    @Test
    fun `common test`() {
        val jsonObject = JSONObject(5)
        assertNotNull(jsonObject)
        assertNull(jsonObject.getJSONObject("haha"))
        jsonObject.put("haha", JSONObject())
        jsonObject.put("foo", JSONArray(mutableListOf("a")))
        assertNotNull(jsonObject["haha"])
        assertNotNull(jsonObject.getJSONObject("haha"))
        assertTrue(jsonObject.getJSONObject("haha") is JSONObject)
        assertNotNull(jsonObject.getJSONArray("foo"))
        assertTrue(jsonObject.getJSONArray("foo").isNotEmpty())
        assertEquals(jsonObject.keys, setOf("haha", "foo"))
        jsonObject.clear()
        assertTrue(jsonObject.isEmpty())

    }

    @Test
    fun `common test type`() {
        val jsonObject = JSONObject()
        jsonObject.putAll(mapOf("haha" to "heihei", "foo" to "bar"))
        assertEquals(2, jsonObject.size)
        assertTrue(jsonObject.containsValue("heihei"))
        assertTrue(jsonObject.containsValue("bar"))
        assertTrue(jsonObject.containsKey("foo"))
        assertFalse(jsonObject.containsKey("giegie"))
        assertFalse(jsonObject.containsKey(1))
        assertNull(jsonObject.remove(1))
        jsonObject.remove("haha")
        assertEquals(1, jsonObject.size)
        assertEquals("""{"foo":"bar"}""", jsonObject.toString())
        assertEquals(listOf("bar"), jsonObject.values.toList())
        assertEquals(mapOf("foo" to "bar").hashCode(), jsonObject.hashCode())
    }

    @Test
    fun `test toJavaObject with TestClass`() {
        val jsonObject = JSONObject()
        jsonObject.putAll(mapOf("foo" to "bar"))
        val ret = jsonObject.toJavaObject(TestClass::class.java)
        assertEquals("bar", ret.foo)
    }

    @Test
    fun `test toJavaObject with MutableMap`() {
        val jsonObject = JSONObject()
        jsonObject.put("test", "value")
        val ret = jsonObject.toJavaObject(MutableMap::class.java)
        assertEquals(1, ret.size)
        assertEquals(listOf("value"), ret.values.toList())
    }

    @Test
    fun `test toJavaObject with JSONObject`() {
        val jsonObject = JSONObject()
        jsonObject.put("test", "value")
        val ret = jsonObject.toJavaObject(JSONObject::class.java)
        assertEquals(1, ret.size)
        assertEquals(listOf("value"), ret.values.toList())
    }

    @Test
    fun `test toJavaObject with JSONArray failed`() {
        val jsonObject = JSONObject()
        jsonObject.put("test", "value")
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "Cannot deserialize value of type",
            block = {
                jsonObject.toJavaObject(JSONArray::class.java)
            }
        )
    }

    @Test
    fun `test getObject Type`() {
        val jsonObject = JSONObject()
        jsonObject.put("test", TestClass("haha"))
        assertNull(jsonObject.getObject("hoho", TestClass::class.java))
        assertNotNull(jsonObject.getObject("test", TestClass::class.java))
        assertEquals("haha", jsonObject.getObject("test", TestClass::class.java)!!.foo)
    }

    @Test
    fun `test get Type and it's value`() {
        val jsonObject = JSONObject()
        jsonObject["a"] = true
        jsonObject["b"] = "¯¯ðƒ©ƒˀ¸ˇ"
        jsonObject["c"] = "3"
        jsonObject["d"] = "5.1"
        jsonObject["e"] = 5.1

        assertEquals(true, jsonObject.getBooleanValue("a"))
        assertEquals(false, jsonObject.getBooleanValue("b"))
        assertEquals(false, jsonObject.getBooleanValue("c"))
        assertEquals(true, jsonObject.getBoolean("a"))
        assertNull(jsonObject.getBoolean("g"))
        assertFails { jsonObject.getBoolean("b") }

        assertEquals(1, jsonObject.getByte("a"))
        assertFails { jsonObject.getByte("b") }
        assertEquals(3, jsonObject.getByte("c"))
        assertEquals(1, jsonObject.getByteValue("a"))
        assertEquals(0, jsonObject.getByteValue("b"))
        assertEquals(3, jsonObject.getByteValue("c"))

        assertEquals(1, jsonObject.getShort("a"))
        assertFails { jsonObject.getShort("b") }
        assertEquals(3, jsonObject.getShort("c"))
        assertEquals(1, jsonObject.getShortValue("a"))
        assertEquals(0, jsonObject.getShortValue("b"))
        assertEquals(3, jsonObject.getShortValue("c"))

        assertEquals(1, jsonObject.getInteger("a"))
        assertFails { jsonObject.getInteger("b") }
        assertEquals(3, jsonObject.getInteger("c"))
        assertEquals(1, jsonObject.getIntValue("a"))
        assertEquals(0, jsonObject.getIntValue("b"))
        assertEquals(3, jsonObject.getIntValue("c"))
        assertEquals(5, jsonObject.getIntValue("e"))

        assertEquals(1L, jsonObject.getLong("a"))
        assertFails { jsonObject.getLong("b") }
        assertEquals(3L, jsonObject.getLong("c"))
        assertEquals(1L, jsonObject.getLongValue("a"))
        assertEquals(0L, jsonObject.getLongValue("b"))
        assertEquals(3L, jsonObject.getLongValue("c"))
        assertEquals(5L, jsonObject.getLongValue("e"))

        assertEquals(1f, jsonObject.getFloat("a"))
        assertFails { jsonObject.getFloat("b") }
        assertEquals(3f, jsonObject.getFloat("c"))
        assertEquals(1f, jsonObject.getFloatValue("a"))
        assertEquals(0f, jsonObject.getFloatValue("b"))
        assertEquals(3f, jsonObject.getFloatValue("c"))
        assertEquals(5.1f, jsonObject.getFloatValue("e"))

        assertEquals(1.0, jsonObject.getDouble("a"))
        assertFails { jsonObject.getDouble("b") }
        assertEquals(3.0, jsonObject.getDouble("c"))
        assertEquals(1.0, jsonObject.getDoubleValue("a"))
        assertEquals(0.0, jsonObject.getDoubleValue("b"))
        assertEquals(3.0, jsonObject.getDoubleValue("c"))
        assertEquals(5.1, jsonObject.getDoubleValue("e"))

        assertFails { jsonObject.getBigDecimal("a") }
        assertFails { jsonObject.getBigDecimal("b") }
        assertEquals(BigDecimal(3), jsonObject.getBigDecimal("c"))
        assertEquals(BigDecimal("5.1"), jsonObject.getBigDecimal("e"))

        assertFails { jsonObject.getBigInteger("a") }
        assertFails { jsonObject.getBigInteger("b") }
        assertEquals(BigInteger("3"), jsonObject.getBigInteger("c"))
        assertEquals(BigInteger("5"), jsonObject.getBigInteger("e"))

        assertEquals("true", jsonObject.getString("a"))
        assertEquals("¯¯ðƒ©ƒˀ¸ˇ", jsonObject.getString("b"))
        assertEquals("5.1", jsonObject.getString("e"))
        assertNull(jsonObject.getString("g"))

    }

    data class TestClass(val foo: String)
}
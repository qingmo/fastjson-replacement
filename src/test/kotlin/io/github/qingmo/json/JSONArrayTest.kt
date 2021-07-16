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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

internal class JSONArrayTest {

    @Test
    fun `test init with size`() {
        val data = JSONArray(5)
        assertNotNull(data)
        assertTrue(data.isEmpty())
        assertFalse(data.contains("haha"))
        assertFailsWith(
            exceptionClass = IndexOutOfBoundsException::class,
            message = "Index: 10",
            block = {
                data.getJSONObject(10)
            }
        )

        val value = JSONObject(mutableMapOf("a" to "b"))
        data.add(value)
        data.add(null)
        assertEquals(value, data.getJSONObject(0))
        assertNull(data.getJSONObject(1))
        assertTrue(data.contains(null))
        assertNull(data.getObject(1, String::class.java))
        data.clear()
        assertTrue(data.isEmpty())
        data.addAll(listOf("haha", "hoho"))
        assertEquals("haha", data.getObject(0, String::class.java))
        assertEquals(2, data.size)
        data.addAll(2, listOf("foo", "bar"))
        assertEquals("foo", data.getString(2))
        assertTrue(data.containsAll(listOf("haha", "foo")))
        assertTrue(data.removeAll(listOf("haha", "foo")))
        assertFalse(data.containsAll(listOf("haha", "foo")))
        assertTrue(data.retainAll(listOf("hoho")))
        assertEquals(1, data.size)
    }

    @Test
    fun `test getJSONArray`() {
        val data = JSONArray()
        data.add(JSONArray())
        data.add("haha")
        assertNotNull(data.getJSONArray(0))
        assertNull(data.getJSONArray(1))
    }

    @Test
    fun `test getJSONArray list ops`() {
        val data = JSONArray()
        data.addAll(
            listOf(
                "a",
                "b",
                "c",
                "d",
                "e"
            )
        )
        assertEquals(listOf("a", "b"), data.subList(0, 2))
        data.replaceAll {
            if (it == "b")
                "2"
            else
                it
        }
        assertEquals("2", data[1])
        data.removeAt(1)
        assertEquals("a", data[0])
        assertEquals("c", data[1])

        assertNotNull(data.listIterator())
        assertEquals("a", data.listIterator().next())
        assertNotNull(data.listIterator(1))
        assertEquals("c", data.listIterator(1).next())
        assertFails { data.listIterator(10) }
        assertNotNull(data.spliterator())
    }

    @Test
    fun `test get different type`() {
        val data = JSONArray()
        data.add(true)
        data.add("haha")
        data.add("3")
        data.add("4.1")
        assertTrue(data.getBooleanValue(0))
        assertFalse(data.getBooleanValue(1))

        assertEquals(1, data.getByte(0))
        assertFails { data.getByte(1) }
        assertEquals(3, data.getByte(2))

        assertEquals(1, data.getByteValue(0))
        assertEquals(0, data.getByteValue(1))
        assertEquals(3, data.getByteValue(2))

        assertEquals(1, data.getShort(0))
        assertFails { data.getShort(1) }
        assertEquals(3, data.getShort(2))

        assertEquals(1, data.getShortValue(0))
        assertEquals(0, data.getShortValue(1))
        assertEquals(3, data.getShortValue(2))

        assertEquals(1, data.getInteger(0))
        assertFails { data.getInteger(1) }
        assertEquals(3, data.getInteger(2))

        assertEquals(1, data.getIntValue(0))
        assertEquals(0, data.getIntValue(1))
        assertEquals(3, data.getIntValue(2))

        assertEquals(1, data.getLong(0))
        assertFails { data.getLong(1) }
        assertEquals(3, data.getLong(2))

        assertEquals(1, data.getLongValue(0))
        assertEquals(0, data.getLongValue(1))
        assertEquals(3, data.getLongValue(2))

        assertEquals(1f, data.getFloat(0))
        assertFails { data.getFloat(1) }
        assertEquals(4.1f, data.getFloat(3))

        assertEquals(1f, data.getFloatValue(0))
        assertEquals(0f, data.getFloatValue(1))
        assertEquals(4.1f, data.getFloatValue(3))

        assertEquals(1.0, data.getDouble(0))
        assertFails { data.getDouble(1) }
        assertEquals(4.1, data.getDouble(3))

        assertEquals(1.0, data.getDoubleValue(0))
        assertEquals(0.0, data.getDoubleValue(1))
        assertEquals(4.1, data.getDoubleValue(3))

        assertFails { data.getBigDecimal(1) }
        assertEquals(BigDecimal("4.1"), data.getBigDecimal(3))
        assertFails { data.getBigInteger(1) }
        assertEquals(BigInteger("3"), data.getBigInteger(2))


    }

    @Test
    fun `test toJavaList`() {
        val data1 = JSONArray()
        data1.add("haha")
        val value = data1.toJavaList(String::class.java)
        assertNotNull(value)
        assertEquals(1, value.size)
        assertEquals("haha", value[0])
        assertEquals("""["haha"]""", data1.toString())
        assertTrue(data1.equals(listOf("haha")))
        assertEquals(listOf("haha").hashCode(), data1.hashCode())
        val array = data1.toArray()
        assertEquals(1, array.size)
        assertEquals("haha", array[0] as String)
        data1.remove("haha")
        assertTrue(data1.isEmpty())
        data1.add(0, "haha")
        data1.add(1, "heihei")
        assertEquals(0, data1.indexOf("haha"))
        assertEquals(1, data1.lastIndexOf("heihei"))
        data1.set(0, "haha2")
    }

    @Test
    fun `test sort`() {
        val data1 = JSONArray()
        data1.addAll(listOf("e", "d", "c", "b", "a"))
        data1.sortWith(Comparator { o1, o2 -> o1.hashCode() - o2.hashCode() })
        assertTrue(data1.equals(listOf("a", "b", "c", "d", "e")))
    }

}
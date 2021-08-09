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
import io.github.qingmo.json.internal.TypeUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

internal class TypeUtilsTest {
    @Test
    fun `test castToString`() {
        assertNull(TypeUtils.castToString(null))
        assertEquals("1", TypeUtils.castToString(1))
    }

    @Test
    fun `test castToByte`() {
        assertNull(TypeUtils.castToByte(null))
        assertEquals(1, TypeUtils.castToByte(BigDecimal.ONE))
        assertEquals(5, TypeUtils.castToByte(5.1))
        assertEquals(5, TypeUtils.castToByte(5.1f))
        assertEquals(3, TypeUtils.castToByte(3))
        assertNull(TypeUtils.castToByte(""))
        assertNull(TypeUtils.castToByte("null"))
        assertNull(TypeUtils.castToByte("NULL"))
        assertNull(TypeUtils.castToByte("NuLL"))
        assertNull(TypeUtils.castToByte("Null"))
        assertEquals(1, TypeUtils.castToByte(true))
        assertEquals(0, TypeUtils.castToByte(false))
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to byte, value :",
            block = {
                TypeUtils.castToByte(Pair(1, 2))
            }
        )
    }

    @Test
    fun `test castToChar`() {
        assertNull(TypeUtils.castToChar(null))
        assertEquals('a', TypeUtils.castToChar('a'))
        assertNull(TypeUtils.castToChar(""))
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to char, value :",
            block = {
                TypeUtils.castToChar("abc")
            }
        )
        assertEquals('a', TypeUtils.castToChar("a"))
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to char, value :",
            block = {
                TypeUtils.castToChar(Pair(1, 2))
            }
        )
    }

    @Test
    fun `test castToShort`() {
        assertNull(TypeUtils.castToShort(null))
        assertEquals(1, TypeUtils.castToShort(BigDecimal.ONE))
        assertEquals(1, TypeUtils.castToShort(1))
        assertEquals(2, TypeUtils.castToShort(2.1))
        assertEquals(3, TypeUtils.castToShort(3.6f))
        assertNull(TypeUtils.castToShort(""))
        assertNull(TypeUtils.castToShort("null"))
        assertNull(TypeUtils.castToShort("NULL"))
        assertNull(TypeUtils.castToShort("NuLL"))
        assertNull(TypeUtils.castToShort("Null"))
        assertFailsWith(
            exceptionClass = NumberFormatException::class,
            block = {
                TypeUtils.castToShort("abc")
            }
        )
        assertEquals(14, TypeUtils.castToShort("14"))
        assertEquals(1, TypeUtils.castToShort(true))
        assertEquals(0, TypeUtils.castToShort(false))
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to short, value :",
            block = {
                TypeUtils.castToShort(Pair(1, 2))
            }
        )
    }

    @Test
    fun `test castToBigDecimal`() {
        assertNull(TypeUtils.castToBigDecimal(null))
        assertEquals(BigDecimal("1.2"), TypeUtils.castToBigDecimal(1.2f))
        assertNull(TypeUtils.castToBigDecimal(Float.NaN))
        assertNull(TypeUtils.castToBigDecimal(Float.POSITIVE_INFINITY))
        assertNull(TypeUtils.castToBigDecimal(Float.NEGATIVE_INFINITY))
        assertEquals(BigDecimal("1.9"), TypeUtils.castToBigDecimal(1.9))
        assertNull(TypeUtils.castToBigDecimal(Double.NaN))
        assertNull(TypeUtils.castToBigDecimal(Double.POSITIVE_INFINITY))
        assertNull(TypeUtils.castToBigDecimal(Double.NEGATIVE_INFINITY))
        assertEquals(BigDecimal.ONE, TypeUtils.castToBigDecimal(BigDecimal.ONE))
        assertEquals(BigDecimal.ONE, TypeUtils.castToBigDecimal(BigInteger("1")))
        assertEquals(BigDecimal("2"), TypeUtils.castToBigDecimal(BigInteger("2")))
        assertFails { TypeUtils.castToBigDecimal(mapOf("1" to "2")) }
        assertNull(TypeUtils.castToBigDecimal(""))
        assertNull(TypeUtils.castToBigDecimal("null"))
        assertNull(TypeUtils.castToBigDecimal("NULL"))
        assertNull(TypeUtils.castToBigDecimal("NuLL"))
        assertNull(TypeUtils.castToBigDecimal("Null"))
        val longCharArray: CharArray = CharArray(65536) {
            it.toChar()
        }
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "decimal overflow",
            block = {
                assertNull(TypeUtils.castToBigDecimal(longCharArray.joinToString()))
            }
        )

    }


    @Test
    fun `test castToBigInteger`() {
        assertNull(TypeUtils.castToBigInteger(null))
        assertEquals(BigInteger("1"), TypeUtils.castToBigInteger(1.2f))
        assertNull(TypeUtils.castToBigInteger(Float.NaN))
        assertNull(TypeUtils.castToBigInteger(Float.POSITIVE_INFINITY))
        assertNull(TypeUtils.castToBigInteger(Float.NEGATIVE_INFINITY))
        assertEquals(BigInteger("1"), TypeUtils.castToBigInteger(1.9))
        assertNull(TypeUtils.castToBigInteger(Double.NaN))
        assertNull(TypeUtils.castToBigInteger(Double.POSITIVE_INFINITY))
        assertNull(TypeUtils.castToBigInteger(Double.NEGATIVE_INFINITY))
        assertEquals(BigInteger.ONE, TypeUtils.castToBigInteger(BigInteger.ONE))
        val bigDecimal = BigDecimal("1.5")
        assertEquals(BigInteger.ONE, TypeUtils.castToBigInteger(bigDecimal))
        val longFrancimal = BigDecimal("1.5e10")
        assertEquals(BigInteger("15000000000"), TypeUtils.castToBigInteger(longFrancimal))
        val longFrancimal2 = BigDecimal("1.5e1001")
        assertEquals(BigDecimal("15e1000").toBigInteger(), TypeUtils.castToBigInteger(longFrancimal2))
        assertNull(TypeUtils.castToBigInteger(""))
        assertNull(TypeUtils.castToBigInteger("null"))
        assertNull(TypeUtils.castToBigInteger("NULL"))
        assertNull(TypeUtils.castToBigInteger("NuLL"))
        assertNull(TypeUtils.castToBigInteger("Null"))
        val longCharArray: CharArray = CharArray(65536) {
            it.toChar()
        }
        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "decimal overflow",
            block = {
                assertNull(TypeUtils.castToBigInteger(longCharArray.joinToString()))
            }
        )

    }

    @Test
    fun `test castToFloat`() {
        assertNull(TypeUtils.castToFloat(null))
        assertEquals(1.2f, TypeUtils.castToFloat(1.2f))
        assertEquals(1.0f, TypeUtils.castToFloat(1))
        assertEquals(2.3f, TypeUtils.castToFloat(2.3))
        assertEquals(2.3f, TypeUtils.castToFloat("2.3"))
        assertNull(TypeUtils.castToFloat(""))
        assertNull(TypeUtils.castToFloat("null"))
        assertNull(TypeUtils.castToFloat("NULL"))
        assertNull(TypeUtils.castToFloat("NuLL"))
        assertNull(TypeUtils.castToFloat("Null"))
        assertEquals(1002.3f, TypeUtils.castToFloat("1,002.3"))
        assertEquals(1f, TypeUtils.castToFloat(true))
        assertEquals(0f, TypeUtils.castToFloat(false))

        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to float, value :",
            block = {
                assertNull(TypeUtils.castToFloat(Pair(1, 2)))
            }
        )
    }

    @Test
    fun `test castToDouble`() {
        assertNull(TypeUtils.castToDouble(null))
        val delta = 1.2- TypeUtils.castToDouble(1.2f)!!
        assertTrue(Math.abs(delta) < 0.000001)
        assertEquals(1.0, TypeUtils.castToDouble(1))
        assertEquals(2.3, TypeUtils.castToDouble(2.3))
        assertEquals(2.3, TypeUtils.castToDouble("2.3"))
        assertNull(TypeUtils.castToDouble(""))
        assertNull(TypeUtils.castToDouble("null"))
        assertNull(TypeUtils.castToDouble("NULL"))
        assertNull(TypeUtils.castToDouble("NuLL"))
        assertNull(TypeUtils.castToDouble("Null"))
        assertEquals(1002.3, TypeUtils.castToDouble("1,002.3"))
        assertEquals(1.0, TypeUtils.castToDouble(true))
        assertEquals(0.0, TypeUtils.castToDouble(false))

        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to double, value :",
            block = {
                assertNull(TypeUtils.castToDouble(Pair(1, 2)))
            }
        )
    }

    @Test
    fun `test castToInt`() {
        assertNull(TypeUtils.castToInt(null))
        assertEquals(1, TypeUtils.castToInt(1))
        assertEquals(1, TypeUtils.castToInt(BigDecimal.ONE))
        assertEquals(1000, TypeUtils.castToInt(BigDecimal("1000")))
        assertEquals(23, TypeUtils.castToInt("00023"))
        assertNull(TypeUtils.castToInt(""))
        assertNull(TypeUtils.castToInt("null"))
        assertNull(TypeUtils.castToInt("NULL"))
        assertNull(TypeUtils.castToInt("NuLL"))
        assertNull(TypeUtils.castToInt("Null"))
        assertEquals(1002, TypeUtils.castToInt("1,002"))
        assertEquals(5, TypeUtils.castToInt("5.00"))
        assertEquals(1, TypeUtils.castToInt(true))
        assertEquals(0, TypeUtils.castToInt(false))

        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to int, value :",
            block = {
                assertNull(TypeUtils.castToInt(Pair(1, 2)))
            }
        )
    }

    @Test
    fun `test castToLong`() {
        assertNull(TypeUtils.castToLong(null))
        assertEquals(1L, TypeUtils.castToLong(1))
        assertEquals(1L, TypeUtils.castToLong(BigDecimal.ONE))
        assertEquals(1000, TypeUtils.castToLong(BigDecimal("1000")))
        assertFailsWith(
            exceptionClass = NumberFormatException::class,
            block = {
                assertNull(TypeUtils.castToLong("abc"))
            }
        )
        assertNull(TypeUtils.castToLong(""))
        assertNull(TypeUtils.castToLong("null"))
        assertNull(TypeUtils.castToLong("NULL"))
        assertNull(TypeUtils.castToLong("NuLL"))
        assertNull(TypeUtils.castToLong("Null"))
        assertEquals(1002L, TypeUtils.castToLong("1,002"))
        assertEquals(1L, TypeUtils.castToLong(true))
        assertEquals(0L, TypeUtils.castToLong(false))

        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to long, value :",
            block = {
                assertNull(TypeUtils.castToLong(Pair(1, 2)))
            }
        )
    }

    @Test
    fun `test castToBoolean`() {
        assertNull(TypeUtils.castToBoolean(null))
        assertEquals(true, TypeUtils.castToBoolean(true))
        assertEquals(false, TypeUtils.castToBoolean(false))
        assertEquals(true, TypeUtils.castToBoolean(BigDecimal.ONE))
        assertEquals(false, TypeUtils.castToBoolean(BigDecimal.ZERO))
        assertEquals(false, TypeUtils.castToBoolean(BigDecimal.TEN))
        assertEquals(false, TypeUtils.castToBoolean(5))
        assertEquals(false, TypeUtils.castToBoolean(5.1))
        assertEquals(false, TypeUtils.castToBoolean(0))
        assertEquals(false, TypeUtils.castToBoolean(0.0))
        assertEquals(true, TypeUtils.castToBoolean(1.0))
        assertEquals(true, TypeUtils.castToBoolean(1))
        assertEquals(true, TypeUtils.castToBoolean("1"))
        assertEquals(false, TypeUtils.castToBoolean("0"))
        assertEquals(true, TypeUtils.castToBoolean("T"))
        assertEquals(true, TypeUtils.castToBoolean("Y"))
        assertEquals(false, TypeUtils.castToBoolean("F"))
        assertEquals(false, TypeUtils.castToBoolean("N"))
        assertEquals(true, TypeUtils.castToBoolean("y"))
        assertEquals(false, TypeUtils.castToBoolean("f"))
        assertEquals(true, TypeUtils.castToBoolean("true"))
        assertEquals(false, TypeUtils.castToBoolean("false"))
        assertEquals(true, TypeUtils.castToBoolean("TRUE"))
        assertEquals(false, TypeUtils.castToBoolean("FALSE"))
        assertNull(TypeUtils.castToBoolean(""))
        assertNull(TypeUtils.castToBoolean("null"))
        assertNull(TypeUtils.castToBoolean("NULL"))
        assertNull(TypeUtils.castToBoolean("NuLL"))
        assertNull(TypeUtils.castToBoolean("Null"))

        assertFailsWith(
            exceptionClass = JSONException::class,
            message = "can not cast to boolean, value :",
            block = {
                assertNull(TypeUtils.castToBoolean(Pair(1, 2)))
            }
        )
    }

    @Test
    fun `test byteValue`() {
        assertEquals(0, TypeUtils.byteValue(null))
        val data1 = BigDecimal("24.01")
        assertEquals(24, TypeUtils.byteValue(data1))
        assertFailsWith(
            exceptionClass = ArithmeticException::class,
            message = "Overflow",
            block = {
                TypeUtils.byteValue(BigDecimal((Short.MAX_VALUE + 1)))
            }
        )
        assertFailsWith(
            exceptionClass = ArithmeticException::class,
            message = "Overflow",
            block = {
                TypeUtils.byteValue(BigDecimal((Short.MIN_VALUE - 1)))
            }
        )
    }

    @Test
    fun `test null with type value`() {
        assertEquals(0, TypeUtils.longValue(null))
        assertEquals(0, TypeUtils.intValue(null))
        assertEquals(0, TypeUtils.shortValue(null))
    }

    @Test
    fun `test private constructor`() {
        val javaClazz = TypeUtils::class.java
        val con: Constructor<TypeUtils> = javaClazz.getDeclaredConstructor()
        con.isAccessible = true
        assertFailsWith(
            exceptionClass = InvocationTargetException::class,
            message = "can not instance static object",
            block = {
                con.newInstance()
            }
        )
    }

    @Test
    fun `test shortValue`() {
        assertEquals(0, TypeUtils.shortValue(null))
        assertEquals(0, TypeUtils.shortValue(BigDecimal.ZERO))
        assertEquals(1, TypeUtils.shortValue(BigDecimal.ONE))
    }

    @Test
    fun `test intValue`() {
        assertEquals(0, TypeUtils.intValue(null))
        assertEquals(0, TypeUtils.intValue(BigDecimal.ZERO))
        assertEquals(1, TypeUtils.intValue(BigDecimal.ONE))
    }

    @Test
    fun `test longValue`() {
        assertEquals(0, TypeUtils.longValue(null))
        assertEquals(0, TypeUtils.longValue(BigDecimal.ZERO))
        assertEquals(1, TypeUtils.longValue(BigDecimal.ONE))
    }
}
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
import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Matcher
import java.util.regex.Pattern

object TypeUtils {
    private val NUMBER_WITH_TRAILING_ZEROS_PATTERN: Pattern = Pattern.compile("\\.0*$")
    fun castToString(value: Any?): String? {
        return value?.toString()
    }

    fun castToByte(value: Any?): Byte? {
        if (value == null) {
            return null
        }
        if (value is BigDecimal) {
            return byteValue(value as BigDecimal?)
        }
        if (value is Number) {
            return value.toByte()
        }
        if (value is String) {
            val strVal = value
            return if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                null
            } else strVal.toByte()
        }
        if (value is Boolean) {
            return if (value) 1.toByte() else 0.toByte()
        }
        throw JSONException("can not cast to byte, value : $value")
    }

    fun castToChar(value: Any?): Char? {
        if (value == null) {
            return null
        }
        if (value is Char) {
            return value
        }
        if (value is String) {
            val strVal = value
            if (strVal.length == 0) {
                return null
            }
            if (strVal.length != 1) {
                throw JSONException("can not cast to char, value : $value")
            }
            return strVal[0]
        }
        throw JSONException("can not cast to char, value : $value")
    }

    fun castToShort(value: Any?): Short? {
        if (value == null) {
            return null
        }
        if (value is BigDecimal) {
            return shortValue(value as BigDecimal?)
        }
        if (value is Number) {
            return value.toShort()
        }
        if (value is String) {
            val strVal = value
            return if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                null
            } else strVal.toShort()
        }
        if (value is Boolean) {
            return if (value) 1.toShort() else 0.toShort()
        }
        throw JSONException("can not cast to short, value : $value")
    }

    fun castToBigDecimal(value: Any?): BigDecimal? {
        if (value == null) {
            return null
        }
        if (value is Float) {
            if (java.lang.Float.isNaN((value as Float?)!!) || java.lang.Float.isInfinite((value as Float?)!!)) {
                return null
            }
        } else if (value is Double) {
            if (java.lang.Double.isNaN((value as Double?)!!) || java.lang.Double.isInfinite((value as Double?)!!)) {
                return null
            }
        } else if (value is BigDecimal) {
            return value
        } else if (value is BigInteger) {
            return BigDecimal(value as BigInteger?)
        } else if (value is Map<*, *> && value.size == 0) {
            return null
        }
        val strVal = value.toString()
        if (strVal.isEmpty()
            || "null".equals(strVal, ignoreCase = true)
        ) {
            return null
        }
        if (strVal.length > 65535) {
            throw JSONException("decimal overflow")
        }
        return BigDecimal(strVal)
    }

    fun castToBigInteger(value: Any?): BigInteger? {
        if (value == null) {
            return null
        }
        if (value is Float) {
            val floatValue = value
            return if (java.lang.Float.isNaN(floatValue) || java.lang.Float.isInfinite(floatValue)) {
                null
            } else BigInteger.valueOf(floatValue.toLong())
        } else if (value is Double) {
            val doubleValue = value
            return if (java.lang.Double.isNaN(doubleValue) || java.lang.Double.isInfinite(doubleValue)) {
                null
            } else BigInteger.valueOf(doubleValue.toLong())
        } else if (value is BigInteger) {
            return value
        } else if (value is BigDecimal) {
            return value.toBigInteger()
        }
        val strVal = value.toString()
        if (strVal.isEmpty()
            || "null".equals(strVal, ignoreCase = true)
        ) {
            return null
        }
        if (strVal.length > 65535) {
            throw JSONException("decimal overflow")
        }
        return BigInteger(strVal)
    }

    fun castToFloat(value: Any?): Float? {
        if (value == null) {
            return null
        }
        if (value is Number) {
            return value.toFloat()
        }
        if (value is String) {
            var strVal = value.toString()
            if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                return null
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",".toRegex(), "")
            }
            return strVal.toFloat()
        }
        if (value is Boolean) {
            return if (value) 1f else 0f
        }
        throw JSONException("can not cast to float, value : $value")
    }

    fun castToDouble(value: Any?): Double? {
        if (value == null) {
            return null
        }
        if (value is Number) {
            return value.toDouble()
        }
        if (value is String) {
            var strVal = value.toString()
            if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                return null
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",".toRegex(), "")
            }
            return strVal.toDouble()
        }
        if (value is Boolean) {
            return if (value) 1.0 else 0.0
        }
        throw JSONException("can not cast to double, value : $value")
    }

    fun castToLong(value: Any?): Long? {
        if (value == null) {
            return null
        }
        if (value is BigDecimal) {
            return longValue(value as BigDecimal?)
        }
        if (value is Number) {
            return value.toLong()
        }
        if (value is String) {
            var strVal = value
            if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                return null
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",".toRegex(), "")
            }
            return try {
                strVal.toLong()
            } catch (ex: NumberFormatException) {
                throw ex
            }
        }
        if (value is Boolean) {
            return if (value) 1L else 0L
        }
        throw JSONException("can not cast to long, value : $value")
    }

    fun byteValue(decimal: BigDecimal?): Byte {
        if (decimal == null) {
            return 0
        }
        return decimal.toBigInteger().toBigDecimal().byteValueExact()
    }

    fun shortValue(decimal: BigDecimal?): Short {
        if (decimal == null) {
            return 0
        }
        return decimal.toBigInteger().toBigDecimal().shortValueExact()
    }

    fun intValue(decimal: BigDecimal?): Int {
        if (decimal == null) {
            return 0
        }
        return decimal.toBigInteger().toBigDecimal().intValueExact()
    }

    fun longValue(decimal: BigDecimal?): Long {
        if (decimal == null) {
            return 0
        }
        return decimal.toBigInteger().toBigDecimal().longValueExact()
    }

    fun castToInt(value: Any?): Int? {
        if (value == null) {
            return null
        }
        if (value is Int) {
            return value
        }
        if (value is BigDecimal) {
            return intValue(value as BigDecimal?)
        }
        if (value is Number) {
            return value.toInt()
        }
        if (value is String) {
            var strVal = value
            if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                return null
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",".toRegex(), "")
            }
            val matcher: Matcher = NUMBER_WITH_TRAILING_ZEROS_PATTERN.matcher(strVal)
            if (matcher.find()) {
                strVal = matcher.replaceAll("")
            }
            return (strVal as String).toInt()
        }
        if (value is Boolean) {
            return if (value) 1 else 0
        }

        throw JSONException("can not cast to int, value : $value")
    }

    fun castToBoolean(value: Any?): Boolean? {
        if (value == null) {
            return null
        }
        if (value is Boolean) {
            return value
        }
        if (value is BigDecimal) {
            return intValue(value as BigDecimal?) == 1
        }
        if (value is Number) {
            return value.toInt() == 1
        }
        if (value is String) {
            val strVal = value
            if (strVal.length == 0 //
                || "null".equals(strVal, ignoreCase = true)
            ) {
                return null
            }
            if ("true".equals(strVal, ignoreCase = true) //
                || "1" == strVal
            ) {
                return java.lang.Boolean.TRUE
            }
            if ("false".equals(strVal, ignoreCase = true) //
                || "0" == strVal
            ) {
                return java.lang.Boolean.FALSE
            }
            if ("Y".equals(strVal, ignoreCase = true) //
                || "T" == strVal
            ) {
                return java.lang.Boolean.TRUE
            }
            if ("F".equals(strVal, ignoreCase = true) //
                || "N" == strVal
            ) {
                return java.lang.Boolean.FALSE
            }
        }
        throw JSONException("can not cast to boolean, value : $value")
    }
}

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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ser.std.DateSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.qingmo.json.exception.JSONException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

object JSON {

    val STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss"
    val DATE_PATTERN = "yyyy-MM-dd"
    val TIME_PATTERN = "HH:mm:ss"

    private val objectMapper = jacksonObjectMapper()

    init {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)

        // 初始化JavaTimeModule
        val javaTimeModule = JavaTimeModule()
        //处理LocalDateTime
        val dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN)
        javaTimeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
        javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateTimeFormatter))
        //处理LocalDate
        val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        javaTimeModule.addSerializer(LocalDate::class.java, LocalDateSerializer(dateFormatter))
        javaTimeModule.addDeserializer(LocalDate::class.java, LocalDateDeserializer(dateFormatter))

        //处理LocalTime
        val timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
        javaTimeModule.addSerializer(LocalTime::class.java, LocalTimeSerializer(timeFormatter))
        javaTimeModule.addDeserializer(LocalTime::class.java, LocalTimeDeserializer(timeFormatter))

        javaTimeModule.addSerializer(Date::class.java, DateSerializer(false, SimpleDateFormat(STANDARD_PATTERN)))
        javaTimeModule.addDeserializer(Date::class.java, MultiDateDeserializer())

        //注册时间模块, 支持支持jsr310, 即新的时间类(java.time包下的时间类)
        objectMapper.registerModule(javaTimeModule)
    }

    fun toJSONString(jsonString: Any?): String {
        return try {
            objectMapper.writeValueAsString(jsonString)
        } catch (e: Exception) {
            throw JSONException(e)
        }
    }

    fun parse(jsonString: String): Any {
        if (isJsonObj(jsonString)) {
            return parseObject(jsonString)
        }
        return if (isJsonArray(jsonString)) {
            parseArray(jsonString)
        } else try {
            objectMapper.readValue(jsonString, JsonNode::class.java)
        } catch (e: Exception) {
            throw JSONException(e)
        }
    }

    fun parseObject(jsonString: String?): JSONObject {
        try {
            return objectMapper.readValue(jsonString!!)
        } catch (e: Exception) {
            throw JSONException(e)
        }
    }

    fun <T> parseObject(jsonString: String?, clazz: Class<T>?): T {
        return try {
            objectMapper.readValue(jsonString, clazz)
        } catch (e: Exception) {
            throw JSONException(e)
        }
    }

    fun parseArray(jsonString: String?): JSONArray {
        try {
            val result: JSONArray = objectMapper.readValue(jsonString!!)
            return result
        } catch (e: Exception) {
            throw JSONException(e)
        }
    }

    fun <T> parseArray(jsonString: String?, clazz: Class<T>?): List<T> {
        return try {
            val javaType: JavaType = objectMapper.typeFactory.constructParametricType(MutableList::class.java, clazz)
            objectMapper.readValue(jsonString, javaType)
        } catch (e: Exception) {
            throw JSONException(e)
        }
    }

    /**
     * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    fun isJson(str: String?): Boolean {
        return isJsonObj(str) || isJsonArray(str)
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    fun isJsonObj(str: String?): Boolean {
        return if (str.isNullOrBlank()) {
            false
        } else {
            str.trim().startsWith('{') && str.trim().endsWith('}')
        }
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    fun isJsonArray(str: String?): Boolean {
        return if (str.isNullOrBlank()) {
            false
        } else {
            str.trim().startsWith('[') && str.trim().endsWith(']')
        }
    }

}

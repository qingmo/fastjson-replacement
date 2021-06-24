package com.github.qingmo.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.qingmo.json.exception.JSONException


object JSON {

    private val objectMapper = jacksonObjectMapper()
    init {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
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
            val result: JSONObject = objectMapper.readValue(jsonString!!)
            return result
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
            val result:JSONArray = objectMapper.readValue(jsonString!!)
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
        return if (isBlank(str)) {
            false
        } else isWrap(str!!.trim { it <= ' ' }, '{', '}')
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    fun isJsonArray(str: String?): Boolean {
        return if (isBlank(str)) {
            false
        } else isWrap(str!!.trim { it <= ' ' }, '[', ']')
    }

    private fun isBlank(str: String?): Boolean {
        return str == null || str.isBlank()
    }

    private fun isWrap(str: String, start: Char, end: Char): Boolean {
        return if (isBlank(str)) {
            false
        } else str[0] == start && str[str.length - 1] == end
    }
}

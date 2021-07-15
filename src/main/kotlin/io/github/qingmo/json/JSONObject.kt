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

import io.github.qingmo.json.JSON.parseObject
import java.math.BigDecimal
import java.math.BigInteger
import java.util.HashMap

class JSONObject : MutableMap<Any, Any?> {
    private var map: MutableMap<Any, Any?>

    constructor(map: MutableMap<Any, Any?>) {
        this.map = map
    }

    constructor() {
        map = HashMap()
    }

    constructor(size: Int) {
        map = HashMap(size)
    }

    fun toJSONString(): String {
        return JSON.toJSONString(map)
    }

    fun getJSONObject(key: String): JSONObject? {
        val value = map.get(key) ?: return null
        if (value is JSONObject) {
            return value
        }
        return JSON.parseObject(JSON.toJSONString(value))
    }

    fun getJSONArray(key: String): JSONArray {
        val value = map[key] ?: return JSONArray()
        if (value is JSONArray) {
            return value
        }
        return JSON.parseArray(JSON.toJSONString(value))
    }

    
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val obj = map[key] ?: return null
        return parseObject(JSON.toJSONString(obj), clazz)
    }

    fun getBoolean(key: String): Boolean? {
        val value = get(key) ?: return null
        return TypeUtils.castToBoolean(value)
    }

    fun getBooleanValue(key: String): Boolean {
        val value = get(key)
        val booleanVal: Boolean = TypeUtils.castToBoolean(value) ?: return false
        return booleanVal
    }

    fun getByte(key: String): Byte? {
        val value = get(key)
        return TypeUtils.castToByte(value)
    }

    fun getByteValue(key: String): Byte {
        val value = get(key)
        return TypeUtils.castToByte(value) ?: return 0
    }

    fun getShort(key: String): Short? {
        val value = get(key)
        return TypeUtils.castToShort(value)
    }

    fun getShortValue(key: String): Short {
        val value = get(key)
        return TypeUtils.castToShort(value) ?: return 0
    }

    fun getInteger(key: String): Int? {
        val value = get(key)
        return TypeUtils.castToInt(value)
    }

    fun getIntValue(key: String): Int {
        val value = get(key)
        return TypeUtils.castToInt(value) ?: return 0
    }

    fun getLong(key: String): Long? {
        val value = get(key)
        return TypeUtils.castToLong(value)
    }

    fun getLongValue(key: String): Long {
        val value = get(key)
        return TypeUtils.castToLong(value) ?: return 0L
    }

    fun getFloat(key: String): Float? {
        val value = get(key)
        return TypeUtils.castToFloat(value)
    }

    fun getFloatValue(key: String): Float {
        val value = get(key)
        return TypeUtils.castToFloat(value) ?: return 0f
    }

    fun getDouble(key: String): Double? {
        val value = get(key)
        return TypeUtils.castToDouble(value)
    }

    fun getDoubleValue(key: String): Double {
        val value = get(key)
        return TypeUtils.castToDouble(value) ?: return 0.0
    }

    fun getBigDecimal(key: String): BigDecimal? {
        val value = get(key)
        return TypeUtils.castToBigDecimal(value)
    }

    fun getBigInteger(key: String): BigInteger? {
        val value = get(key)
        return TypeUtils.castToBigInteger(value)
    }

    fun getString(key: String): String? {
        val value = get(key) ?: return null
        return value.toString()
    }

    fun <T> toJavaObject(clazz: Class<T>): T {
        return if (clazz == MutableMap::class.java || clazz == JSONObject::class.java || clazz == JSONArray::class.java) {
            @Suppress("UNCHECKED_CAST")
            this as T
        } else parseObject(toJSONString(), clazz)
    }

    override fun toString(): String {
        return toJSONString()
    }

    override fun remove(key: Any): Any? {
        return map.remove(key)
    }

    override val size: Int
        get() = map.size

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun containsKey(key: Any): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: Any?): Boolean {
        return map.containsValue(value)
    }

    override fun get(key: Any): Any? {
        return map.get(key)
    }

    override fun put(key: Any, value: Any?): Any? {
        return map.put(key, value)
    }

    override fun putAll(from: Map<out Any, Any?>) {
        map.putAll(from)
    }

    override fun clear() {
        map.clear()
    }

    override val keys: MutableSet<Any>
        get() = map.keys

    override val values: MutableCollection<Any?>
        get() = map.values

    override val entries: MutableSet<MutableMap.MutableEntry<Any, Any?>>
        get() = map.entries

    override fun equals(other: Any?): Boolean {
        return map == other
    }

    override fun hashCode(): Int {
        return map.hashCode()
    }
}


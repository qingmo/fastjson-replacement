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

import io.github.qingmo.json.JSON.parseArray
import io.github.qingmo.json.internal.TypeUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList
import java.util.Comparator
import java.util.Spliterator
import java.util.function.UnaryOperator

class JSONArray : MutableList<Any?> {
    private var list: MutableList<Any?>

    constructor(list: MutableList<Any?>) {
        this.list = list
    }

    constructor(size: Int) {
        list = ArrayList(size)
    }

    constructor() {
        list = ArrayList(16)
    }

    fun getJSONObject(index: Int): JSONObject? {
        val value = list.get(index) ?: return null
        if (value is JSONObject) {
            return value
        }
        return JSON.parseObject(JSON.toJSONString(value))
    }

    fun getJSONArray(index: Int): JSONArray? {
        val value = list[index]
        if (value is JSONArray) {
            return value
        }
        if (value !is List<*>) {
            return null
        }
        val params = value.toMutableList()
        return JSONArray(params)
    }

    fun getBoolean(index: Int): Boolean? {
        val value = get(index)
        return TypeUtils.castToBoolean(value)
    }

    fun getBooleanValue(index: Int): Boolean {
        val value = get(index)
        try {
            return TypeUtils.castToBoolean(value)!!
        } catch (ignore: Exception) {
            return false
        }
    }

    fun <T> toJavaList(clazz: Class<T>?): List<T> {
        return parseArray(toJSONString(), clazz)
    }

    fun getByte(index: Int): Byte? {
        val value = get(index)
        return TypeUtils.castToByte(value)
    }

    fun getByteValue(index: Int): Byte {
        val value = get(index)
        try {
            return TypeUtils.castToByte(value)!!
        } catch (ignore: Exception) {
            return 0
        }
    }

    fun getShort(index: Int): Short? {
        val value = get(index)
        return TypeUtils.castToShort(value)
    }

    fun getShortValue(index: Int): Short {
        val value = get(index)
        try {
            return TypeUtils.castToShort(value)!!
        } catch (ignore: Exception) {
            return 0
        }
    }

    fun getInteger(index: Int): Int? {
        val value = get(index)
        return TypeUtils.castToInt(value)
    }

    fun getIntValue(index: Int): Int {
        val value = get(index)
        try {
            return TypeUtils.castToInt(value)!!
        } catch (ignore: Exception) {
            return 0
        }
    }

    fun getLong(index: Int): Long? {
        val value = get(index)
        return TypeUtils.castToLong(value)
    }

    fun getLongValue(index: Int): Long {
        val value = get(index)
        try {
            return TypeUtils.castToLong(value)!!
        } catch (ignore: Exception) {
            return 0L
        }
    }

    fun getFloat(index: Int): Float? {
        val value = get(index)
        return TypeUtils.castToFloat(value)
    }

    fun getFloatValue(index: Int): Float {
        val value = get(index)
        try {
            return TypeUtils.castToFloat(value)!!
        } catch (ignore: Exception) {
            return 0f
        }
    }

    fun getDouble(index: Int): Double? {
        val value = get(index)
        return TypeUtils.castToDouble(value)
    }

    fun getDoubleValue(index: Int): Double {
        val value = get(index)
        try {
            return TypeUtils.castToDouble(value)!!
        } catch (ignore: Exception) {
            return 0.0
        }
    }

    fun getBigDecimal(index: Int): BigDecimal? {
        val value = get(index)
        return TypeUtils.castToBigDecimal(value)
    }

    fun getBigInteger(index: Int): BigInteger? {
        val value = get(index)
        return TypeUtils.castToBigInteger(value)
    }

    fun getString(index: Int): String? {
        val value = get(index)
        return TypeUtils.castToString(value)
    }


    fun <T> getObject(index: Int, clazz: Class<T>): T? {
        val obj = list.get(index) ?: return null
        return JSON.parseObject(JSON.toJSONString(obj), clazz)
    }

    fun toJSONString(): String {
        return JSON.toJSONString(list)
    }

    override fun toString(): String {
        return toJSONString()
    }

    override val size: Int
        get() = list.size

    override fun isEmpty(): Boolean {
        return list.isEmpty()
    }

    override fun contains(element: Any?): Boolean {
        return list.contains(element)
    }

    override fun iterator(): MutableIterator<Any?> {
        return list.iterator()
    }

    fun toArray(): Array<Any?> {
        return list.toTypedArray()
    }

    override fun add(element: Any?): Boolean {
        return list.add(element)
    }

    override fun remove(element: Any?): Boolean {
        return list.remove(element)
    }

    override fun containsAll(elements: Collection<Any?>): Boolean {
        return list.containsAll(elements)
    }

    override fun addAll(elements: Collection<Any?>): Boolean {
        return list.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<Any?>): Boolean {
        return list.addAll(index, elements)
    }

    override fun removeAll(elements: Collection<Any?>): Boolean {
        return list.removeAll(elements)
    }

    override fun retainAll(elements: Collection<Any?>): Boolean {
        return list.retainAll(elements)
    }

    override fun clear() {
        list.clear()
    }

    override fun equals(other: Any?): Boolean {
        return list == other
    }

    override fun hashCode(): Int {
        return list.hashCode()
    }

    override fun get(index: Int): Any? {
        return list[index]
    }

    override operator fun set(index: Int, element: Any?): Any? {
        return list.set(index, element)
    }

    override fun add(index: Int, element: Any?) {
        list.add(index, element)
    }

    override fun removeAt(index: Int): Any? {
        return list.removeAt(index)
    }

    override fun indexOf(element: Any?): Int {
        return list.indexOf(element)
    }

    override fun lastIndexOf(element: Any?): Int {
        return list.lastIndexOf(element)
    }

    override fun listIterator(): MutableListIterator<Any?> {
        return list.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<Any?> {
        return list.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Any?> {
        return list.subList(fromIndex, toIndex)
    }

    override fun replaceAll(operator: UnaryOperator<Any?>) {
        list.replaceAll(operator)
    }

    override fun sort(c: Comparator<in Any?>) {
        list.sortWith(c)
    }

    override fun spliterator(): Spliterator<Any?> {
        return list.spliterator()
    }
}

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
package io.github.qingmo.json;

import io.github.qingmo.json.internal.TypeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;

public final class JSONArray implements List<Object> {
    private List<Object> list;

    public JSONArray(List<Object> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
    }

    public JSONArray(int size) {
        this.list = new ArrayList<>(size);
    }

    public JSONArray() {
        this.list = new ArrayList<>(16);
    }

    public final JSONObject getJSONObject(int index) {
        Object value = this.list.get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return JSON.parseObject(JSON.toJSONString(value));
    }

    public final JSONArray getJSONArray(int index) {
        Object value = this.list.get(index);
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        if (value instanceof List) {
            return new JSONArray((List) value);
        }
        return null;
    }

    public final Boolean getBoolean(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToBoolean(value);
    }

    public final boolean getBooleanValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToBoolean(value);
        } catch (Exception e) {
            return false;
        }
    }

    public final <T> List<T> toJavaList(Class<T> clazz) {
        return JSON.parseArray(this.toJSONString(), clazz);
    }

    public final Byte getByte(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToByte(value);
    }

    public final byte getByteValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToByte(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public final Short getShort(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToShort(value);
    }

    public final short getShortValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToShort(value);
        } catch (Exception ignore) {
            return 0;
        }
    }

    public final Integer getInteger(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToInt(value);
    }

    public final int getIntValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToInt(value);
        } catch (Exception ignore) {
            return 0;
        }
    }

    public final Long getLong(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToLong(value);
    }

    public final long getLongValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToLong(value);
        } catch (Exception ignore) {
            return 0L;
        }
    }

    public final Float getFloat(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToFloat(value);
    }

    public final float getFloatValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToFloat(value);
        } catch (Exception ignore) {
            return 0.0F;
        }
    }

    public final Double getDouble(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToDouble(value);
    }

    public final double getDoubleValue(int index) {
        Object value = this.list.get(index);
        try {
            return TypeUtils.castToDouble(value);
        } catch (Exception ignore) {
            return 0.0D;
        }
    }

    public final BigDecimal getBigDecimal(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToBigDecimal(value);
    }

    public final BigInteger getBigInteger(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToBigInteger(value);
    }

    public final String getString(int index) {
        Object value = this.list.get(index);
        return TypeUtils.castToString(value);
    }

    public final <T> T getObject(int index, Class<T> clazz) {
        Object value = this.list.get(index);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(value), clazz);
    }

    public final String toJSONString() {
        return JSON.toJSONString(this.list);
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    public int getSize() {
        return this.list.size();
    }

    @Override
    public final int size() {
        return this.getSize();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object element) {
        return this.list.contains(element);
    }

    @Override
    public Iterator<Object> iterator() {
        return this.list.iterator();
    }

    @Override
    public final Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public boolean add(Object element) {
        return this.list.add(element);
    }

    @Override
    public Object remove(int index) {
        return this.list.remove(index);
    }

    @Override
    public boolean remove(Object element) {
        return this.list.remove(element);
    }

    @Override
    public boolean containsAll(Collection elements) {
        return this.list.containsAll(elements);
    }

    @Override
    public boolean addAll(Collection elements) {
        return this.list.addAll(elements);
    }

    @Override
    public boolean addAll(int index, Collection elements) {
        return this.list.addAll(index, elements);
    }

    @Override
    public boolean removeAll(Collection elements) {
        return this.list.removeAll(elements);
    }

    @Override
    public boolean retainAll(Collection elements) {
        return this.list.retainAll(elements);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public boolean equals(Object other) {
        return this.list.equals(other);
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public Object get(int index) {
        return this.list.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return this.list.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        this.list.add(index, element);
    }

    @Override
    public int indexOf(Object element) {
        return this.list.indexOf(element);
    }

    @Override
    public int lastIndexOf(Object element) {
        return this.list.lastIndexOf(element);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return this.list.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return this.list.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    @Override
    public void replaceAll(UnaryOperator operator) {
        this.list.replaceAll(operator);
    }

    @Override
    public void sort(Comparator c) {
        this.list.sort(c);
    }

    @Override
    public Spliterator<Object> spliterator() {
        return this.list.spliterator();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }
}

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObject implements Map<String, Object> {

    private Map<String, Object> map;

    public JSONObject(Map<String, Object> map) {
        this.map = map;
    }

    public JSONObject() {
        this.map = new HashMap<>();
    }

    public JSONObject(Integer size) {
        this.map = new HashMap<>(size);
    }

    public String toJSONString() {
        return JSON.toJSONString(this.map);
    }

    public JSONObject getJSONObject(String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }
        return JSON.parseObject(JSON.toJSONString(value));
    }

    public JSONArray getJSONArray(String key) {
        Object value = map.get(key);
        if (value == null) {
            return new JSONArray();
        }
        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }
        return JSON.parseArray(JSON.toJSONString(value));
    }


    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        if (obj == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }

    public Boolean getBoolean(String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return TypeUtils.castToBoolean(value);
    }

    public Boolean getBooleanValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToBoolean(value);
        } catch (Exception ignore) {
            return false;
        }
    }

    public Byte getByte(String key) {
        Object value = map.get(key);
        return TypeUtils.castToByte(value);
    }

    public Byte getByteValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToByte(value);
        } catch (Exception ignore) {
            return (byte) 0;
        }
    }

    public Short getShort(String key) {
        Object value = map.get(key);
        return TypeUtils.castToShort(value);
    }

    public Short getShortValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToShort(value);
        } catch (Exception ignore) {
            return (short) 0;
        }
    }

    public Integer getInteger(String key) {
        Object value = map.get(key);
        return TypeUtils.castToInt(value);
    }

    public Integer getIntValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToInt(value);
        } catch (Exception ignore) {
            return 0;
        }
    }

    public Long getLong(String key) {
        Object value = map.get(key);
        return TypeUtils.castToLong(value);
    }

    public Long getLongValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToLong(value);
        } catch (Exception ignore) {
            return 0L;
        }
    }

    public Float getFloat(String key) {
        Object value = map.get(key);
        return TypeUtils.castToFloat(value);
    }

    public Float getFloatValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToFloat(value);
        } catch (Exception ignore) {
            return 0f;
        }
    }

    public Double getDouble(String key) {
        Object value = map.get(key);
        return TypeUtils.castToDouble(value);
    }

    public Double getDoubleValue(String key) {
        Object value = map.get(key);
        try {
            return TypeUtils.castToDouble(value);
        } catch (Exception ignore) {
            return 0.0;
        }
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = map.get(key);
        return TypeUtils.castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = map.get(key);
        return TypeUtils.castToBigInteger(value);
    }

    public String getString(String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JSONObject.class) {
            return (T) this;
        } else {
            return JSON.parseObject(toJSONString(), clazz);
        }
    }

    @Override
    public String toString() {
        return toJSONString();
    }


    @Override
    public Object remove(Object key) {
        return map.remove(key.toString());
    }


    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {

        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }


    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}

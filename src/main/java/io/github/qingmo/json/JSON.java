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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.github.qingmo.json.exception.JSONException;
import io.github.qingmo.json.internal.MultiDateDeserializer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public final class JSON {
    public static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();

        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        // 初始化JavaTimeModule
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        //处理LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        //处理LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        //处理LocalTime
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(STANDARD_PATTERN)));
        javaTimeModule.addDeserializer(Date.class, new MultiDateDeserializer());

        //注册时间模块, 支持支持jsr310, 即新的时间类(java.time包下的时间类)
        objectMapper.registerModule(javaTimeModule);
    }


    public static String toJSONString(Object jsonString) {
        try {
            return objectMapper.writeValueAsString(jsonString);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static Object parse(String jsonString) {
        if (isJsonObj(jsonString)) {
            return parseObject(jsonString);
        }
        if (isJsonArray(jsonString)) {
            return parseArray(jsonString);
        }
        try {
            return objectMapper.readValue(jsonString, JsonNode.class);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static JSONObject parseObject(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, JSONObject.class);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static JSONArray parseArray(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, JSONArray.class);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }

    public static boolean isJsonObj(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        String trimedStr = str.trim();
        return trimedStr.startsWith("{") && trimedStr.endsWith("}");
    }

    public static boolean isJsonArray(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        String trimedStr = str.trim();
        return trimedStr.startsWith("[") && trimedStr.endsWith("]");
    }

    private JSON() {
        throw new UnsupportedOperationException("can not instance static object");
    }
}

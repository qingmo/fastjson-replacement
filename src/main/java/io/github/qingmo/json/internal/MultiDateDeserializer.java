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
package io.github.qingmo.json.internal;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.qingmo.json.exception.JSONException;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class MultiDateDeserializer extends StdDeserializer<Date> {
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "yyyyMMdd'T'HHmmss");

    public MultiDateDeserializer() {
        super(Date.class);
    }

    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p == null) {
            throw new JSONException(
                    "Unparseable date with empty JsonParser."
            );
        }
        ObjectCodec codec = p.getCodec();
        if (codec == null) {
            throw new JSONException(
                    "Unparseable date with empty ObjectCodec."
            );
        }
        JsonNode node = codec.readTree(p);
        if (node == null) {
            throw new JsonParseException(
                    p,
                    "Unparseable date with empty value."
            );
        }
        String dateStr = node.textValue();
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new JsonParseException(
                    p,
                    "Unparseable date with empty value."
            );
        }
        try {
            DateTime parseDateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(dateStr);
            return parseDateTime.toDate();
        } catch (Exception ignore) {

        }
        for (String dateFormat : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(dateFormat).parse(dateStr);
            } catch (ParseException ignore) {
                // ignore
            }
        }

        throw new JsonParseException(
                p,
                "Unparseable date: \"" + dateStr + "\". Supported formats: " + DATE_FORMATS
        );
    }
}


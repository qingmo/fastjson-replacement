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

import io.github.qingmo.json.exception.JSONException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TypeUtils {
    private TypeUtils() {
        throw new UnsupportedOperationException("can not instance static class");
    }

    private final static Pattern NUMBER_WITH_TRAILING_ZEROS_PATTERN = Pattern.compile("\\.0*$");

    public static String castToString(Object value) {
        return value == null ? null : value.toString();
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return byteValue((BigDecimal) value);
        }
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            } else {
                return Byte.valueOf(strVal);
            }
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? (byte) 1 : (byte) 0;
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            if (strVal.length() != 1) {
                throw new JSONException("can not cast to char, value : " + value);
            }
            return strVal.charAt(0);
        }
        throw new JSONException("can not cast to char, value : " + value);
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).shortValue();
        }
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            } else {
                return Short.parseShort(strVal);
            }
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? (short) 1 : (short) 0;
        }
        throw new JSONException("can not cast to short, value : " + value);
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            Float ret = (Float) value;
            if (Float.isNaN(ret) || Float.isInfinite(ret)) {
                return null;
            }
        } else if (value instanceof Double) {
            Double ret = (Double) value;
            if (Double.isNaN(ret) || Double.isInfinite(ret)) {
                return null;
            }
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        String strVal = value.toString();
        if (strVal.isEmpty()
                || "null".equalsIgnoreCase(strVal)
        ) {
            return null;
        }
        if (strVal.length() > 65535) {
            throw new JSONException("decimal overflow");
        }
        return new BigDecimal(strVal);
    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            Float floatValue = (Float) value;
            if (java.lang.Float.isNaN(floatValue) || java.lang.Float.isInfinite(floatValue)) {
                return null;
            } else {
                return BigInteger.valueOf(floatValue.longValue());
            }
        } else if (value instanceof Double) {
            Double doubleValue = (Double) value;
            if (java.lang.Double.isNaN(doubleValue) || java.lang.Double.isInfinite(doubleValue)) {
                return null;
            } else {
                return BigInteger.valueOf(doubleValue.longValue());
            }
        } else if (value instanceof BigInteger) {
            return (BigInteger) value;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toBigInteger();
        }
        String strVal = value.toString();
        if (strVal.isEmpty()
                || "null".equalsIgnoreCase(strVal)
        ) {
            return null;
        }
        if (strVal.length() > 65535) {
            throw new JSONException("decimal overflow");
        }
        return new BigInteger(strVal);
    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",", "");
            }
            return Float.valueOf(strVal);
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1f : 0f;
        }
        throw new JSONException("can not cast to float, value : " + value);
    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",", "");
            }
            return Double.valueOf(strVal);
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1.0 : 0.0;
        }
        throw new JSONException("can not cast to double, value : " + value);
    }

    public static Long castToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",", "");
            }
            try {
                return Long.valueOf(strVal);
            } catch (NumberFormatException ex) {
                throw ex;
            }
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? 1L : 0L;
        }
        throw new JSONException("can not cast to long, value : " + value);
    }

    public static Byte byteValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return decimal.setScale(0, RoundingMode.HALF_UP).byteValueExact();
    }

    public static Short shortValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return decimal.shortValueExact();
    }

    public static int intValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return decimal.intValueExact();
    }

    public static long longValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }
        return decimal.longValueExact();
    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            }
            if (strVal.indexOf(',') != -1) {
                strVal = strVal.replace(",", "");
            }
            Matcher matcher = NUMBER_WITH_TRAILING_ZEROS_PATTERN.matcher(strVal);
            if (matcher.find()) {
                strVal = matcher.replaceAll("");
            }
            return Integer.valueOf(strVal);
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }

        throw new JSONException("can not cast to int, value :" + value);
    }

    public static Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue() == 1;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)
            ) {
                return null;
            }
            if ("true".equalsIgnoreCase(strVal) //
                    || "1".equalsIgnoreCase(strVal)
            ) {
                return java.lang.Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal) //
                    || "0".equalsIgnoreCase(strVal)
            ) {
                return java.lang.Boolean.FALSE;
            }
            if ("Y".equalsIgnoreCase(strVal) //
                    || "T".equalsIgnoreCase(strVal)
            ) {
                return java.lang.Boolean.TRUE;
            }
            if ("F".equalsIgnoreCase(strVal) //
                    || "N".equalsIgnoreCase(strVal)
            ) {
                return java.lang.Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value :" + value);
    }
}

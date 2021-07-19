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

import io.github.qingmo.json.JSON.parseObject
import io.github.qingmo.json.JSON.toJSONString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.Serializable


class CommonTest {
    @Test
    fun testParseInnerArray() {
        val data = """{
  "key": "value"
}"""
        val json = parseObject(data)
        try {
            val shouldNotNull = json.getJSONArray("somekeynotexists")
            Assertions.assertNotNull(shouldNotNull)
            Assertions.assertEquals(0, shouldNotNull.size)
        } catch (e: Exception) {
            Assertions.fail()
        }
    }

    @Test
    fun testToJSONString() {
        class Haha : Serializable {
            var a: String? = null
            var b: String? = null
        }

        val haha = Haha()
        haha.a = "1"
        val data = toJSONString(haha)
        Assertions.assertEquals("{\"a\":\"1\"}", data)
    }
}
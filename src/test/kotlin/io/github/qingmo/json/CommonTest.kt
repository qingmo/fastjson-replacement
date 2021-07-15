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
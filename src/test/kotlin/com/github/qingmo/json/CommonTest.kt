package com.github.qingmo.json

import com.github.qingmo.json.JSON.parseObject
import com.github.qingmo.json.JSON.toJSONString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.Serializable
import java.lang.Exception


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
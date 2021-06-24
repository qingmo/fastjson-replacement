package com.github.qingmo.json

import java.util.Locale
import kotlin.test.fail

object TestUtils {

    fun verifyException(e: Throwable, vararg matches: String) {
        val msg = e.message?.lowercase(Locale.getDefault()) ?: ""
        val lmsg = msg.lowercase(Locale.getDefault())
        for (match in matches) {
            val lmatch = match.lowercase(Locale.getDefault())
            if (lmsg.indexOf(lmatch) >= 0) {
                return
            }
        }
        fail("Expected an exception with one of substrings [${JSON.toJSONString(matches)}]: got one with message ${msg}")
    }
}
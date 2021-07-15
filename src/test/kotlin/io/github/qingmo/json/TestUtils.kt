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
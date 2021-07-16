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
package io.github.qingmo.json.exception

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class JSONExceptionTest {

    @Test
    fun `add converage of test code`() {
        val first = JSONException("haha")
        assertEquals("haha", first.message)
        assertNull(first.cause)
        val second = JSONException("hoho", IllegalArgumentException("gaga"))
        assertEquals("hoho", second.message)
        assertNotNull(second.cause)
        assertEquals("gaga", second.cause?.message)
    }
}
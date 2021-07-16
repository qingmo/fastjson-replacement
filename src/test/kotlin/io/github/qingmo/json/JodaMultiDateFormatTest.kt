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

import org.joda.time.format.ISODateTimeFormat
import org.junit.jupiter.api.Test
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class JodaMultiDateFormatTest {

    @Test
    fun `test with ISO8601`() {

        val rest = ISODateTimeFormat.dateTimeParser().parseDateTime("2016-11-01")
        assertNotNull(rest)
        val calendar = Calendar.getInstance()
        calendar.time = rest.toDate()
        assertEquals(2016, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH))
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY))
        val rest2 = ISODateTimeFormat.dateTimeParser().parseDateTime("2000-01-01T01:01:01")
        assertNotNull(rest2)
        calendar.time = rest2.toDate()
        assertEquals(2000, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH))
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(1, calendar.get(Calendar.HOUR_OF_DAY))
        val rest3 = ISODateTimeFormat.dateTimeParser().parseDateTime("2004-05-03T17:30:08+08:00")
        assertNotNull(rest3)
        calendar.time = rest3.toDate()
        assertEquals(2004, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.MAY, calendar.get(Calendar.MONTH))
        assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH))

        assertFailsWith(
            exceptionClass = IllegalArgumentException::class,
            block = {
                ISODateTimeFormat.dateTimeParser().parseDateTime("abcd")
            }
        )
    }
}
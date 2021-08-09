package io.github.qingmo.json;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JavaJSONTest {

    @Test
    public void testRemove() {
        JSONArray jsonArray = new JSONArray(Arrays.asList("a", "b", "c"));
        assertEquals("a", jsonArray.remove(0));
        assertEquals("b", jsonArray.get(0));
    }

    @Test
    public void testToArray() {
        JSONArray jsonArray = new JSONArray(Arrays.asList("a", "b", "c"));
        String[] result = jsonArray.toArray(new String[]{});
        assertEquals(3, result.length);
        assertEquals("a", result[0]);
    }

//    @Test
//    public void testLocalTime() {
//        String data = "{\"haha\":\"12:11:01\"}";
//        LocalTimeTest ret = JSON.parseObject(data, LocalTimeTest.class);
//        assertNotNull(ret);
//        assertEquals(12, ret.getHaha().getHour());
//        assertEquals(11, ret.getHaha().getMinute());
//        assertEquals(1, ret.getHaha().getSecond());
//        LocalTimeTest test = new LocalTimeTest(
//                LocalTime.of(ret.getHaha().getHour(), ret.getHaha().getMinute(), 2, 0));
//        String teststr = JSON.toJSONString(test);
//        assertEquals("{\"haha\":\"12:11:02\"}", teststr);
//
//    }
}

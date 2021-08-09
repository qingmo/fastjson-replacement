package io.github.qingmo.json;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}

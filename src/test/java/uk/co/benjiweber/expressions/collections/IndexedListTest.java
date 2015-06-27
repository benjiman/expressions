package uk.co.benjiweber.expressions.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static uk.co.benjiweber.expressions.collections.EnhancedList.enhancedList;

public class IndexedListTest {
    @Test
    public void example_mapping_with_index() {
        List<String> result = enhancedList("foo", "bar")
                .mapWithIndex((it, i) -> it + "_" + i);

        assertEquals(asList("foo_0","bar_1"), result);
    }

    @Test
    public void example_consuming_with_index() {
        List<String> result = new ArrayList<>();
        enhancedList("foo", "bar")
            .withIndex((it, i) -> result.add(it + "_" + i));

        assertEquals(asList("foo_0","bar_1"), result);
    }
}
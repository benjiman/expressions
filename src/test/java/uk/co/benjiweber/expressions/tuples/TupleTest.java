package uk.co.benjiweber.expressions.tuples;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static uk.co.benjiweber.expressions.tuples.Tuple.tuple;

public class TupleTest {

    @Test public void return_and_expand_tuples() {
        String name = me()
            .map((firstname, favouriteNo, surname) -> tuple(firstname, surname))
            .map((firstname, surname) -> firstname + " " + surname);

        assertEquals("benji weber", name);
    }

    static TriTuple<String, Integer, String> me() {
        return tuple("benji",9001,"weber");
    }

    @Test public void tuple_equality() {
        assertEquals(tuple("hello"), tuple("hello"));
        assertNotEquals(tuple("hello"), tuple("hello2"));

        assertEquals(tuple("hello","world"), tuple("hello", "world"));
        assertNotEquals(tuple("hello", "world"), tuple("hello", "world2"));

        assertEquals(tuple("hello","world",5), tuple("hello", "world",5));
        assertNotEquals(tuple("hello", "world",5), tuple("hello", "world2",5));
    }

    @Test public void tuple_toString() {
        assertEquals("{hello}",tuple("hello").toString());
        assertEquals("{hello, world}",tuple("hello","world").toString());
        assertEquals("{hello, world, 9}",tuple("hello","world",9).toString());
    }

    @Test(expected = NumberTooBigException.class)
    public void return_and_throw_checked_exception() throws NumberTooBigException {
        String name = me()
            .map((firstname, favouriteNo, surname) -> {
                if (favouriteNo > 9000) throw new NumberTooBigException();
                return firstname;
            });
    }

    static class NumberTooBigException extends Exception {}
}

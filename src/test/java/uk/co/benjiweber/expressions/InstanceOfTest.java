package uk.co.benjiweber.expressions;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static uk.co.benjiweber.expressions.InstanceOf.when;
import static org.junit.Assert.assertEquals;

public class InstanceOfTest {
    @Test
    public void should_return_value_when_input_object_is_instance() {
        Object foo = "ffoo";

        String result = when(foo).instanceOf(String.class)
                .then(s -> s.substring(1))
                .otherwise("incorrect");

        assertEquals("foo", result);
    }

    @Test
    public void should_return_alternative_when_input_object_is_not_instance() {
        Object foo = 5;

        String result = when(foo).instanceOf(String.class)
                .then(s -> s.substring(1))
                .otherwise("expected");

        assertEquals("expected", result);
    }

    @Test
    public void should_return_optional_value_when_input_object_is_instance() {
        Object foo = "ffoo";

        Optional<String> result = when(foo).instanceOf(String.class)
                .then(s -> s.substring(1))
                .optional();

        assertEquals("foo", result.get());
    }

    @Test
    public void should_return_empty_optional_when_input_object_is_not_instance() {
        Object foo = 5;

        Optional<String> result = when(foo).instanceOf(String.class)
                .then(s -> s.substring(1))
                .optional();

        assertFalse(result.isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void should_throw_exceptions() {
        Object foo = "hello";

        Optional<String> result = when(foo).instanceOf(String.class)
                .then(s -> {
                    if (true) throw new NullPointerException();
                    return "nope";
                })
                .optional();
    }

    @Test
    public void should_not_throw_exception_if_block_does_not_need_evaluating() {
        Object foo = 5;

        Optional<String> result = when(foo).instanceOf(String.class)
                .then(s -> {
                    if(true) throw new NullPointerException();
                    return "nope";
                })
                .optional();

        assertFalse(result.isPresent());
    }

    @Test(expected = RuntimeException.class)
    public void should_wrap_checked_exceptions() {
        Object foo = "hello";

        Optional<String> result = when(foo).instanceOf(String.class)
                .then(s -> {
                    if(true) throw new Exception();
                    return "nope";
                })
                .optional();
    }

    static class Duck {
        boolean quacked = false;
        Void quack() {
            quacked = true;
            return null;
        }
    }

    @Test
    public void should_return_value_when_input_object_is_instance_duck() {

        Object foo = new Duck();

        when(foo).instanceOf(Duck.class)
                .then(duck -> duck.quack())
                .otherwise(null);

        assertTrue(((Duck)foo).quacked);

    }

}

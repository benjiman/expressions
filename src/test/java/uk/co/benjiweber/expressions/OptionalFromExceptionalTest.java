package uk.co.benjiweber.expressions;

import org.junit.Test;
import uk.co.benjiweber.expressions.exceptions.Exceptions;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OptionalFromExceptionalTest {

    @Test
    public void should_be_empty_when_exception_thrown() {
        Optional<String> foo = Exceptions.toOptional(this::foo).get();
        assertFalse(foo.isPresent());
    }

    @Test
    public void should_have_correct_value_when_value_returned() {
        Optional<String> bar = Exceptions.toOptional(this::bar).get();
        assertEquals("bar", bar.get());
    }

    @Test
    public void should_have_correct_value_when_value_returned_function() {
        Optional<String> foo = Exceptions.toOptional(this::oneParam).apply(false);
        assertEquals("foo", foo.get());
    }

    @Test
    public void should_have_correct_value_when_function_throws() {
        Optional<String> foo = Exceptions.toOptional(this::oneParam).apply(true);
        assertFalse(foo.isPresent());
    }

    private String foo() throws FooNotFoundException {
        throw new FooNotFoundException();
    }

    private String bar() throws FooNotFoundException {
        return "bar";
    }

    private String oneParam(boolean throwRequested) throws FooNotFoundException {
        return throwRequested ? thenThrow(new FooNotFoundException(),"") : "foo";
    }

    static class FooNotFoundException extends Exception {}

    static <T, E extends Exception> T thenThrow(E e, T t) throws E {
        throw e;
    }
}

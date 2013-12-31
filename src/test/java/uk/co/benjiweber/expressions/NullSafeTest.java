package uk.co.benjiweber.expressions;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static uk.co.benjiweber.expressions.NullSafe.nullSafe;

public class NullSafeTest {

    Foo foo = new Foo();

    @Test public void should_return_value_of_expression_passed_in() {
        Optional<String> result = nullSafe(() -> foo.foo().bar().baz());
        assertEquals("baz", result.get());
    }

    @Test public void should_return_optional_empty_when_expression_evaluates_to_null() {
        Optional<String> result = nullSafe(() -> foo.foo().bar().aNull());
        assertFalse(result.isPresent());
    }

    @Test public void should_return_optional_empty_when_null_pointer_exception() {
        Optional<String> result = nullSafe(() -> foo.foo().npe().baz());
        assertFalse(result.isPresent());
    }

    static class Foo {
        Bar foo() {
            return new Bar();
        }
    }

    static class Bar {
        Baz bar() {
            return new Baz();
        }
        Baz npe() {
            return null;
        }
    }

    static class Baz {
        String baz() {
            return "baz";
        }
        String aNull() {
            return null;
        }
    }
}

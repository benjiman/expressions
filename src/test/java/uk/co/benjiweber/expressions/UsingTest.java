package uk.co.benjiweber.expressions;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static uk.co.benjiweber.expressions.Using.using;

public class UsingTest {

    static class Foo implements AutoCloseable {

        public String result() {
            return "result";
        }

        boolean closed = false;
        public void close() throws Exception {
            closed = true;
        }
    }

    @Test public void should_return_value() {
        String result = using(Foo::new, foo -> {
            return foo.result();
        });
    }

    @Test public void should_close_closeable() {
        Foo closeable = using(Foo::new, foo -> {
            return foo;
        });

        Assert.assertTrue(closeable.closed);
    }

    Foo foo = new Foo();
    Foo getClosable() {
        return foo;
    }

    @Test public void should_close_closeable_when_exception() {
        try {
            using(this::getClosable, foo -> {
                if (true) throw new NullPointerException();
                return "";
            });
        } catch (NullPointerException e) {
            // Expected
        }

        Assert.assertTrue(foo.closed);
    }

}

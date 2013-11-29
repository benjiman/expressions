package uk.co.benjiweber.expressions;

import org.junit.Test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.Try.Try;

public class TryAsExpressionTest {

    static class SuperException extends Exception {}
    static class SubException extends SuperException {}

    @Test public void should_return_try_value() {
        String result = Try(() -> {
            return "try";
        }).Catch(NullPointerException.class, e -> {
            return "catch";
        }).apply();

        assertEquals("try", result);
    }

    @Test public void should_return_try_value_when_number() {
        Number result = Try(() -> {
            return 1;
        }).Catch(NullPointerException.class, e -> {
            return 2;
        }).apply();

        assertEquals(1, result);
    }


    @Test public void should_return_catch_value() {
        String result = Try(() -> {
            if (true) throw new NullPointerException();
            return "try";
        }).Catch(NullPointerException.class, e -> {
            return "catch";
        }).apply();

        assertEquals("catch", result);
    }


    @Test public void should_catch_subclass() throws SubException {
        String result = Try(() -> {
            if (true) throw new SubException();
            return "try";
        }).Catch(SuperException.class, e -> {
            return "catch";
        }).apply();

        assertEquals("catch", result);
    }

    @Test public void should_catch_in_order_specified() throws SubException {
        String result = Try(() -> {
            if (true) throw new SubException();
            return "try";
        }).Catch(SubException.class, e -> {
            return "firstcatch";
        }).Catch(SuperException.class, e -> {
            return "secondcatch";
        }).apply();

        assertEquals("firstcatch", result);
    }

    @Test public void should_wrap_but_not_catch_uncaught_checked_exception() {
        try {
            Try(() -> {
                if (true) throw new SubException();
                return "try";
            }).apply();
            fail("Should have thrown");
        } catch (RuntimeException e) {
            assertEquals(SubException.class, e.getCause().getClass());
        }
    }

    @Test public void should_not_wrap_or_catch_uncaught_runtime_exception() {
        try {
            Try(() -> {
                if (true) throw new NullPointerException();
                return "try";
            }).Catch(SubException.class, e -> {
                return "firstcatch";
            }).Catch(SuperException.class, e -> {
                return "secondcatch";
            }).apply();

            fail("should have thrown npe");
        } catch (NullPointerException e) {

        }
    }

    @Test public void should_not_wrap_or_catch_uncaught_error() {
        try {
            Try(() -> {
                if (true) throw new Error();
                return "try";
            }).Catch(SubException.class, e -> {
                return "firstcatch";
            }).Catch(SuperException.class, e -> {
                return "secondcatch";
            }).apply();

            fail("should have thrown error");
        } catch (Error e) {

        }
    }

    @Test public void should_execute_finally_when_returning_normally() {
        boolean[] finallyCalled = new boolean[1];
        String result = Try(() -> {
            return "try";
        }).Catch(SuperException.class, e -> {
            return "catch";
        }).Finally(() -> {
            finallyCalled[0] = true;
        }).apply();

        assertTrue(finallyCalled[0]);
    }

    @Test public void should_execute_finally_when_catching_exception()  throws SuperException {
        boolean[] finallyCalled = new boolean[1];
        String result = Try(() -> {
            if(true) throw new SuperException();
            return "try";
        }).Catch(SuperException.class, e -> {
            return "catch";
        }).Finally(() -> {
            finallyCalled[0] = true;
        }).apply();

        assertTrue(finallyCalled[0]);
    }

    @Test public void should_execute_finally_when_uncaught_exception() {
        boolean[] finallyCalled = new boolean[1];
        try {
            String result = Try(() -> {
                if(true) throw new NullPointerException();
                return "try";
            }).Catch(SuperException.class, e -> {
                return "catch";
            }).Finally(() -> {
                finallyCalled[0] = true;
            }).apply();

            fail();
        } catch (RuntimeException e) {

        }
        assertTrue(finallyCalled[0]);
    }

    @Test public void should_execute_finally_when_uncaught_error() {
        boolean[] finallyCalled = new boolean[1];
        try {
            String result = Try(() -> {
                if(true) throw new Error();
                return "try";
            }).Catch(SuperException.class, e -> {
                return "catch";
            }).Finally(() -> {
                finallyCalled[0] = true;
            }).apply();

            fail();
        } catch (Error e) {

        }
        assertTrue(finallyCalled[0]);
    }


    @Test(expected=NullPointerException.class) public void should_propagate_exception_in_finally() {
        String result = Try(() -> {
            if(true) throw new SuperException();
            return "try";
        }).Catch(SuperException.class, e -> {
            return "catch";
        }).Finally(() -> {
            throw new NullPointerException();
        }).apply();
    }

    @Test(expected=Error.class) public void should_propagate_error_in_finally() {
        String result = Try(() -> {
            if(true) throw new SuperException();
            return "try";
        }).Catch(SuperException.class, e -> {
            return "catch";
        }).Finally(() -> {
            throw new Error();
        }).apply();
    }

    @Test(expected=RuntimeException.class) public void should_wrap_and_propagate_checked_exception_in_finally() {
        String result = Try(() -> {
            if(true) throw new SuperException();
            return "try";
        }).Catch(SuperException.class, e -> {
            return "catch";
        }).Finally(() -> {
            throw new Exception();
        }).apply();
    }
}

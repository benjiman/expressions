package uk.co.benjiweber.expressions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.Exceptions.unchecked;

public class ExceptionsTest {
    @Test
    public void checked_to_unchecked() {
        String foo = unchecked(() -> Example.methodThatThrowsCheckedException(DO_NOT_THROW));
        assertEquals("foo", foo);
    }

    @Test
    public void checked_to_unchecked_method_reference() {
        unchecked(Example::methodThatThrowsCheckedExceptionNoParams);
    }

    @Test
    public void checked_to_unchecked_void() {
        unchecked(() -> Example.voidMethodThatThrowsACheckedException(DO_NOT_THROW));
    }

    @Test(expected = RuntimeException.class)
    public void checked_to_unchecked_should_wrap_in_runtime_exception() {
        String foo = unchecked(() -> Example.methodThatThrowsCheckedException(THROW));
    }

    @Test(expected = RuntimeException.class)
    public void checked_to_unchecked_void_should_wrap_in_runtime_exception() {
        unchecked(() -> Example.voidMethodThatThrowsACheckedException(THROW));
    }

    static class ACheckedExceptionIDontHaveAGoodWayToDealWith extends Exception {

    }

    static boolean THROW = true;
    static boolean DO_NOT_THROW = false;


    static class Example {
        public static String methodThatThrowsCheckedException(boolean throwPlease) throws ACheckedExceptionIDontHaveAGoodWayToDealWith {
            if (throwPlease) throw new ACheckedExceptionIDontHaveAGoodWayToDealWith();
            return "foo";
        }

        public static void voidMethodThatThrowsACheckedException(boolean throwPlease) throws ACheckedExceptionIDontHaveAGoodWayToDealWith {
            if (throwPlease) throw new ACheckedExceptionIDontHaveAGoodWayToDealWith();
        }

        public static void methodThatThrowsCheckedExceptionNoParams() throws ACheckedExceptionIDontHaveAGoodWayToDealWith {

        }
    }


}

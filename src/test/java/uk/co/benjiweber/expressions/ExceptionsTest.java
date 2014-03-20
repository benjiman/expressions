package uk.co.benjiweber.expressions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.Exceptions.unchecked;
import static uk.co.benjiweber.expressions.Exceptions.wrappingAll;
import static uk.co.benjiweber.expressions.Exceptions.wrappingChecked;

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

    @Test(expected = Wrapped.class)
    public void wrapping_checked() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsCheckedException(THROW)).in(Wrapped::new);
    }

    @Test(expected = NullPointerException.class)
    public void wrapping_checked_should_not_wrap_npe() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsNPE(THROW)).in(Wrapped::new);
    }


    @Test
    public void wrapping_checked_should_return_value_when_no_exception() throws Wrapped {
        String foo = wrappingChecked(() -> Example.methodThatThrowsCheckedException(DO_NOT_THROW)).in(Wrapped::new);
        assertEquals("foo", foo);
    }

    @Test
    public void wrapping_all_should_return_value_when_no_exception() throws Wrapped {
        String foo = wrappingAll(() -> Example.methodThatThrowsCheckedException(DO_NOT_THROW)).in(Wrapped::new);
        assertEquals("foo", foo);
    }

    @Test(expected = RuntimeException.class)
    public void wrapping_checked_should_not_wrap_runtime_exception() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsRuntimeException(THROW)).in(Wrapped::new);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_should_wrap_npe() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsNPE(THROW)).in(Wrapped::new);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_should_wrap_runtime_exception() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsRuntimeException(THROW)).in(Wrapped::new);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_checked_using_supplier() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsCheckedException(THROW)).in(() -> new Wrapped(null));
    }

    @Test(expected = Wrapped.class)
    public void wrapping_checked_using_reflection() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsCheckedException(THROW)).in(Wrapped.class);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_using_supplier() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsCheckedException(THROW)).in(() -> new Wrapped(null));
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_using_reflection() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsCheckedException(THROW)).in(Wrapped.class);
    }

    static class ACheckedExceptionIDontHaveAGoodWayToDealWith extends Exception {

    }

    static class Wrapped extends Exception {
        public Wrapped(Exception e) {
            super(e);
        }
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

        public static String methodThatThrowsNPE(boolean throwPlease) {
            if(throwPlease) throw new NullPointerException();
            return "not_an_npe";
        }

        public static String methodThatThrowsRuntimeException(boolean throwPlease) {
            if(throwPlease) throw new NullPointerException();
            return "not_an_exception";
        }
    }


}

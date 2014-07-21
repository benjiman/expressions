package uk.co.benjiweber.expressions.exceptions;

import org.junit.Test;
import uk.co.benjiweber.expressions.exceptions.Result;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.unchecked;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.wrappingAll;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.wrappingChecked;

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

    @Test
    public void streams_and_exceptions() {
        List<String> result =
            asList("foo", "bar", "baz", "boooo")
                .stream()
                .map(Result.wrapReturn(Example::duplicatesShortStrings))
                .map(Result.wrap(s -> s.toUpperCase()))
                .filter(Result::success)
                .map(Result::unwrap)
                .collect(Collectors.toList());

        assertEquals(asList("FOOFOO","BARBAR","BAZBAZ"), result);
    }

    @Test
    public void streams_and_exceptions_exceptions_mid_stream() {
        List<String> result =
            asList("foo", "bar", "baz", "UPR", "boooo")
                .stream()
                .map(Result.wrapReturn(Example::duplicatesShortStrings))
                .map(Result.wrapExceptional(Example::uppercasesStrings))
                .filter(Result::success)
                .map(Result::unwrap)
                .collect(Collectors.toList());

        assertEquals(asList("FOOFOO","BARBAR","BAZBAZ"), result);
    }

    @Test
    public void streams_and_exceptions_exceptions_map_failure_cases() {
        List<String> result =
            asList("foo", "bar", "baz", "UPR", "boooo")
                .stream()
                .map(Result.wrapReturn(Example::duplicatesShortStrings))
                .map(Result.wrapExceptional(Example::uppercasesStrings))
                .map(Result.onSuccess(Function.<String>identity()).on(InputTooLongException.class, s -> "OhNoes").mapper())
                .filter(Result::success)
                .map(Result::unwrap)
                .collect(Collectors.toList());

        assertEquals(asList("FOOFOO","BARBAR","BAZBAZ", "OhNoes"), result);
    }

    @Test
    public void streams_and_exceptions_exceptions_throw_unfiltered_failures() {
        try {
            asList("foo", "bar", "baz", "UPR", "boooo")
                .stream()
                .map(Result.wrapReturn(Example::duplicatesShortStrings))
                .map(Result.wrapExceptional(Example::uppercasesStrings))
                .map(Result::unwrap)
                .collect(Collectors.toList());
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof InputContainsUppercaseException);
        }
    }

    @Test
    public void completable_future_supplyaync_exceptional() {
        CompletableFuture
                .supplyAsync(Result.wrapReturn(Example::throwingSupplier))
                .thenApply(Result.wrap(String::toUpperCase))
                .thenAccept(Result.wrapConsumer(System.out::println));


        CompletableFuture
                .supplyAsync(Result.wrapReturn(Example::notThrowingSupplier))
                .thenApply(Result.wrap(String::toUpperCase))
                .thenAccept(Result.wrapConsumer(System.out::println));
    }


    static class ACheckedExceptionIDontHaveAGoodWayToDealWith extends Exception {

    }

    static class Wrapped extends Exception {
        public Wrapped(Exception e) {
            super(e);
        }
    }

    static class InputTooLongException extends Exception {

    }

    static class InputContainsUppercaseException extends Exception {

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

        public static String throwingSupplier() throws InputTooLongException {
            throw new InputTooLongException();
        }

        public static String notThrowingSupplier() throws InputTooLongException {
            return "hello";
        }

        public static String methodThatThrowsRuntimeException(boolean throwPlease) {
            if(throwPlease) throw new NullPointerException();
            return "not_an_exception";
        }

        public static String duplicatesShortStrings(String input) throws InputTooLongException {
            if (input.length() > 3) throw new InputTooLongException();
            return input + input;
        }

        public static String uppercasesStrings(String input) throws InputContainsUppercaseException {
            if (input.matches(".*?[A-Z].*")) throw new InputContainsUppercaseException();
            return input.toUpperCase();
        }
    }


}

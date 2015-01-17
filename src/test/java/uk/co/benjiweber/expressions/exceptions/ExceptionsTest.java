package uk.co.benjiweber.expressions.exceptions;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.unchecked;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.wrappingAll;
import static uk.co.benjiweber.expressions.exceptions.Exceptions.wrappingChecked;
import static uk.co.benjiweber.expressions.exceptions.Example.Do;

public class ExceptionsTest {
    @Test
    public void checked_to_unchecked() {
        String foo = unchecked(() -> Example.methodThatThrowsCheckedException(Do.NOT_THROW));
        assertEquals("foo", foo);
    }

    @Test
    public void checked_to_unchecked_method_reference() {
        unchecked(Example::methodThatThrowsCheckedExceptionNoParams);
    }

    @Test
    public void checked_to_unchecked_void() {
        unchecked(() -> Example.voidMethodThatThrowsACheckedException(Example.Do.NOT_THROW));
    }

    @Test(expected = RuntimeException.class)
    public void checked_to_unchecked_should_wrap_in_runtime_exception() {
        String foo = unchecked(() -> Example.methodThatThrowsCheckedException(Do.THROW));
    }

    @Test(expected = RuntimeException.class)
    public void checked_to_unchecked_void_should_wrap_in_runtime_exception() {
        unchecked(() -> Example.voidMethodThatThrowsACheckedException(Do.THROW));
    }

    @Test(expected = Wrapped.class)
    public void wrapping_checked() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsCheckedException(Do.THROW)).in(Wrapped::new);
    }

    @Test(expected = NullPointerException.class)
    public void wrapping_checked_should_not_wrap_npe() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsNPE(Do.THROW)).in(Wrapped::new);
    }


    @Test
    public void wrapping_checked_should_return_value_when_no_exception() throws Wrapped {
        String foo = wrappingChecked(() -> Example.methodThatThrowsCheckedException(Example.Do.NOT_THROW)).in(Wrapped::new);
        assertEquals("foo", foo);
    }

    @Test
    public void wrapping_all_should_return_value_when_no_exception() throws Wrapped {
        String foo = wrappingAll(() -> Example.methodThatThrowsCheckedException(Do.NOT_THROW)).in(Wrapped::new);
        assertEquals("foo", foo);
    }

    @Test(expected = RuntimeException.class)
    public void wrapping_checked_should_not_wrap_runtime_exception() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsRuntimeException(Do.THROW)).in(Wrapped::new);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_should_wrap_npe() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsNPE(Do.THROW)).in(Wrapped::new);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_should_wrap_runtime_exception() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsRuntimeException(Do.THROW)).in(Wrapped::new);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_checked_using_supplier() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsCheckedException(Do.THROW)).in(() -> new Wrapped(null));
    }

    @Test(expected = Wrapped.class)
    public void wrapping_checked_using_reflection() throws Wrapped {
        wrappingChecked(() -> Example.methodThatThrowsCheckedException(Do.THROW)).in(Wrapped.class);
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_using_supplier() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsCheckedException(Do.THROW)).in(() -> new Wrapped(null));
    }

    @Test(expected = Wrapped.class)
    public void wrapping_all_using_reflection() throws Wrapped {
        wrappingAll(() -> Example.methodThatThrowsCheckedException(Do.THROW)).in(Wrapped.class);
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
                .collect(toList());

        assertEquals(asList("FOOFOO", "BARBAR", "BAZBAZ"), result);
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
                .collect(toList());

        assertEquals(asList("FOOFOO", "BARBAR", "BAZBAZ"), result);
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
                .collect(toList());

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
                .collect(toList());
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

    @Test
    public void exceptional_stream_flatmap() {
        Book book = () -> "book";

        List<String> books = asList(book).stream()
                .flatMap(Exceptions.stream(Book::name))
                .collect(toList());

        assertEquals(asList("book"), books);
    }


    public static class ACheckedExceptionIDontHaveAGoodWayToDealWith extends Exception {

    }

    public static class Wrapped extends Exception {
        public Wrapped(Exception e) {
            super(e);
        }
    }

    public static class InputTooLongException extends Exception {

    }

    public static class InputContainsUppercaseException extends Exception {

    }

    public interface Book {
        String name() throws NoNameException;
    }

    public static class NoNameException extends Exception {}


}

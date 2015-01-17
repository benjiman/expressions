package uk.co.benjiweber.expressions.functions;

import org.junit.Test;
import uk.co.benjiweber.expressions.exceptions.Example;
import uk.co.benjiweber.expressions.exceptions.Result;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.exceptions.Example.Do;
import static uk.co.benjiweber.expressions.functions.ExceptionalFunction.exceptional;

public class ExceptionalFunctionTest {
    @Test
    public void unchecked_returns_result() {
        String foo = exceptional(Example::methodThatThrowsCheckedException).unchecked().apply(Do.NOT_THROW);
        assertEquals("foo", foo);
    }

    @Test(expected = RuntimeException.class)
    public void unchecked_throws() {
        exceptional(Example::methodThatThrowsCheckedException).unchecked().apply(Do.THROW);
    }

    @Test
    public void wrap_in_stream() {
        Function<Do, Stream<String>> f = exceptional(Example::methodThatThrowsCheckedException).stream();

        assertEquals(Optional.empty(), f.apply(Do.THROW).findAny());
        assertEquals(Optional.of("foo"), f.apply(Do.NOT_THROW).findAny());
    }

    @Test
    public void wrap_in_optional() {
        Function<Do, Optional<String>> f = exceptional(Example::methodThatThrowsCheckedException).optional();

        assertEquals(Optional.empty(), f.apply(Do.THROW));
        assertEquals(Optional.of("foo"), f.apply(Do.NOT_THROW));
    }


    interface Container<T> {
        T value();
        static <T> Container<T> empty() { return () -> null; }
        static <T> Container<T> of(T value) { return () -> value; }
    }

    @Test
    public void wrap_in_custom_container() {
        Function<Do, Container<String>> f = exceptional(Example::methodThatThrowsCheckedException).wrapReturn(Container::of).wrapException(Container::empty);
        assertEquals("foo", f.apply(Do.NOT_THROW).value());
        assertEquals(null, f.apply(Do.THROW).value());
    }

    @Test
    public void wrapping_in_results() {
        List<String> result =
            asList("foo", "bar", "baz", "UPR", "boooo")
                .stream()
                .map(exceptional(Example::duplicatesShortStrings).resultOut())
                .map(exceptional(Example::uppercasesStrings).resultInOut())
                .filter(Result::success)
                .map(Result::unwrap)
                .collect(toList());

        assertEquals(asList("FOOFOO", "BARBAR", "BAZBAZ"), result);
    }

    @Test
    public void flatmapping_with_stream() {
        List<String> result =
                asList("foo", "bar", "baz", "UPR", "boooo")
                    .stream()
                    .flatMap(exceptional(Example::duplicatesShortStrings).stream())
                    .flatMap(exceptional(Example::uppercasesStrings).stream())
                    .collect(toList());

        assertEquals(asList("FOOFOO", "BARBAR", "BAZBAZ"), result);
    }

    @Test
    public void logging_discarded() {
        List<String> result =
            asList("foo", "bar", "baz", "UPR", "boooo")
                .stream()
                .flatMap(exceptional(Example::duplicatesShortStrings).wrapReturn(Stream::of).peek(System.out::println).orElse(Stream::empty))
                .flatMap(exceptional(Example::uppercasesStrings).wrapReturn(Stream::of).peek(System.out::println).orElse(Stream::empty))
                .collect(toList());

        assertEquals(asList("FOOFOO", "BARBAR", "BAZBAZ"), result);
    }



}
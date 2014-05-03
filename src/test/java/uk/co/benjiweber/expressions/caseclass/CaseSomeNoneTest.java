package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.caseclass.Case;
import uk.co.benjiweber.expressions.caseclass.Case2;
import uk.co.benjiweber.expressions.caseclass.Case3;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.caseclass.CaseSomeNoneTest.None.none;
import static uk.co.benjiweber.expressions.caseclass.CaseSomeNoneTest.Some.some;
import static uk.co.benjiweber.expressions.caseclass.Case3.erasesTo;

public class CaseSomeNoneTest {
    interface Option<T> extends Case2<Some<T>, None<T>>{ }
    interface Some<T> extends Option<T> {
        T value();
        static <T> Some<T> some(T value) {
            return () -> value;
        }
    }
    interface None<T> extends Option<T> {
        static final None none = new None(){} ;
        static <T> None<T> none() { return (None<T>)none;}
    }

    @Test
    public void some_none_match_example() {
        Option<String> exists = some("hello");
        Option<String> missing = none();

        assertEquals("hello", describe(exists));

        assertEquals("missing", describe(missing));
    }

    private String describe(Option<String> option) {
        return option.match()
            .when(erasesTo(Some.class), some -> some.value())
            .when(erasesTo(None.class), none -> "missing");
    }
}

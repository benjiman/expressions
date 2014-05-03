package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.EqualsHashcode;
import uk.co.benjiweber.expressions.ToString;
import uk.co.benjiweber.expressions.caseclass.Case2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.caseclass.CaseListTest.EmptyList.empty;
import static uk.co.benjiweber.expressions.caseclass.CaseListTest.Tail.list;
import static uk.co.benjiweber.expressions.caseclass.Case3.erasesTo;

public class CaseListTest {

    @Test
    public void list_size_example() {
        List<String> list = list("one", list("two", list("three", empty())));

        assertEquals(3, size(list));
    }

    private int size(List<String> list) {
        return list.match()
                .when(erasesTo(Tail.class), n -> 1 + size(n.tail()))
                .when(erasesTo(EmptyList.class), n -> 0);
    }

    @Test
    public void list_double_item_example() {
        List<String> list = list("one", list("two", list("three", empty())));
        List<String> expected = list("oneone", list("twotwo", list("threethree", empty())));

        assertEquals(expected, longer(list));
    }

    private List<String> longer(List<String> list) {
        return list.match()
                .when(erasesTo(Tail.class), n -> list(n.head() + n.head(), longer(n.tail())))
                .when(erasesTo(EmptyList.class), n -> empty());
    }


    interface List<T> extends Case2<Tail<T>,EmptyList<T>> {}

    interface Tail<T> extends List<T>, EqualsHashcode<Tail<T>>, ToString<Tail<T>> {
        T head();
        List<T> tail();
        static <T> List<T> list(T head, List<T> tail) {
            return new Tail<T>() {
                public T head() { return head;}
                public List<T> tail() { return tail; }
                @Override public boolean equals(Object o) { return autoEquals(o); }
                @Override public int hashCode() { return autoHashCode(); }
                @Override public String toString() { return autoToString(); }
            };
        }

        default java.util.List<Function<Tail<T>, ?>> props() {
            return Arrays.asList(Tail::head, Tail::tail);
        }
    }
    interface EmptyList<T> extends List<T> {
        static EmptyList empty = new EmptyList(){
            @Override public boolean equals(Object other) { return other == this; }
            @Override public String toString() { return ""; }
        };
        static <T> List<T> empty() { return (EmptyList<T>)empty;};
    }

}

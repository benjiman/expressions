package uk.co.benjiweber.expressions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Arrays.toString;

interface Paint extends EqualsHashcode<Paint>, ToString<Paint> {
    int red();
    int green();
    int blue();
    default Paint mix(Paint other) {
        return create(red() + other.red(), green() + other.green(), blue() + other.blue());
    }

    static Paint create(int red, int green, int blue) {
        return new Paint() {
            public int red() { return red; }
            public int green() { return green; }
            public int blue() { return blue; }
            @Override public boolean equals(Object o) { return autoEquals(o); }
            @Override public int hashCode() { return autoHashCode(); }
            @Override public String toString() { return autoToString(); }
        };
    }

    default List<Function<Paint,?>> props() {
        return asList(Paint::red, Paint::green, Paint::blue);
    }

}
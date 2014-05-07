package uk.co.benjiweber.expressions;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Arrays.toString;

interface Paint {
    int red();
    int green();
    int blue();
    default Paint mix(Paint other) {
        return create(red() + other.red(), green() + other.green(), blue() + other.blue());
    }

    static Paint create(int red, int green, int blue) {
        abstract class PaintValue extends Value<Paint> implements Paint {}
        return new PaintValue() {
            public int red() { return red; }
            public int green() { return green; }
            public int blue() { return blue; }
        }.using(Paint::red, Paint::green, Paint::blue);
    }

    default List<Function<Paint,?>> props() {
        return asList(Paint::red, Paint::green, Paint::blue);
    }

}
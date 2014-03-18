package uk.co.benjiweber.expressions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Property<T> {
    private final Supplier<T> getter;
    private final Function<T,T> setter;

    public Property(Supplier<T> getter, Function<T,T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public interface PropertyBuilder<T> {
        public Property<T> set(Function<T,T> setter);
    }
    public static <T> PropertyBuilder<T> get(Supplier<T> getter) {
        return setter -> new Property<T>(getter, setter);
    }

    public T get() {
        return getter.get();
    }

    public T set(T value) {
        return setter.apply(value);
    }

}

package uk.co.benjiweber.expressions;

import static uk.co.benjiweber.expressions.Exceptions.unchecked;

public class ChainableVoid<T> {
    private final T obj;

    public ChainableVoid(T obj) {
        this.obj = obj;
    }

    public static <T> ChainableVoid<T> chain(T instance) {
        return new ChainableVoid<>(instance);
    }

    public ChainableVoid<T> invoke(VoidMethodOn<T> voidMethod) {
        unchecked(() -> voidMethod.invoke(obj));
        return this;
    }

    public interface VoidMethodOn<T> {
        void invoke(T instance) throws Exception;
    }

    public T unwrap() {
        return obj;
    }

}

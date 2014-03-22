package uk.co.benjiweber.expressions.exceptions;

public interface ExceptionalFunction<T,R, E extends Exception> {
    R apply(T t) throws E;
}

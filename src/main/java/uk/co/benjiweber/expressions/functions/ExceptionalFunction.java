package uk.co.benjiweber.expressions.functions;

public interface ExceptionalFunction<A, R, E extends Exception> {
    R apply(A a) throws E;
}

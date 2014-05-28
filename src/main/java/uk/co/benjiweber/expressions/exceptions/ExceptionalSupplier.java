package uk.co.benjiweber.expressions.exceptions;

public interface ExceptionalSupplier<R, E extends Exception> {
    R apply() throws E;
}

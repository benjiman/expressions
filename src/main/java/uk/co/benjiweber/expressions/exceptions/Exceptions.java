package uk.co.benjiweber.expressions.exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class Exceptions {
    public static <T, E extends Exception> T unchecked(ExceptionalSupplier<T,E> supplier) {
        try {
            return supplier.supply();
        } catch (Error | RuntimeException rex) {
            throw rex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void unchecked(ExceptionalVoid method) {
        try {
            method.invoke();
        } catch (Error | RuntimeException rex) {
            throw rex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface Wrapper<T> {
        <U extends Exception> T in(Function<Exception, U> exceptionMapper) throws U;
        <U extends Exception> T in(Supplier<U> exceptionSupplier) throws U;
        <U extends Exception> T in(Class<U> exceptionClass) throws U;
    }

    public static <T, E extends Exception> Wrapper<T> wrappingChecked(ExceptionalSupplier<T, E> supplier) {
        return new Wrapper<T>() {
            public <U extends Exception> T in(Function<Exception, U> exceptionMapper) throws U {
                try {
                    return supplier.supply();
                } catch (RuntimeException | Error rex) {
                    throw rex;
                } catch (Exception e) {
                    throw exceptionMapper.apply(e);
                }
            }

            @Override
            public <U extends Exception> T in(Supplier<U> exceptionSupplier) throws U {
                try {
                    return supplier.supply();
                } catch (RuntimeException | Error rex) {
                    throw rex;
                } catch (Exception e) {
                    throw exceptionSupplier.get();
                }
            }

            public <U extends Exception> T in(Class<U> exceptionClass) throws U {
                try {
                    return supplier.supply();
                } catch (RuntimeException | Error rex) {
                    throw rex;
                } catch (Exception e) {
                    throw constructAndWrapIfPossible(exceptionClass, e);
                }
            }
        };
    }

    public static <T, E extends Exception> Wrapper<T> wrappingAll(ExceptionalSupplier<T,E> supplier) {
        return new Wrapper<T>() {
            public <U extends Exception> T in(Function<Exception, U> exceptionMapper) throws U {
                try {
                    return supplier.supply();
                } catch (Exception e) {
                    throw exceptionMapper.apply(e);
                }
            }

            @Override
            public <U extends Exception> T in(Supplier<U> exceptionSupplier) throws U {
                try {
                    return supplier.supply();
                } catch (Exception e) {
                    throw exceptionSupplier.get();
                }
            }

            public <U extends Exception> T in(Class<U> exceptionClass) throws U {
                try {
                    return supplier.supply();
                } catch (Exception e) {
                    throw constructAndWrapIfPossible(exceptionClass, e);
                }
            }
        };
    }

    public interface ExceptionalSupplier<T, E extends Exception> {
        T supply() throws E;
    }

    public interface ExceptionalVoid {
        void invoke() throws Exception;
    }

    static class UnableToInstantiateSuppliedException extends RuntimeException {
        public UnableToInstantiateSuppliedException(Exception e) {
            super(e);
        }
    }

    private static <U extends Exception> U constructAndWrapIfPossible(Class<U> exceptionClass, Exception e) {
        try {
            return exceptionClass.getConstructor(Exception.class).newInstance(e);
        } catch (Exception ex1) {
            try {
                return exceptionClass.getConstructor().newInstance();
            } catch (Exception ex2) {
                throw new UnableToInstantiateSuppliedException(ex2);
            }
        }
    }

    public static <T,R,E extends Exception> Function<T,Optional<R>> toOptional(ExceptionalFunction<T,R,E> f) {
        return t -> {
            try {
                return Optional.ofNullable(f.apply(t));
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    public static <R,E extends Exception> Supplier<Optional<R>> toOptional(ExceptionalSupplier<R,E> f) {
        return () -> {
            try {
                return Optional.ofNullable(f.supply());
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }
}

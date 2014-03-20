package uk.co.benjiweber.expressions;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Supplier;

public class Exceptions {
    public static <T> T unchecked(ExceptionalSupplier<T> supplier) {
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

    public static <T> Wrapper<T> wrappingChecked(ExceptionalSupplier<T> supplier) {
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

    public static <T> Wrapper<T> wrappingAll(ExceptionalSupplier<T> supplier) {
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

    public interface ExceptionalSupplier<T> {
        T supply() throws Exception;
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


}

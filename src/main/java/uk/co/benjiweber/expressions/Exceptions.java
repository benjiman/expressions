package uk.co.benjiweber.expressions;

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

    public interface ExceptionalSupplier<T> {
        T supply() throws Exception;
    }

    public interface ExceptionalVoid {
        void invoke() throws Exception;
    }
}

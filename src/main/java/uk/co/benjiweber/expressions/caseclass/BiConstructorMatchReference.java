package uk.co.benjiweber.expressions.caseclass;

import java.util.function.BiFunction;

public interface BiConstructorMatchReference<T> {
    T comparee();
    public interface UniMatchBiConstructorMatchReference<T,M0> extends BiConstructorMatchReference<T> {}
    public interface BiMatchBiConstructorMatchReference<T,M0,M1> extends BiConstructorMatchReference<T> {}
    public static <A,B,T> BiConstructorMatchReference<T> $2(BiFunction<A,B,T> constructor, A arg0, B arg1) {
        return null;
    }
    public static <A,B,T> UniMatchBiConstructorMatchReference<T,B> $(BiFunction<A,B,T> constructor, A arg0, MatchesAny _) {
        return () -> constructor.apply(arg0, null);
    }
    public static <A,B,T> UniMatchBiConstructorMatchReference<T,B> $(BiFunction<A,B,T> constructor, MatchesAny _, B arg1) {
        return () -> constructor.apply(null, arg1);
    }
    public static <A,B,T> BiMatchBiConstructorMatchReference<T,A,B> $(BiFunction<A, B, T> constructor, MatchesAny _, MatchesAny arg1) {
        return () -> constructor.apply(null, null);
    }
}

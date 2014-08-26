package uk.co.benjiweber.expressions.caseclass.constructor;

import uk.co.benjiweber.expressions.caseclass.MatchesAny;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.NoMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;

import java.util.function.BiFunction;

public interface BiConstructor<T> {
    T comparee();
    public static <A,B,T> NoMatch<T> $(BiFunction<A,B,T> constructor, A arg0, B arg1) {
        return () -> constructor.apply(arg0, arg1);
    }

    public static <A,B,T> UniMatch<T,B> $(BiFunction<A,B,T> constructor, A arg0, MatchesAny _) {
        return () -> constructor.apply(arg0, null);
    }
    public static <A,B,T> UniMatch<T,A> $(BiFunction<A,B,T> constructor, MatchesAny _, B arg1) {
        return () -> constructor.apply(null, arg1);
    }
    public static <A,B,M0,T> UniMatch<T,M0> $(BiFunction<A,B,T> constructor, A arg0, UniMatch<B,M0> arg1) {
        return () -> constructor.apply(arg0, arg1.comparee());
    }
    public static <A,B,M0,T> UniMatch<T,M0> $(BiFunction<A,B,T> constructor, UniMatch<A,M0> arg0, B arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1);
    }

    public static <A,B,T> BiMatch<T,A,B> $(BiFunction<A, B, T> constructor, MatchesAny arg0, MatchesAny arg1) {
        return () -> constructor.apply(null, null);
    }
    public static <A,B,M0,M1,T> BiMatch<T,M0,M1> $(BiFunction<A, B, T> constructor, A arg0, BiMatch<B,M0,M1> arg1) {
        return () -> constructor.apply(arg0, arg1.comparee());
    }
    public static <A,B,M0,M1,T> BiMatch<T,M0,M1> $(BiFunction<A, B, T> constructor, BiMatch<A,M0,M1> arg0, B arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1);
    }
    public static <A,B,M0,M1,T> BiMatch<T,M0,M1> $(BiFunction<A, B, T> constructor, UniMatch<A,M0> arg0, UniMatch<B,M1> arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1.comparee());
    }
    public static <A,B,M0,T> BiMatch<T,M0,B> $(BiFunction<A, B, T> constructor, UniMatch<A,M0> arg0, MatchesAny arg1) {
        return () -> constructor.apply(arg0.comparee(), null);
    }
    public static <A,B,M0,T> BiMatch<T,A,M0> $(BiFunction<A, B, T> constructor, MatchesAny arg0, UniMatch<B,M0> arg1) {
        return () -> constructor.apply(null, arg1.comparee());
    }

    public static <A,B,M0,M1,M2,T> TriMatch<T,M0,M1,M2> $(BiFunction<A, B, T> constructor, A arg0, TriMatch<B,M0,M1,M2> arg1) {
        return () -> constructor.apply(arg0, arg1.comparee());
    }
    public static <A,B,M0,M1,M2,T> TriMatch<T,M0,M1,M2> $(BiFunction<A, B, T> constructor,  TriMatch<A,M0,M1,M2> arg0, B arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1);
    }

    public static <A,B,M1,M2,T> TriMatch<T,A,M1,M2> $(BiFunction<A, B, T> constructor, MatchesAny arg0, BiMatch<B,M1,M2> arg1) {
        return () -> constructor.apply(null, arg1.comparee());
    }

    public static <A,B,M1,M2,T> TriMatch<T,M1,M2,B> $(BiFunction<A, B, T> constructor, BiMatch<A,M1,M2> arg0, MatchesAny arg1) {
        return () -> constructor.apply(arg0.comparee(), null);
    }
}

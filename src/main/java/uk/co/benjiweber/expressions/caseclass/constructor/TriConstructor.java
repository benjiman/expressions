package uk.co.benjiweber.expressions.caseclass.constructor;

import uk.co.benjiweber.expressions.caseclass.MatchesAny;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.NoMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;
import uk.co.benjiweber.expressions.functions.TriFunction;


public interface TriConstructor<T> {
    T comparee();
    public static <A,B,C,T> NoMatch<T> $(TriFunction<A,B,C,T> constructor, A arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1, arg2);
    }
    public static <A,B,C,D,T> UniMatch<T,D> $(TriFunction<A, B, C, T> constructor, A arg0, B arg1, UniMatch<C,D> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }
    public static <A,B,C,D,T> UniMatch<T,D> $(TriFunction<A, B, C, T> constructor, A arg0, UniMatch<B,D> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }
    public static <A,B,C,D,T> UniMatch<T,D> $(TriFunction<A, B, C, T> constructor, UniMatch<A,D> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public static <A,B,C,T> UniMatch<T,A> $(TriFunction<A, B, C, T> constructor, A arg0, B arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0, arg1, null);
    }
    public static <A,B,C,T> UniMatch<T,B> $(TriFunction<A, B, C, T> constructor, A arg0, MatchesAny arg1, C arg2) {
        return () -> constructor.apply(arg0, null, arg2);
    }
    public static <A,B,C,T> UniMatch<T,C> $(TriFunction<A, B, C, T> constructor, MatchesAny arg0, B arg1, C arg2) {
        return () -> constructor.apply(null, arg1, arg2);
    }


    public static <A,B,C,D,E,T> BiMatch<T,D,E> $(TriFunction<A, B, C, T> constructor, BiMatch<A,D,E> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public static <A,B,C,D,E,T> BiMatch<T,D,E> $(TriFunction<A, B, C, T> constructor, A arg0, BiMatch<B,D,E> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }

    public static <A,B,C,D,E,T> BiMatch<T,D,E> $(TriFunction<A, B, C, T> constructor, A arg0, B arg1, BiMatch<C,D,E> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }

    public static <A,B,C,T> BiMatch<T,B,C> $(TriFunction<A, B, C, T> constructor, A arg0, MatchesAny arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0, null, null);
    }
    public static <A,B,C,T> BiMatch<T,A,C> $(TriFunction<A, B, C, T> constructor, MatchesAny arg0, B arg1, MatchesAny arg2) {
        return () -> constructor.apply(null, arg1, null);
    }
    public static <A,B,C,T> BiMatch<T,A,B> $(TriFunction<A, B, C, T> constructor, MatchesAny arg0, MatchesAny arg1, C arg2) {
        return () -> constructor.apply(null, null, arg2);
    }

    public static <A,B,C,M0,T> BiMatch<T,A,M0> $(TriFunction<A, B, C, T> constructor, MatchesAny arg0, B arg1, UniMatch<C,M0> arg2) {
        return () -> constructor.apply(null, arg1, arg2.comparee());
    }
    public static <A,B,C,M0,T> BiMatch<T,B,M0> $(TriFunction<A, B, C, T> constructor, A arg0, MatchesAny arg1, UniMatch<C,M0> arg2) {
        return () -> constructor.apply(arg0, null, arg2.comparee());
    }
    public static <A,B,C,M0,T> BiMatch<T,M0,C> $(TriFunction<A, B, C, T> constructor, MatchesAny arg0, UniMatch<B,M0> arg1, C arg2) {
        return () -> constructor.apply(null, arg1.comparee(), arg2);
    }
    public static <A,B,C,M0,T> BiMatch<T,M0,C> $(TriFunction<A, B, C, T> constructor, A arg0, UniMatch<B,M0> arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), null);
    }
    public static <A,B,C,M0,T> BiMatch<T,M0,B> $(TriFunction<A, B, C, T> constructor, UniMatch<A,M0> arg0, MatchesAny arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), null, arg2);
    }
    public static <A,B,C,M0,T> BiMatch<T,M0,C> $(TriFunction<A, B, C, T> constructor, UniMatch<A,M0> arg0, B arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, null);
    }

    public static <A,B,C,D,E,T> TriMatch<T,D,E,B> $(TriFunction<A, B, C, T> constructor, MatchesAny _,  BiMatch<B,D,E> arg1, C arg2) {
        return () -> constructor.apply(null, arg1.comparee(), arg2);
    }

    public static <A,B,C,D,E,T> TriMatch<T,D,E,B> $(TriFunction<A, B, C, T> constructor, MatchesAny _, B arg1, BiMatch<C,D,E> arg2) {
        return () -> constructor.apply(null, arg1, arg2.comparee());
    }

    public static <A,B,C,D,E,T> TriMatch<T,D,E,B> $(TriFunction<A, B, C, T> constructor, BiMatch<A,D,E> arg0, MatchesAny _, C arg2) {
        return () -> constructor.apply(arg0.comparee(), null, arg2);
    }

    public static <A,B,C,D,E,T> TriMatch<T,D,E,B> $(TriFunction<A, B, C, T> constructor, A arg0, MatchesAny _, BiMatch<C,D,E> arg2) {
        return () -> constructor.apply(arg0, null, arg2.comparee());
    }

    public static <A,B,C,D,E,T> TriMatch<T,D,E,B> $(TriFunction<A, B, C, T> constructor, A arg0, BiMatch<B,D,E> arg1, MatchesAny _) {
        return () -> constructor.apply(arg0, arg1.comparee(), null);
    }

    public static <A,B,C,D,E,T> TriMatch<T,D,E,B> $(TriFunction<A, B, C, T> constructor, BiMatch<A,D,E> arg0, B arg1, MatchesAny _) {
        return () -> constructor.apply(arg0.comparee(), arg1, null);
    }

    public static <A,B,C,D,E,F,T> TriMatch<T,D,E,F> $(TriFunction<A, B, C, T> constructor, TriMatch<A,D,E,F> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public static <A,B,C,D,E,F,T> TriMatch<T,D,E,F> $(TriFunction<A, B, C, T> constructor, A arg0, TriMatch<B,D,E,F> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }

    public static <A,B,C,D,E,F,T> TriMatch<T,D,E,F> $(TriFunction<A, B, C, T> constructor, A arg0, B arg1,TriMatch<C,D,E,F> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }
}

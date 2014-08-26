package uk.co.benjiweber.expressions.caseclass;

import uk.co.benjiweber.expressions.functions.TriFunction;

import static uk.co.benjiweber.expressions.caseclass.BiConstructorMatchReference.UniMatchBiConstructorMatchReference;

public interface TriConstructorMatchReference<T> {
    T comparee();
    public interface UniMatchTriConstructorMatchReference<T,M0> extends TriConstructorMatchReference<T> {}
    public interface BiMatchTriConstructorMatchReference<T,M0,M1> extends TriConstructorMatchReference<T> {}
    public static <A,B,C,T> TriConstructorMatchReference<T> $3(TriFunction<A,B,C,T> constructor, A arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1, arg2);
    }
    public static <A,B,C,D,T> UniMatchTriConstructorMatchReference<T,D> $(TriFunction<A, B, C, T> constructor, A arg0, B arg1, UniMatchBiConstructorMatchReference<C,D> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }
    public static <A,B,C,D,T> UniMatchTriConstructorMatchReference<T,D> $(TriFunction<A, B, C, T> constructor, A arg0, UniMatchBiConstructorMatchReference<B,D> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }
    public static <A,B,C,D,T> UniMatchTriConstructorMatchReference<T,D> $(TriFunction<A, B, C, T> constructor, UniMatchBiConstructorMatchReference<A,D> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public static <A,B,C,D,E,T> BiMatchTriConstructorMatchReference<T,D,E> $(TriFunction<A, B, C, T> constructor, BiConstructorMatchReference.BiMatchBiConstructorMatchReference<A,D,E> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public static <A,B,C,D,E,T> BiMatchTriConstructorMatchReference<T,D,E> $(TriFunction<A, B, C, T> constructor, A arg0, BiConstructorMatchReference.BiMatchBiConstructorMatchReference<B,D,E> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }

    public static <A,B,C,D,E,T> BiMatchTriConstructorMatchReference<T,D,E> $(TriFunction<A, B, C, T> constructor, A arg0, B arg1, BiConstructorMatchReference.BiMatchBiConstructorMatchReference<C,D,E> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }

}

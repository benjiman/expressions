package uk.co.benjiweber.expressions.caseclass.constructor;

import uk.co.benjiweber.expressions.caseclass.MatchesAny;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.NoMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;

import java.util.function.Function;

public interface UniConstructor<T> extends ForComparing<T> {
    public static <A,T> NoMatch<T> $(Function<A, T> constructor, A arg0) {
        return () -> constructor.apply(arg0);
    }
    public static <A,T> UniMatch<T,A> $(Function<A, T> constructor, MatchesAny _) {
        return () -> constructor.apply(null);
    }
    public static <A,T,M0> UniMatch<T,A> $(Function<A, T> constructor, UniMatch<A,M0> arg0) {
        return () -> constructor.apply(arg0.comparee());
    }
    public static <A,M0,M1,T> BiMatch<T,M0,M1> $(Function<A, T> constructor, BiMatch<A,M0,M1> arg0) {
        return () -> constructor.apply(arg0.comparee());
    }
    public static <A,M0,M1,M2,T> TriMatch<T,M0,M1,M2> $(Function<A, T> constructor, TriMatch<A,M0,M1,M2> arg0) {
        return () -> constructor.apply(arg0.comparee());
    }
}

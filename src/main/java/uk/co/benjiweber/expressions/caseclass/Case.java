package uk.co.benjiweber.expressions.caseclass;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Arrays.asList;

public interface Case<T> {
    default MatchBuilder<T> match() {
        return new MatchBuilder<T>() {
            public <R> MatchBuilderR<T, R> when(T value, Function<T, R> f) {
                return new MatchBuilderR<T, R>(asList(MatchDefinition.create(value, f)), Case.this);
            }
            public <R, A, B> MatchBuilderR<T, R> when(TwoMissing<T, A, B> value, BiFunction<A, B, R> f) {
                Function<T,R> valueExtractor = t -> f.apply(value.prop1((T)Case.this), value.prop2((T)Case.this));
                return new MatchBuilderR<T, R>(asList(MatchDefinition.create(value.original(), valueExtractor)), Case.this);
            }
            public <R, A, B> MatchBuilderR<T, R> when(OneMissing<T, A> value, Function<A, R> f) {
                Function<T,R> valueExtractor = t -> f.apply(value.prop1((T) Case.this));
                return new MatchBuilderR<T, R>(asList(MatchDefinition.create(value.original(), valueExtractor)), Case.this);
            }
        };
    }

    default <A> OneMissing<T,A> missing(Function<T,A> prop1) {
        return new OneMissing<T, A>() {
            public A prop1(T extractFrom) { return prop1.apply(extractFrom); }
            public T original() { return (T)Case.this; }
        };
    }

    default <A,B> TwoMissing<T,A,B> missing(Function<T,A> prop1, Function<T,B> prop2) {
        return new TwoMissing<T, A, B>() {
            public A prop1(T extractFrom) { return prop1.apply(extractFrom); }
            public B prop2(T extractFrom) { return prop2.apply(extractFrom); }
            public T original() { return (T)Case.this; }
        } ;
    }

    interface TwoMissing<T,A,B> {
        A prop1(T extractFrom);
        B prop2(T extractFrom);
        T original();
    }
    interface OneMissing<T,A> {
        A prop1(T extractFrom);
        T original();
    }

    public interface MatchBuilder<T> {
        <R> MatchBuilderR<T,R> when(T value, Function<T, R> f);
        <R,A,B> MatchBuilderR<T,R> when(TwoMissing<T,A,B> value, BiFunction<A,B,R> f);
    }

    interface MatchDefinition<T,R> {
        T value();
        Function<T,R> f();
        static <T,R> MatchDefinition<T,R> create(T value, Function<T,R> f) {
            return new MatchDefinition<T, R>() {
                public T value() { return value; }
                public Function<T, R> f() { return f; }
            };
        }

        static <T,R> Predicate<MatchDefinition<T,R>> matches(Case<T> value) {
            return match -> Objects.equals(match.value(), value);
        }
    }

    public static class MatchBuilderR<T,R> {
        private List<MatchDefinition<T,R>> cases = new ArrayList<MatchDefinition<T, R>>();
        private Function<T, R> defaultCase;
        private Case<T> value;

        private MatchBuilderR(List<MatchDefinition<T,R>> cases, Case<T> value) {
            this.value = value;
            this.cases.addAll(cases);
        }

        public MatchBuilderR<T,R> when(T value, Function<T,R> f) {
            cases.add(MatchDefinition.create(value, f));
            return this;
        }

        public <A> MatchBuilderR<T,R> when(OneMissing<T,A> value, Function<A,R> f) {
            Function<T,R> valueExtractor = t -> f.apply(value.prop1((T)this.value));
            cases.add(MatchDefinition.create(value.original(), valueExtractor));
            return this;
        }

        public <A,B> MatchBuilderR<T,R> when(TwoMissing<T,A,B> value, BiFunction<A,B,R> f) {
            Function<T,R> valueExtractor = t -> f.apply(value.prop1((T)this.value), value.prop2((T)this.value));
            cases.add(MatchDefinition.create(value.original(), valueExtractor));
            return this;
        }

        public R _(R defaultValue) {
            return _(t -> defaultValue);
        }

        public R _(Function<T,R> f) {
            defaultCase = f;
            return cases.stream()
                .filter(MatchDefinition.matches(value))
                .findFirst()
                .map(match -> match.f().apply((T)value))
                .orElseGet(() -> defaultCase.apply((T)value));
        }
    }

}



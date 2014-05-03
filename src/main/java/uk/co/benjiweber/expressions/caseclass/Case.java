package uk.co.benjiweber.expressions.caseclass;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Case<T> {
    default MatchBuilder<T> match() {
        return new MatchBuilder<T>() {
            public <R> MatchBuilderR<T, R> when(T value, Function<T, R> f) {
                return new MatchBuilderR<T, R>(Arrays.asList(MatchDefinition.create(value,f)), Case.this);
            }
        };
    }

    public interface MatchBuilder<T> {
        <R> MatchBuilderR<T,R> when(T value, Function<T, R> f);
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



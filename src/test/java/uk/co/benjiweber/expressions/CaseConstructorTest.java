package uk.co.benjiweber.expressions;

import org.junit.Test;
import uk.co.benjiweber.expressions.caseclass.Case;
import uk.co.benjiweber.expressions.caseclass.MatchesAny;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.CaseConstructorTest.Person.person;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class CaseConstructorTest {

    @Test
    public void constructor_matching_example() {
        Person so = person("Some", "One");

        String someone = so.match()
                .when(person("Ann", "Other"), p -> "another")
                .when(person("Some", "One"), p -> "someone")
                ._("Unknown Person");

        assertEquals("someone", someone);
    }

    @Test
    public void constructor_matching_any() {
        Person so = person("Some", "One");
        Person an = person("Ann", "Other");

        String someone = so.match()
                .when(person("Some", _), p -> "someone")
                .when(person(_, "Other"), p -> "another")
                ._("Unknown Person");

        assertEquals("someone", someone);

        String another = an.match()
                .when(person("Some", _), p -> "someone")
                .when(person(_, "Other"), p -> "another")
                ._("Unknown Person");

        assertEquals("another", another);

    }

    interface Person extends Case<Person>, EqualsHashcode<Person>, ToString<Person> {
        String firstname();
        String lastname();

        static Person person(String firstname, String lastname) {
            return person(firstname, lastname, Person::firstname, Person::lastname);
        }
        static Person person(String firstname, MatchesAny lastname) {
            return person(firstname, null, Person::firstname);
        }
        static Person person(MatchesAny firstname, String lastname) {
            return person(null, lastname, Person::lastname);
        }
        static Person person(String firstname, String lastname, Function<Person, ?>... props) {
            return new Person() {
                public String firstname() { return firstname; }
                public String lastname() { return lastname; }
                @Override public String toString() { return autoToString(); }
                @Override public boolean equals(Object o) { return autoEquals(o); }
                @Override public int hashCode() { return autoHashCode(); }
                public List<Function<Person, ?>> props() { return asList(props); }
            };
        }
    }


}

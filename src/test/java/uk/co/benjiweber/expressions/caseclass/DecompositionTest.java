package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.EqualsHashcode;
import uk.co.benjiweber.expressions.ToString;
import uk.co.benjiweber.expressions.Value;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.caseclass.DecompositionTest.Person.person;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class DecompositionTest {
    @Test
    public void decomposition_variable_items_example() {
        Person a = person("Bob", "Smith", 18);
        Person b = person("Bill", "Smith", 28);
        Person c = person("Old", "Person", 90);

        assertEquals("first_Smith_18", matchExample(a));
        assertEquals("second_28", matchExample(b));
        assertEquals("unknown", matchExample(c));
    }

    String matchExample(Person person) {
        return person.match()
            .when(person("Bob", _, _), (surname, age) -> "first_" + surname + "_" + age)
            .when(person("Bill", "Smith", _), age -> "second_" + age)
            ._("unknown");
    }


    interface Person extends Case<Person> {
        String firstname();
        String lastname();
        Integer age();

        static Person person(String firstname, String lastname, Integer age, Function<Person, ?>... props) {
            abstract class PersonValue extends Value<Person> implements Person {}
            return new PersonValue() {
                public String firstname() { return firstname; }
                public String lastname() { return lastname; }
                public Integer age() { return age; }
            }.using(props);
        }

        static Person person(String firstname, String lastname, Integer age) {
            return person(firstname, lastname, age, Person::firstname, Person::lastname, Person::age);
        }
        static OneMissing<Person, String> person(String firstname, MatchesAny lastname, Integer age) {
            return person(firstname, null, age, Person::firstname, Person::age)
                .missing(Person::lastname);
        }
        static OneMissing<Person, String> person(MatchesAny firstname, String lastname, Integer age) {
            return person(null, lastname, age, Person::lastname, Person::age)
                .missing(Person::firstname);
        }
        static OneMissing<Person, Integer> person(String firstName, String lastname, MatchesAny age) {
            return person(firstName, lastname, null, Person::firstname, Person::lastname)
                .missing(Person::age);
        }
        static TwoMissing<Person, String, String> person(MatchesAny firstname, MatchesAny lastname, Integer age) {
            return person(null, null, age, Person::age)
                .missing(Person::firstname, Person::lastname);
        }
        static TwoMissing<Person, String, Integer> person(String firstname, MatchesAny lastname, MatchesAny age) {
            return person(firstname, null, null, Person::firstname)
                .missing(Person::lastname, Person::age);
        }
        static TwoMissing<Person, String, Integer> person(MatchesAny firstname, String lastname, MatchesAny age) {
            return person(null, lastname, null, Person::lastname)
                .missing(Person::firstname, Person::age);
        }
    }

}

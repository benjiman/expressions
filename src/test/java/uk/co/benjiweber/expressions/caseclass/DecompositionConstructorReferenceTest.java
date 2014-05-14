package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.Value;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.caseclass.DecompositionConstructorReferenceTest.Person.person;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class DecompositionConstructorReferenceTest {
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
            .when(Person::person, "Bob", _, _).then( (surname, age) -> "first_" + surname + "_" + age )
            .when(Person::person,"Bill", "Smith",_).then( age -> "second_" + age )
            ._("unknown");
    }


    public interface Person extends Case<Person> {
        String firstname();
        String lastname();
        Integer age();

        static Person person(String firstname, String lastname, Integer age) {
            abstract class PersonValue extends Value<Person> implements Person {}
            return new PersonValue() {
                public String firstname() { return firstname; }
                public String lastname() { return lastname; }
                public Integer age() { return age; }
            }.using(Person::firstname, Person::lastname, Person::age);
        }
    }

}

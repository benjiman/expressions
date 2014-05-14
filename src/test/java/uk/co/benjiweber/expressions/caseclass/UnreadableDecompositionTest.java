package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.caseclass.DecompositionConstructorReferenceTest.Person;
import static uk.co.benjiweber.expressions.caseclass.DecompositionConstructorReferenceTest.Person.person;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class UnreadableDecompositionTest {


    @Test
    public void one_missings() {
        assertEquals("foo", person("Bob", "asdf", 18).match().when(Person::person, _, "asdf", 18).then(a -> "foo")._("unknown"));
        assertEquals("unknown", person("Bob", "fdsa", 18).match().when(Person::person, _, "asdf", 18).then(a -> "foo")._("unknown"));

        assertEquals("foo", person("Bob", "sfasdf", 18).match().when(Person::person, "Bob", _, 18).then(a -> "foo")._("unknown"));
        assertEquals("unknown", person("Bob", "", 18).match().when(Person::person, "Bill", _, 18).then(a -> "foo")._("unknown"));

        assertEquals("foo", person("Bob", "sfasdf", 18).match().when(Person::person, "Bob", "sfasdf", _).then(a -> "foo")._("unknown"));
        assertEquals("unknown", person("Bob", "", 18).match().when(Person::person, "Bob", "sfasdf", _).then(a -> "foo")._("unknown"));


        assertEquals("foo", person("Bob", "asdf", 18).match().when(Person::person, _, "aaaaaaa", 18).then(age -> "aaaaaaa").when(Person::person, _,"asdf", 18).then(a -> "foo")._("unknown"));
        assertEquals("unknown", person("Bob", "fdsa", 18).match().when(Person::person, _, "aaaaaaa", 18).then(age -> "aaaaaaa").when(Person::person, _,"asdf", 18).then(a -> "foo")._("unknown"));

        assertEquals("foo", person("Bob", "asdf", 18).match().when(Person::person, _, "aaaaaaa", 18).then(age -> "aaaaaaa").when(Person::person, "Bob",_, 18).then(a -> "foo")._("unknown"));
        assertEquals("unknown", person("Bob", "fdsa", 18).match().when(Person::person, _, "aaaaaaa", 18).then(age -> "aaaaaaa").when(Person::person, "Bill",_, 18).then(a -> "foo")._("unknown"));

        assertEquals("foo", person("Bob", "asdf", 18).match().when(Person::person, _, "aaaaaaa", 18).then(age -> "aaaaaaa").when(Person::person, "Bob","asdf", _).then(a -> "foo")._("unknown"));
        assertEquals("unknown", person("Bob", "fdsa", 18).match().when(Person::person, _, "aaaaaaa", 18).then(age -> "aaaaaaa").when(Person::person, "Bob","asdf", _).then(a -> "foo")._("unknown"));
    }

    @Test
    public void all_missing() {
        assertEquals("foo", person("Bob","asdf",18).match().when(Person::person, _,_,_).then((firstname,lastname,age) -> "foo")._(""));
    }

    @Test
    public void none_missing() {
        assertEquals("foo", person("Bob","asdf",18).match().when(Person::person, "Bob", "asdf", 18).then(person -> "foo")._(""));
    }


}

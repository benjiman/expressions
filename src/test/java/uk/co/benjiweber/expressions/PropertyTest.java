package uk.co.benjiweber.expressions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static uk.co.benjiweber.expressions.Property.get;

public class PropertyTest {

    static class Person {
        private String name;
        public Property<String> Name = get(() -> name).set(value -> this.name = value);
    }

    @Test
    public void property() {
        Person person = new Person();
        assertNull(person.Name.get());
        person.Name.set("Bob");
        assertEquals("Bob", person.Name.get());
        person.Name.set("Bill");
        assertEquals("Bill", person.Name.get());
    }
}

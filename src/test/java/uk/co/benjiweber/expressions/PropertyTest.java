package uk.co.benjiweber.expressions;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static uk.co.benjiweber.expressions.Property.get;

public class PropertyTest {

    static class Person {
        private String name;
        public final Property<String> Name = get(() -> name).set(value -> this.name = value);
    }

    @Test
    public void property() {
        Person person = new Person();
        assertNull(person.Name.get());
        assertNull(person.name);

        person.Name.set("Bob");
        assertEquals("Bob", person.Name.get());
        assertEquals("Bob", person.name);

        person.Name.set("Bill");
        assertEquals("Bill", person.Name.get());
        assertEquals("Bill", person.name);
    }

    @Test
    public void pass_around_references() {
        Person person = new Person();
        person.Name.set("Bob");

        takesAProperty(person.Name, "Bill");
        assertEquals("Bill", person.Name.get());

        takesASetter(person.Name::set,  "Matt");
        assertEquals("Matt", person.Name.get());

        String got = takesAGetter(person.Name::get);
        assertEquals("Matt", got);
    }

    private void takesAProperty(Property<String> property, String newValue) {
        property.set(newValue);
    }

    private void takesASetter(Consumer<String> setter, String newValue) {
        setter.accept(newValue);
    }

    private String takesAGetter(Supplier<String> getter) {
        return getter.get();
    }
}

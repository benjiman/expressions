package uk.co.benjiweber.expressions;

import org.junit.Test;
import uk.co.benjiweber.expressions.properties.Named;
import uk.co.benjiweber.expressions.properties.Property;
import uk.co.benjiweber.expressions.properties.Readonly;
import uk.co.benjiweber.expressions.properties.Writeonly;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static uk.co.benjiweber.expressions.properties.Property.get;
import static uk.co.benjiweber.expressions.properties.Property.set;

public class PropertyTest {

    static class Person {
        private String name;
        public final Property<String> Name = get(() -> name).set(value -> name = value);
        public final Readonly<String> ReadOnlyName = get(() -> name).readonly();
        public final Writeonly<String> WriteOnlyname = set(value -> name = value);
    }

    @Test
    public void mixingPaint() {
        Paint red = Paint.create(100,0,0);
        Paint green = Paint.create(0,100,0);

        Paint mixed = red.mix(green);

        assertEquals(100, mixed.red());
        assertEquals(100, mixed.green());
    }

    @Test
    public void paintEquals() {
        Paint red = Paint.create(100,0,0);
        Paint green = Paint.create(0,100,0);

        Paint mixed1 = red.mix(green);
        Paint mixed2 = green.mix(red);

        assertEquals(mixed1, mixed2);
        assertNotEquals(red, green);
        assertNotEquals(red, mixed1);
    }

    @Test
    public void paintToString() {
        Paint red = Paint.create(100,0,0);
        Paint green = Paint.create(0,100,0);

        assertEquals("{100, 0, 0}", red.toString());
        assertEquals("{0, 100, 0}", green.toString());
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
        assertEquals("Bill", person.ReadOnlyName.get());

        person.WriteOnlyname.set("Alice");
        assertEquals("Alice", person.Name.get());
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

    @Test
    public void property_with_behaviour() {
        TimePeriod period = new TimePeriod();
        period.Hours.set(2D);
        assertEquals(7200D, period.seconds, 0);
        assertEquals(2D, period.Hours.get(), 0);

        period.Hours.set(3D);
        assertEquals(10800D, period.seconds, 0);
        assertEquals(3D, period.Hours.get(), 0);
    }

    @Test
    public void pass_around_references_to_hours() {
        TimePeriod period = new TimePeriod();
        period.Hours.set(2D);

        takesAProperty(period.Hours, 3D);
        assertEquals(3D, period.Hours.get(), 0);

        takesASetter(period.Hours::set,  2D);
        assertEquals(2D, period.Hours.get(), 0);

        Double got = takesAGetter(period.Hours::get);
        assertEquals(2D, got, 0);
    }



    static class TimePeriod {
        private double seconds;

        public final Property<Double> Hours = get(() -> seconds / 3600).set(value -> seconds = value * 3600);
    }

    public static class ExplicitPropertyNames {
        private String foo = "foo";
        public final Named<String> Foo = get(() -> foo).set(value -> foo = value).named("Foo");
        public final Named<String> Bar = get(() -> foo).set(value -> foo = value).named("Bar");
    }

    @Test
    public void explicit_named_property() {
        ExplicitPropertyNames o = new ExplicitPropertyNames();
        assertEquals("Foo", o.Foo.name());
        assertEquals("Bar", o.Bar.name());
    }

    public static class GuessablePropertyNames {
        private String foo = "foo";
        public final Named<String> Foo = get(() -> foo).set(value -> foo = value).named();
        public final Named<String> Bar = get(() -> foo).set(value -> foo = value).named();
    }

    @Test
    public void guessable_named_property() {
        GuessablePropertyNames o = new GuessablePropertyNames();
        assertEquals("Foo", o.Foo.name());
        assertEquals("Bar", o.Bar.name());
    }

    private <T> void takesAProperty(Property<T> property, T newValue) {
        property.set(newValue);
    }

    private <T> void takesASetter(Consumer<T> setter, T newValue) {
        setter.accept(newValue);
    }

    private <T> T takesAGetter(Supplier<T> getter) {
        return getter.get();
    }
}

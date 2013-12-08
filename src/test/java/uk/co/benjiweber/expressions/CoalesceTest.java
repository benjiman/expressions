package uk.co.benjiweber.expressions;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static uk.co.benjiweber.expressions.Coalesce.coalesce;

public class CoalesceTest {

    @Test
    public void should_return_first_non_null_value() {
        Person nullName = new Person(null);
        Person bob = new Person("bob");
        Person barbara = new Person("barbara");
        assertEquals("bob", coalesce(nullName::name, bob::name, barbara::name));
    }

    @Test
    public void should_be_lazy() {
        Person bob = new Person("bob");
        Person angryPerson = new Person("angry") {
            @Override public String name() {
                fail("Should not have asked for the angry person's name");
                return "angry";
            }
        };

        assertEquals("bob", coalesce(bob::name, angryPerson::name));
    }

    @Test
    public void should_be_able_to_use_lambdas() {
        assertEquals("bob", coalesce(() -> new Person("bob").name(), () -> new Person("barbara").name()));
    }

    @Test
    public void should_be_able_to_use_optionals() {
        assertEquals("bob",
            coalesce(
                () -> Optional.<String>empty(),
                () -> Optional.of(new Person("bob").name()),
                () -> Optional.of(new Person("barbara").name())
            ).get()
        );
    }


    static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }

}

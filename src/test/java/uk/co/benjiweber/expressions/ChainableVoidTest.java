package uk.co.benjiweber.expressions;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.benjiweber.expressions.ChainableVoid.chain;

public class ChainableVoidTest {

    @Test
    public void chaining_void_example() {
        Duck duck = chain(new Duck())
            .invoke(Duck::quack)
            .invoke(Duck::waddle)
            .invoke(Duck::setName, "ducky")
            .invoke(Duck::setFooBar, "curry_and_partially_apply", 5)
            .unwrap();

        assertTrue(duck.quackCalled);
        assertTrue(duck.waddleCalled);
        assertEquals("ducky", duck.name);
        assertEquals("curry_and_partially_apply", duck.foo);
        assertEquals(Integer.valueOf(5), duck.bar);

    }

    static class Duck {
        private boolean quackCalled = false;
        private boolean waddleCalled = false;
        public void quack() {
            quackCalled = true;
            System.out.println("quack");
        }
        public void waddle() {
            waddleCalled = true;
            System.out.println("waddle");
        }

        String name;
        public void setName(String name) {
            this.name = name;
        }

        String foo;
        Integer bar;
        public void setFooBar(String foo, Integer bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }
}


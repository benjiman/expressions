package uk.co.benjiweber.expressions;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static uk.co.benjiweber.expressions.ChainableVoid.chain;

public class ChainableVoidTest {

    @Test
    public void chaining_void_example() {
        Duck duck = chain(new Duck())
            .invoke(Duck::quack)
            .invoke(Duck::waddle)
            .unwrap();

        assertTrue(duck.quackCalled);
        assertTrue(duck.waddleCalled);
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
    }
}


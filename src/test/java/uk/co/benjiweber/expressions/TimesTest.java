package uk.co.benjiweber.expressions;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.Times.times;

public class TimesTest {

    @Test
    public void times_collection_size() {
        List<String> aCollection = asList("one", "two", "three");

        times(aCollection.size()).invoke(this::foo);

        assertEquals(3, numberOfTimesFooWasInvoked);
    }

    @Test
    public void times_literal() {
        times(6).invoke(this::bar);

        assertEquals(6, numberOfTimesBarWasInvoked);
    }


    int numberOfTimesFooWasInvoked = 0;
    private void foo() {
        numberOfTimesFooWasInvoked++;
    }

    int numberOfTimesBarWasInvoked = 0;
    private void bar() {
        numberOfTimesBarWasInvoked++;
    }
}

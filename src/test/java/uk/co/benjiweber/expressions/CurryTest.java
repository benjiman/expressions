package uk.co.benjiweber.expressions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.Curry.*;
public class CurryTest {

    @Test
    public void curry_bi() {
        assertEquals(new Integer(3), curry(CurryTest::add).apply(2).apply(1));
    }

    @Test
    public void curry_tri() {
        assertEquals(new Integer(6), curry(CurryTest::addThree).apply(3).apply(2).apply(1));
    }

    @Test
    public void curry_quad() {
        assertEquals("alphabetagammadelta", curry(CurryTest::concat).apply("alpha").apply("beta").apply("gamma").apply("delta"));
    }


    public static int add(int a, int b) {
        return a + b;
    }

    public static int addThree(int a, int b, int c) {
        return a + b + c;
    }

    public static String  concat(String a, String b, String c, String d) {
        return a + b + c + d;
    }



}

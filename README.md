expressions
===========

Playing with Java8. Here's Try as an Expression. [More Examples](https://github.com/benjiman/expressions/blob/master/src/test/java/uk/co/benjiweber/expressions/TryAsExpressionTest.java)
```java
    @Test public void should_return_try_value() {
        String result = Try(() -> {
            return "try";
        }).Catch(NullPointerException.class, e -> {
            return "catch";
        }).apply();

        assertEquals("try", result);
    }
```

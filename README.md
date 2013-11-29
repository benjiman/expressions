expressions
===========

Playing with Java8. Here's Try as an Expression
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

package uk.co.benjiweber.expressions.exceptions;

public class Example {

    public enum Do { THROW(true), NOT_THROW(false);
        public final boolean value;

        Do(boolean value) {
            this.value = value;
        }
    }
    public static String methodThatThrowsCheckedException(Do throwPlease) throws ExceptionsTest.ACheckedExceptionIDontHaveAGoodWayToDealWith {
        if (throwPlease.value) throw new ExceptionsTest.ACheckedExceptionIDontHaveAGoodWayToDealWith();
        return "foo";
    }

    public static void voidMethodThatThrowsACheckedException(Do throwPlease) throws ExceptionsTest.ACheckedExceptionIDontHaveAGoodWayToDealWith {
        if (throwPlease.value) throw new ExceptionsTest.ACheckedExceptionIDontHaveAGoodWayToDealWith();
    }

    public static void methodThatThrowsCheckedExceptionNoParams() throws ExceptionsTest.ACheckedExceptionIDontHaveAGoodWayToDealWith {

    }

    public static String methodThatThrowsNPE(Do throwPlease) {
        if(throwPlease.value) throw new NullPointerException();
        return "not_an_npe";
    }

    public static String throwingSupplier() throws ExceptionsTest.InputTooLongException {
        throw new ExceptionsTest.InputTooLongException();
    }

    public static String notThrowingSupplier() throws ExceptionsTest.InputTooLongException {
        return "hello";
    }

    public static String methodThatThrowsRuntimeException(Do throwPlease) {
        if(throwPlease.value) throw new NullPointerException();
        return "not_an_exception";
    }

    public static String duplicatesShortStrings(String input) throws ExceptionsTest.InputTooLongException {
        if (input.length() > 3) throw new ExceptionsTest.InputTooLongException();
        return input + input;
    }

    public static String uppercasesStrings(String input) throws ExceptionsTest.InputContainsUppercaseException {
        if (input.matches(".*?[A-Z].*")) throw new ExceptionsTest.InputContainsUppercaseException();
        return input.toUpperCase();
    }
}

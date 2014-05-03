package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.EqualsHashcode;
import uk.co.benjiweber.expressions.caseclass.Case;
import uk.co.benjiweber.expressions.caseclass.MatchesAny;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.co.benjiweber.expressions.caseclass.CaseParseArgumentsTest.Argument.arg;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;

public class CaseParseArgumentsTest {

    @Test
    public void parse_arguments_example() {
        applyArgument(arg("--help", "foo"));
        assertEquals("foo", this.helpRequested);

        applyArgument(arg("--lang", "English"));
        assertEquals("English", this.language);

        applyArgument(arg("--nonsense","this does not exist"));
        assertTrue(badArg);
    }

    private void applyArgument(Argument input) {
        input.match()
            .when(arg("--help", _), arg -> this.printHelp(arg.value()))
            .when(arg("--lang", _), arg -> this.setLanguage(arg.value()))
            ._(arg -> printUsageAndExit());
    }

    interface ArgumentHandler {
        void apply();
    }

    private String helpRequested;
    private CaseParseArgumentsTest printHelp(String command) {
        this.helpRequested = command;
        return this;
    }

    private String language;
    private CaseParseArgumentsTest setLanguage(String language) {
        this.language = language;
        return this;
    }

    private boolean badArg;
    private CaseParseArgumentsTest printUsageAndExit() {
        this.badArg = true;
        return this;
    }

    interface Argument extends Case<Argument>, EqualsHashcode<Argument> {
        String flag();
        String value();
        static Argument arg(String flag, MatchesAny value) {
            return arg(flag, null, Argument::flag);
        }
        static Argument arg(String flag, String value) {
            return arg(flag, value, Argument::flag, Argument::value);
        }
        static Argument arg(String flag, String value, Function<Argument,?>... props) {
            return new Argument() {
                public String flag() { return flag; }
                public String value() { return value; }
                public List<Function<Argument,?>> props() { return Arrays.asList(props); }
                @Override public boolean equals(Object o) { return autoEquals(o); }
                @Override public int hashCode() { return autoHashCode(); }
            };
        }
    }




}

package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;
import static uk.co.benjiweber.expressions.caseclass.NestedDecompositionTest.Address.address;
import static uk.co.benjiweber.expressions.caseclass.NestedDecompositionTest.Customer.customer;
import static uk.co.benjiweber.expressions.caseclass.NestedDecompositionTest.FirstLine.firstLine;
import static uk.co.benjiweber.expressions.caseclass.constructor.TriConstructor.*;
import static uk.co.benjiweber.expressions.caseclass.constructor.BiConstructor.*;
import static uk.co.benjiweber.expressions.caseclass.constructor.UniConstructor.*;
public class NestedDecompositionTest {

    @Test
    public void nested_decomposition_using_constructor_references() {
        Customer a = customer("Benji", "Weber", address(firstLine(123, "Some Road"), "AB123CD"));
        String result = a.match().when(
            $(Customer::customer, "Benji", "Weber", $(Address::address, _, "AB123CD"))
        ).then(firstLine -> firstLine.roadName())
        ._("unknown");

        assertEquals("Some Road", result);
    }

    @Test
    public void nested_decomposition_using_constructor_references_no_match() {
        Customer a = customer("Benji", "Weber", address(firstLine(123, "Some Road"), "AB123CD"));
        String result = a.match().when(
                $(Customer::customer, "Someone", "Else", $(Address::address, _, "AB123CD"))
        ).then(firstLine -> firstLine.roadName())
        ._("unknown");

        assertEquals("unknown", result);
    }

    @Test
    public void nested_decomposition_using_constructor_references_deeper() {
        Customer a = customer("Benji", "Weber", address(firstLine(123, "Some Road"), "AB123CD"));
        String result = a.match().when(
                $(Customer::customer, "Benji", "Weber", $(Address::address, $(FirstLine::firstLine, _, _), _))
        ).then((houseNo, road, postCode) -> houseNo + " " + road)
        ._("unknown");

        assertEquals("123 Some Road", result);
    }

    @Test
    public void nested_decomposition_using_constructor_references_optional() {
        Customer a = customer("Benji", "Weber", address(firstLine(123, "Some Road"), "AB123CD"));
        Optional<String> result = a.match().when(
                $(Customer::customer, "Benji", "Weber", $(Address::address, $(FirstLine::firstLine,_,_), _))
        ).then((houseNo, road, postCode) -> houseNo + " " + road)
        .toOptional();

        assertEquals("123 Some Road", result.get());
    }

    @Test
    public void nested_decomposition_using_constructor_references_optional_unknown() {
        Customer a = customer("Benji", "Weber", address(firstLine(123, "Some Road"), "AB123CD"));
        Optional<String> result = a.match().when(
                $(Customer::customer, "Someone", "Weber", $(Address::address, $(FirstLine::firstLine,_,_), _))
        ).then((houseNo, road, postCode) -> houseNo + " " + road)
                .toOptional();

        assertFalse(result.isPresent());
    }

    interface Customer extends Case<Customer> {
        String firstName();
        String lastName();
        Address address();
        public static Customer customer(String firstName, String lastName, Address address) {
            abstract class CustomerValue extends Value<Customer> implements Customer {}
            return new CustomerValue() {
                public String firstName() { return firstName; }
                public String lastName() { return lastName; }
                public Address address() { return address; }
            }.using(Customer::firstName, Customer::lastName, Customer::address);
        }
    }

    interface Address extends Case<Address> {
        FirstLine firstLine();
        String postCode();
        public static Address address(FirstLine firstLine, String postCode) {
            abstract class AddressValue extends Value<Address> implements Address {}
            return new AddressValue() {
                public FirstLine firstLine() { return firstLine; }
                public String postCode() { return postCode; }
            }.using(Address::firstLine, Address::postCode);
        }
    }

    interface FirstLine extends Case<FirstLine> {
        Integer houseNumber();
        String roadName();
        public static FirstLine firstLine(Integer houseNumber, String roadName) {
            abstract class FirstLineValue extends Value<FirstLine> implements FirstLine{}
            return new FirstLineValue() {
                public Integer houseNumber() { return houseNumber; }
                public String roadName() { return roadName; }
            }.using(FirstLine::houseNumber, FirstLine::roadName);
        }
    }
}

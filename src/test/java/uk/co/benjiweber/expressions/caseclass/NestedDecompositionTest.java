package uk.co.benjiweber.expressions.caseclass;

import org.junit.Test;
import uk.co.benjiweber.expressions.Curry;
import uk.co.benjiweber.expressions.Value;

import static junit.framework.TestCase.assertEquals;
import static uk.co.benjiweber.expressions.caseclass.MatchesAny._;
import static uk.co.benjiweber.expressions.caseclass.NestedDecompositionTest.Address.address;
import static uk.co.benjiweber.expressions.caseclass.NestedDecompositionTest.Customer.customer;
import static uk.co.benjiweber.expressions.caseclass.TriConstructorMatchReference.*;
import static uk.co.benjiweber.expressions.caseclass.BiConstructorMatchReference.*;
public class NestedDecompositionTest {

    @Test
    public void nested_decomposition_using_constructor_references() {
        Customer a = customer("Benji", "Weber", address("123 Some Road", "AB123CD"));
        String result = a.match().when(
            $(Customer::customer, "Benji", "Weber", $(Address::address, _, "AB123CD"))
        ).then(firstLine -> firstLine)
        ._("unknown");

        assertEquals("123 Some Road", result);
    }

    @Test
    public void nested_decomposition_using_constructor_references_no_match() {
        Customer a = customer("Benji", "Weber", address("123 Some Road", "AB123CD"));
        String result = a.match().when(
                $(Customer::customer, "Someone", "Else", $(Address::address, _, "AB123CD"))
        ).then(firstLine -> firstLine)
        ._("unknown");

        assertEquals("unknown", result);
    }

    @Test
    public void nested_decomposition_using_constructor_references_bi_match() {
        Customer a = customer("Benji", "Weber", address("123 Some Road", "AB123CD"));
        String result = a.match().when(
                $(Customer::customer, "Benji", "Weber", $(Address::address, _, _))
        ).then((firstLine, postCode) -> firstLine)
        ._("unknown");

        assertEquals("123 Some Road", result);
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
        String firstLine();
        String postCode();
        public static Address address(String firstLine, String postCode) {
            abstract class AddressValue extends Value<Address> implements Address {}
            return new AddressValue() {
                public String firstLine() { return firstLine; }
                public String postCode() { return postCode; }
            }.using(Address::firstLine, Address::postCode);
        }
    }
}

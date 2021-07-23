package com.github.lhrb.lenses;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LensTest {


    record Address (String street, String city, Integer zipCode) {}
    record Person (String firstName, String lastName, Address address) {}

    @Test
    public void test()
    {
        Person p = new Person("Peter", "Schneider", new Address("", "", 123));
        Lens<Person, Address> addressLens =
                Lens.create(Person::address, (person, address) -> new Person(person.firstName, person.lastName, address));
        Lens<Address, Integer> zipLens =
                Lens.create(Address::zipCode, (address, zipCode) -> new Address(address.street, address.city, zipCode));

        Lens<Person, Integer> personZip = addressLens.andThen(zipLens);


        Person withNewZip = personZip.set(p, 124);
        assertEquals(withNewZip.address.zipCode, 124);
        assertEquals(p.address.zipCode, 123);
    }

    @Test
    public void listOfPersons() {

        Lens<Person, Address> addressLens =
                Lens.create(Person::address, (person, address) -> new Person(person.firstName, person.lastName, address));
        Lens<Address, Integer> zipLens =
                Lens.create(Address::zipCode, (address, zipCode) -> new Address(address.street, address.city, zipCode));

        Lens<Person, Integer> personZip = addressLens.andThen(zipLens);

        Arrays.asList(
                new Person("Peter", "Schneider", new Address("", "", 2)),
                new Person("Ann", "Bizarre", new Address("", "", 4)),
                new Person("Joe", "Sloppy", new Address("", "", 5)));

    }
}
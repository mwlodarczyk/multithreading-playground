package com.mwlodarczyk.multithreading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

//Classes should be immutable unless there's a very good reason to make them mutable
// If a class cannot be made immutable, limit its mutability as much as possible.
public class S13_Immutable {

    //Mark the class final
    //Mark all the fields private and final
    //Do not change the state of the objects in any methods of the class
    //Force all the callers to construct an object of the class directly, i.e. do not use any setter methods
    public final class Person {

        private final int age;
        private final String name;
        private final Collection<String> friends;

        public Person(String name, int age, Collection<String> friends) {
            this.age = age;
            this.name = name;
            this.friends = new ArrayList(friends);
        }

        public String getName() {
            return this.name;
        }

        public int getAge() {
            return this.age;
        }

        public Collection<String> getFriends() {
            return Collections.unmodifiableCollection(this.friends);
        }
    }


}

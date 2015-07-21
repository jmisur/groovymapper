package groovymapper;

import groovy.transform.ToString;

@ToString
class Person {
    String name
	int age
	Gender gender
    String color

    enum Gender {
        MAN, WOMAN
    }
}

package groovymapper;

import groovy.transform.ToString;

@ToString(includeNames = true)
class Person {
    String name
	int age
	Gender gender
    String color

    enum Gender {
        MAN, WOMAN
    }
}

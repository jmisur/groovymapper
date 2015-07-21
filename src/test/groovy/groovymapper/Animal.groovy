package groovymapper

import groovy.transform.ToString;

@ToString(includeNames = true)
class Animal {
    String name
	BigDecimal age
	boolean male
	Integer height
    String color
}

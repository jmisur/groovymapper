package groovymapper

import groovy.transform.ToString;

@ToString
class Animal {
    String name
	BigDecimal age
	String sex
	int height
    String color

	void setSex(String sex) {
		this.sex = sex
	}

	void setName(String name) {
		this.name = name
	}

	void setHeight(int height) {
		this.height = height
	}
}

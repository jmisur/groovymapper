package groovymapper

import org.junit.Test

class MapperTest {

    @Test
    void test() {
        Person person = new Person(name: 'John', age: 30, gender: Person.Gender.MAN, color: 'white')
        Animal animal = new Animal()
        println doMap(person, animal)

        assert animal.age == 30
        assert animal.color == 'white'
        assert animal.height == 0
        assert animal.name == 'John'
        assert animal.sex == "MALE"
    }

    @Mapping
    def doMap(Person person, Animal animal) {
        person.map(animal, ['height']) { Person p ->
            age = new BigDecimal(p.age) // for some reason Intellij cannot assign int to BigDec
            sex = p.gender == Person.Gender.MAN ? "MALE" : "FEMALE"
        }
    }
}
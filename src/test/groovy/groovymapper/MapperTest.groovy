package groovymapper

import org.junit.Test

import static groovymapper.Person.Gender.MAN

class MapperTest {

    @Test
    void test() {
        Person person = new Person(name: 'John', age: 30, gender: MAN, color: 'red')
        println person
        Animal animal = new Animal()
        println animal
        println doMap(person, animal)

        assert animal.name == 'John'
        assert animal.age == 70
        assert animal.male == true
        assert animal.height == null
        assert animal.color == 'red'
    }

    @Mapping
    def doMap(Person person, Animal animal) {
        person.map(animal, ['height']) { Person p ->
            age = new BigDecimal(100 - p.age)
            male = p.gender == MAN
        }
    }
}
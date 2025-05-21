package com.example.demo.repositories;

import com.example.demo.integrationtest.testcontainers.AbstractIntegrationTest;
import com.example.demo.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;

    private Person person;

    @BeforeEach
    public void setup() {
        // given / arrange
        person = new Person("joao", "paulo", "maceio", "male", "joao@gmail.com");
    }
    @DisplayName("testGivenPersonObject_whenSave_ThenReturnSavedPerson")
    @Test
    void testGivenPersonObject_whenSave_ThenReturnSavedPerson() {
        //when / act
        Person savedPerson = repository.save(person);
        //then / assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @DisplayName("testGivenPersonList_whenFindAll_ThenReturnSavedPersonList")
    @Test
    void testGivenPersonList_whenFindAll_ThenReturnSavedPersonList() {
        Person person1 = new Person("paulo", "joao", "maceio", "male", "joao@gmail.com");
        repository.save(person);
        repository.save(person1);

        //when / act
        List<Person> personList = repository.findAll();

        //then / assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @DisplayName("testGivenPersonObject_whenFindById_ThenReturnSavedPersonObject")
    @Test
    void testGivenPersonObject_whenFindById_ThenReturnSavedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findById(person.getId()).get();

        //then / assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @DisplayName("testGivenPersonObject_whenFindByEmail_ThenReturnSavedPersonObject")
    @Test
    void testGivenPersonObject_whenFindByEmail_ThenReturnSavedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findByEmail(person.getEmail()).get();

        //then / assert
        assertNotNull(savedPerson);
        assertEquals(person.getEmail(), savedPerson.getEmail());
    }

    @DisplayName("Given Person Object when Update Person Then Return Updated Person Object")
    @Test
    void testGivenPersonObject_whenUpdatePerson_ThenReturnUpdatedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findByEmail(person.getEmail()).get();
        savedPerson.setFirstNome("claudin");
        savedPerson.setLastNome("do motor");

        Person updatedPerson = repository.save(savedPerson);

        //then / assert
        assertNotNull(updatedPerson);
        assertEquals("claudin", updatedPerson.getFirstNome());
        assertEquals("do motor", updatedPerson.getLastNome());
    }

    @DisplayName("Given Person Object when delete Person Then remover Person Object")
    @Test
    void testGivenPersonObject_whenDeletePerson_ThenRemovePerson() {
        repository.save(person);

        //when / act
       repository.deleteById(person.getId());

        Optional<Person> personOptional = repository.findById(person.getId());
        //then / assert
        assertTrue(personOptional.isEmpty());
    }

    @DisplayName("Given Person Object when findBy firstName and lastName Person Then return Person Object")
    @Test
    void testGivenPersonObject_whenFindByFirstAndLastName_ThenReturnSavedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findByJPQL(person.getFirstNome(), person.getLastNome());

        //then / assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @DisplayName("Given Person Object when findBy firstName and lastName when named param Person Then return Person Object")
    @Test
    void testGivenPersonObject_whenFindByFirstAndLastNameByNamedParam_ThenReturnSavedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findByJPQLNamedParameters(person.getFirstNome(), person.getLastNome());

        //then / assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @DisplayName("Given Person Object when findBy firstName and lastName when NativeSQL Person Then return Person Object")
    @Test
    void testGivenPersonObject_whenFindByFirstAndLastNameByNativeSQL_ThenReturnSavedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findByNativeSQL(person.getFirstNome(), person.getLastNome());

        //then / assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }

    @DisplayName("Given Person Object when findBy firstName and lastName when NativeSQL Named Param Person Then return Person Object")
    @Test
    void testGivenPersonObject_whenFindByFirstAndLastNameByNativeSQLNamedParam_ThenReturnSavedPersonObject() {
        repository.save(person);

        //when / act
        Person savedPerson = repository.findByNativeSQLNamedParameters(person.getFirstNome(), person.getLastNome());

        //then / assert
        assertNotNull(savedPerson);
        assertEquals(person.getId(), savedPerson.getId());
    }
}
package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @InjectMocks
    private PersonService service;

    private Person person;

    @BeforeEach
    void setup() {
        person = new Person("any", "any", "any", "any", "any@email.com");
    }

    @Test
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {
//        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        // \/ estrategia do bdd
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
//        given(repository.save(person)).willReturn(person);
        given(repository.save(any(Person.class))).willReturn(person); // como é um mock, é mais recomendavel utilizar uma abstracao do objeto, do que o proprio objeto

        Person savedPerson = service.create(person);

        assertNotNull(savedPerson);
        verify(repository).findByEmail(anyString()); // nesse caso, estamos testando a camada de servico, e so queremos garantir que os metodos certos do que estamos mockando(repository) estao sendo chamados
        verify(repository).save(any(Person.class)); // nesse caso, estamos testando a camada de servico, e so queremos garantir que os metodos certos do que estamos mockando(repository) estao sendo chamados
    }

    @Test
    void testGivenExistingEmail_WhenCreate_thenThrowException() {
        given(repository.findByEmail("any@email.com")).willReturn(Optional.of(person));

        assertThrows(ResourceNotFoundException.class, () -> service.create(person));
        verify(repository, never()).save(any(Person.class));
    }

    @Test
    void testGivenPersonList_WhenFindAllPerson_thenReturnPersonList() {
        given(repository.findAll()).willReturn(List.of(person));

        List<Person> personList = service.findAll();

        assertNotNull(personList);
        verify(repository).findAll();
    }

    @Test
    void testGivenPersonObject_WhenFindById_thenReturnPersonObject() {
        given(repository.findById(1L)).willReturn(Optional.of(person));

        Person personObject = service.findById(1L);

        assertNotNull(personObject);
        verify(repository).findById(1L);
    }

    @Test
    void testGivenPersonObject_whenFindBydId_thenThrowsExecepetion() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
        verify(repository).findById(1L);
    }

    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnPersonObject() {
        person.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.of(person));

        given(repository.save(any(Person.class))).willReturn(person);
        Person updatedPerson = service.update(person);

        assertNotNull(updatedPerson);
        verify(repository).findById(1L);
        verify(repository).save(person);
    }

    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnException() {
        person.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(person));
        verify(repository).findById(1L);
    }

    @Test
    void testGivenPersonId_whenDeletePerson_thenDoNothing() {
        person.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.of(person));
        willDoNothing().given(repository).delete(person);

        service.delete(person.getId());

        verify(repository).findById(1L);
        verify(repository).delete(person);
    }

    @Test
    void testGivenPersonId_whenDeletePerson_thenThrowException() {
        person.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(person.getId()));

        verify(repository).findById(1L);
    }
}
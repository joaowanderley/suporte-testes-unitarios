package com.example.demo.services;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PersonService {
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;

    public List<Person> findAll() {
        logger.info("buscando todas as pessoas");
        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("buscando uma pessoa");
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhum registro para esse ID"));
    }

    public Person create(Person person) {
        logger.info("criando uma pessoa");

        Optional<Person> savedPerson = repository.findByEmail(person.getEmail());
        if(savedPerson.isPresent()) {
            throw new ResourceNotFoundException("email já cadastrado");
        }
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("atualizando uma pessoa");

        var entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum registro para esse ID"));

        entity.setFirstNome(person.getFirstNome());
        entity.setLastNome(person.getLastNome());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(entity);
    }

    public void delete(Long id) {
        logger.info("apagando uma pessoa");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum registro para esse ID"));
        repository.delete(entity);
    }
}

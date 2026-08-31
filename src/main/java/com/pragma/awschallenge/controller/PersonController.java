package com.pragma.awschallenge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pragma.awschallenge.entities.Person;
import com.pragma.awschallenge.repository.PersonRepository;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    
    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseMapping>> listPersons() {
        List<Person> persons = personRepository.findAll();
        return ResponseEntity.ok(persons.stream()
                .map(PersonResponseMapping::fromPerson)
                .toList());
    }

    @PostMapping
    public ResponseEntity<PersonResponseMapping> createPerson(@RequestBody PersonRequestMapping personRequest) {
        Person person = personRequest.toPerson();
        Person savedPerson = personRepository.save(person);
        return ResponseEntity.ok(PersonResponseMapping.fromPerson(savedPerson));
    }

}


record PersonRequestMapping(String name, String email) {
    public Person toPerson() {
        Person person = new Person();
        person.setName(name);
        person.setEmail(email);
        return person;
    }
}

record PersonResponseMapping(Integer id, String name, String email) {
    public static PersonResponseMapping fromPerson(Person person) {
        return new PersonResponseMapping(person.getId(), person.getName(), person.getEmail());
    }
}

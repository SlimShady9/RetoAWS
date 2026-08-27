package com.pragma.awschallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pragma.awschallenge.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    
}

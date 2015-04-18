package com.cristian.mylab.repository;

import org.springframework.data.repository.CrudRepository;

import com.cristian.mylab.entity.Person;

public interface PersonRepository extends CrudRepository<Person, String> {

	Person findByName(String name);

	Iterable<Person> findByTeammatesName(String name);

}

package com.cristian.mylab;

import java.io.File;
import java.io.IOException;

import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.neo4j.graphdb.Transaction;

import com.cristian.mylab.entity.Person;
import com.cristian.mylab.repository.PersonRepository;

@SpringBootApplication
public class Neo4jTestApplication {
	
	@Autowired PersonRepository personRepository;

	@Autowired GraphDatabase graphDatabase;


	
	
	public void run(String... args) throws Exception {

		Person carlos = new Person("Carlos");
		Person cristian = new Person("Cristian");
		Person pepe = new Person("Pepe");

		
		Transaction tx = graphDatabase.beginTx();
		try {
			personRepository.save(carlos);
			personRepository.save(cristian);
			personRepository.save(pepe);

			// if cristian works with pepe and carlos
			
			cristian = personRepository.findByName(cristian.name);
			cristian.worksWith(carlos);
			cristian.worksWith(pepe);
			personRepository.save(cristian);

			//carlos works with pepe
			carlos = personRepository.findByName(carlos.name);
			carlos.worksWith(pepe);
				
			
			personRepository.save(pepe);
		


			Iterable<Person> findByTeammatesName = personRepository.findByTeammatesName("Cristian");
	
			System.out.println("Looking up who works with Greg...");
			for (Person person : findByTeammatesName) {
				System.out.println(person.name + " works with Greg.");
			}

			tx.success();
		} finally {
			tx.close();
		}
	}
	
	
    public static void main(String[] args) throws IOException {
    	FileUtils.deleteRecursively(new File("cristianNeo4jTest.db"));
        SpringApplication.run(Neo4jTestApplication.class, args);
    }
}

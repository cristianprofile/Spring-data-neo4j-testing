package demo;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.cristian.mylab.Neo4jTestApplication;
import com.cristian.mylab.entity.Person;
import com.cristian.mylab.repository.PersonRepository;
import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Neo4jTestApplication.class)
public class Neo4jTestApplicationTests {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	GraphDatabase graphDatabase;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Neo4jTemplate template;

	@Rollback(false)
	@BeforeTransaction
	public void cleanUpGraph() {
		Neo4jHelper.cleanDb(template);
	}

	@Test
	@Transactional
	public void contextLoads() throws IOException {
		Person carlos = new Person("Carlos");
		Person cristian = new Person("Cristian");
		Person pepe = new Person("Pepe");
		Person juanan = new Person("Juanan");
		
		personRepository.save(carlos);
		personRepository.save(cristian);
		personRepository.save(pepe);
		personRepository.save(juanan);

		// if cristian works with pepe and carlos

		cristian = personRepository.findByName(cristian.name);
		cristian.worksWith(carlos);
		cristian.worksWith(pepe);
		personRepository.save(cristian);

		// carlos works with pepe
		carlos = personRepository.findByName(carlos.name);
		carlos.worksWith(pepe);

		personRepository.save(carlos);
		
		
		juanan = personRepository.findByName(juanan.name);
		juanan.worksWith(carlos);
		
		personRepository.save(juanan);

		Iterable<Person> findByTeammatesName = personRepository
				.findByTeammatesName("Carlos");
		
		for (Person person : findByTeammatesName) {
			Assert.assertTrue(person.getName().equals(cristian.getName()) || 
					person.getName().equals(pepe.getName()) || 
					person.getName().equals(juanan.getName()));
		}
		
		juanan = personRepository.findByName(juanan.name);
		personRepository.delete(juanan);
		
		 Person findByName = personRepository.findByName("juanan");
		 Assert.assertNull(findByName);
	}

}

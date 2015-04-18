package demo;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.cristian.mylab.Neo4jTestApplication;
import com.cristian.mylab.entity.Person;
import com.cristian.mylab.repository.PersonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Neo4jTestApplication.class)
public class Neo4jTestApplicationTests {

	@Autowired PersonRepository personRepository;

	@Autowired GraphDatabase graphDatabase;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void contextLoads() throws IOException {
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
	
			logger.info("Looking up who works with Cristian...");
			for (Person person : findByTeammatesName) {
				logger.info("{} works with Greg.",person.name);
			}

			tx.success();
		} finally {
			tx.close();
		}
	}
	
	

}

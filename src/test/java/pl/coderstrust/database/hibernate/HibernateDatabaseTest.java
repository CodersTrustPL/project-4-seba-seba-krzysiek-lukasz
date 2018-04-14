package pl.coderstrust.database.hibernate;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HibernateDatabaseTest extends DatabaseTest {

  @Autowired
  HibernateDatabase database;

  @Autowired
  InvoiceRepository repository;

  @Override
  public Database getCleanDatabase() {
    repository.deleteAll();
    return database;
  }
}
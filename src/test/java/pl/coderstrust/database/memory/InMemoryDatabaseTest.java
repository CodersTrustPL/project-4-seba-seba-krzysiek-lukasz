package pl.coderstrust.database.memory;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;

public class InMemoryDatabaseTest extends DatabaseTest {

  @Override
  public Database getCleanDatabase() {
    return new InMemoryDatabase();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

}


package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;

public class InMemoryDatabaseTest extends DatabaseTest{

  @Override
  public Database getDatabase() {
    return new InMemoryDatabase();
  }

}

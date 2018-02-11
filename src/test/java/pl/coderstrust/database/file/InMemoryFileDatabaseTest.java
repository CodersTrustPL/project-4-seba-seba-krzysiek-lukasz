package pl.coderstrust.database.file;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.database.memory.InMemoryDatabase;

public class InMemoryFileDatabaseTest extends DatabaseTest {

  @Override
  public Database getDatabase(){
    return new InMemoryDatabase();
  }

}

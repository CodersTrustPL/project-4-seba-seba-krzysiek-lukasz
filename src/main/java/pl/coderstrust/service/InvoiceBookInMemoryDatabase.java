package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.database.memory.InMemoryDatabase;

public class InvoiceBookInMemoryDatabase extends InvoiceBook {

  @Override
  Database getDatabase() {
    return new InMemoryDatabase();
  }
}

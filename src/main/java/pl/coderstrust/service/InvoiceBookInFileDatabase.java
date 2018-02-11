package pl.coderstrust.service;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.inFileDatabase.InFileDatabase;

public class InvoiceBookInFileDatabase extends InvoiceBook {

  @Override
  Database getDatabase() {
    return new InFileDatabase();
  }
}

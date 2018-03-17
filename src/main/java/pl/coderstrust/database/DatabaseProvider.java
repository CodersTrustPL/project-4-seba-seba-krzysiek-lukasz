package pl.coderstrust.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.coderstrust.database.file.InFileDatabase;
import pl.coderstrust.database.memory.InMemoryDatabase;
import pl.coderstrust.database.multifile.MultiFileDatabase;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;


@Configuration
public class DatabaseProvider {

  @Value("${pl.coderstrust.database.MasterDatabase}")
  private String masterDatabaseType;

  @Value("${pl.coderstrust.database.FilterDatabase}")
  private String filterDatabaseType;

  @Bean
  public Database<Invoice> masterWithInvoices() {
    switch (masterDatabaseType) {
      case "inFile":
        return new InFileDatabase<>(Invoice.class);
      case "multiFile":
        return new MultiFileDatabase<>(Invoice.class);
      default:
        return new InMemoryDatabase<>(Invoice.class);
    }
  }

  @Bean
  public Database<Company> masterWithCompanies() {
    switch (masterDatabaseType) {
      case "inFile":
        return new InFileDatabase<>(Company.class);
      case "multiFile":
        return new MultiFileDatabase<>(Company.class);
      default:
        return new InMemoryDatabase<>(Company.class);
    }
  }


  @Bean
  public Database<Invoice> filterWithInvoices() {
    switch (filterDatabaseType) {
      case "inFile":
        return new InFileDatabase<>(Invoice.class);
      case "multiFile":
        return new MultiFileDatabase<>(Invoice.class);
      default:
        return new InMemoryDatabase<>(Invoice.class);
    }
  }

  @Bean
  public Database<Company> filterWithCompanies() {
    switch (filterDatabaseType) {
      case "inFile":
        return new InFileDatabase<>(Company.class);
      case "multiFile":
        return new MultiFileDatabase<>(Company.class);
      default:
        return new InMemoryDatabase<>(Company.class);
    }

  }
}

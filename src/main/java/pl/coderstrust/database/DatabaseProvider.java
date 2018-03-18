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
  private String masterDbType;

  @Value("${pl.coderstrust.database.FilterDatabase}")
  private String filterDbType;

  @Value("${pl.coderstrust.database.MasterDatabase.key}")
  private String masterDbKey;

  @Value("${pl.coderstrust.database.FilterDatabase.key}")
  private String filterDbKey;


  @Bean
  public Database<Invoice> masterWithInvoices() {
    switch (masterDbType) {
      case "inFile":
        return new InFileDatabase<>(Invoice.class, masterDbKey);
      case "multiFile":
        return new MultiFileDatabase<>(Invoice.class, masterDbKey);
      default:
        return new InMemoryDatabase<>(Invoice.class);
    }
  }

  @Bean
  public Database<Company> masterWithCompanies() {
    switch (masterDbType) {
      case "inFile":
        return new InFileDatabase<>(Company.class, filterDbKey);
      case "multiFile":
        return new MultiFileDatabase<>(Company.class, filterDbKey);
      default:
        return new InMemoryDatabase<>(Company.class);
    }
  }


  @Bean
  public Database<Invoice> filterWithInvoices() {
    switch (filterDbType) {
      case "inFile":
        return new InFileDatabase<>(Invoice.class, masterDbKey);
      case "multiFile":
        return new MultiFileDatabase<>(Invoice.class, filterDbKey);
      default:
        return new InMemoryDatabase<>(Invoice.class);
    }
  }

  @Bean
  public Database<Company> filterWithCompanies() {
    switch (filterDbType) {
      case "inFile":
        return new InFileDatabase<>(Company.class, filterDbKey);
      case "multiFile":
        return new MultiFileDatabase<>(Company.class, filterDbKey);
      default:
        return new InMemoryDatabase<>(Company.class);
    }
  }
}

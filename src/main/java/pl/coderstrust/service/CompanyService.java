package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;

import java.util.Optional;

@Service
public class CompanyService extends AbstractService<Company> {

  @Autowired
  public CompanyService(@Qualifier("companiesDatabase") Database<Company> dbCompanies) {
    super.entriesDb = dbCompanies;
  }

  @Override
  public boolean nipExist(String nip) {
    return entriesDb.getEntries().stream()
        .anyMatch(company -> company.getNip().equals(nip));
  }

  @Override
  public Optional<Company> getEntryByNip(String nip) {
    return entriesDb.getEntries().stream()
        .filter(company -> company.getNip().equals(nip))
        .findAny();
  }
}


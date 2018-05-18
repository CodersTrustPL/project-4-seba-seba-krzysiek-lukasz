package pl.coderstrust.database.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;

import java.util.List;

public class HibernateCompanyDatabase implements Database<Company> {

  @Autowired
  CompanyRepository companyRepository;

  @Override
  public long addEntry(Company entry) {
    companyRepository.findA
    return 0;
  }

  @Override
  public void deleteEntry(long id) {

  }

  @Override
  public Company getEntryById(long id) {
    return null;
  }

  @Override
  public void updateEntry(Company entry) {

  }

  @Override
  public List<Company> getEntries() {
    return null;
  }

  @Override
  public boolean idExist(long id) {
    return false;
  }
}

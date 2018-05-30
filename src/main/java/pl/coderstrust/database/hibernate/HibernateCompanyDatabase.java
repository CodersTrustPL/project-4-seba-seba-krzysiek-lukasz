package pl.coderstrust.database.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;

import java.util.List;

public class HibernateCompanyDatabase implements Database<Company> {

  private CompanyRepository companyRepository;

  @Autowired
  public HibernateCompanyDatabase(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
  }

  @Override
  public long addEntry(Company company) {
    return companyRepository.save(company).getId();
  }

  @Override
  public void deleteEntry(long id) {
    companyRepository.delete(id);
  }

  @Override
  public Company getEntryById(long id) {
    return companyRepository.findOne(id);
  }

  @Override
  public void updateEntry(Company company) {
    companyRepository.save(company);
  }

  @Override
  public List<Company> getEntries() {
    return companyRepository.findAll();
  }

  @Override
  public boolean idExist(long id) {
    return companyRepository.exists(id);
  }

}

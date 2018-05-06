package pl.coderstrust.database.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.util.List;

@Service
public class HibernateCompanyDatabase<T extends WithNameIdIssueDate> implements Database<T> {

  private final Logger logger = LoggerFactory.getLogger(HibernateCompanyDatabase.class);

  public HibernateCompanyDatabase() {
  }

  @Autowired
  CompanyRepository companyRepository;

  @Override
  public synchronized long addEntry(T entry) {

    Company company = (Company) entry;
    Company savedCompany;

    if (company.getId() != null && companyRepository.exists(company.getId())) {
      savedCompany = (Company) companyRepository.findOne(company.getId());
    } else {
      savedCompany = (Company) companyRepository.save(entry);
    }
    return savedCompany.getId();
  }

  @Override
  public void deleteEntry(long id) {
    if (!idExist(id)) {
      logger.warn(" from deleteEntry (hibernateDatabase): "
          + ExceptionMsg.COMPANY_NOT_EXIST);
      throw new DbException(ExceptionMsg.COMPANY_NOT_EXIST);
    } else {
      companyRepository.delete(id);
    }
  }

  @Override
  public T getEntryById(long id) {
    if (!idExist(id)) {
      logger.warn(" from getEntryByiD (hibernateDatabase): "
          + ExceptionMsg.COMPANY_NOT_EXIST);
      throw new DbException(ExceptionMsg.COMPANY_NOT_EXIST);
    } else {
      return (T) companyRepository.findOne(id);
    }
  }

  @Override
  public void updateEntry(T entry) {
    companyRepository.save(entry);
  }

  @Override
  public List<T> getEntries() {
    return (List<T>) companyRepository.findAll();
  }

  @Override
  public boolean idExist(long id) {
    return companyRepository.exists(id);
  }
}

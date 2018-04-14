package pl.coderstrust.database.hibernate;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.WithNameIdIssueDate;

@Service
public class HibernateDatabase<T extends WithNameIdIssueDate> implements Database<T> {

  private final Logger logger = LoggerFactory.getLogger(HibernateDatabase.class);

  public HibernateDatabase() {
  }

  @Autowired
  InvoiceRepository repository;

  @Override
  public long addEntry(T entry) {
    Invoice savedInvoice;
    if (idExist(entry.getId())) {
      entry.setId(repository.getMaxId()+1);
      savedInvoice = (Invoice) repository.save(entry);
    } else {
      savedInvoice = (Invoice) repository.save(entry);
    }
    return savedInvoice.getId();
  }

  @Override
  public void deleteEntry(long id) {
    if (!idExist(id)) {
      logger.warn(" from deleteEntry (hibernateDatabase): "
          + ExceptionMsg.INVOICE_NOT_EXIST);
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else repository.delete(id);
  }

  @Override
  public T getEntryById(long id) {
    if (!idExist(id)) {
      logger.warn(" from getEntryByiD (hibernateDatabase): "
          + ExceptionMsg.INVOICE_NOT_EXIST);
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }else return (T) repository.findOne(id);
  }

  @Override
  public void updateEntry(T entry) {
    repository.save(entry);
  }

  @Override
  public List<T> getEntries() {
    return (List<T>) repository.findAll();
  }

  @Override
  public boolean idExist(long id) {
    return repository.exists(id);
  }
}

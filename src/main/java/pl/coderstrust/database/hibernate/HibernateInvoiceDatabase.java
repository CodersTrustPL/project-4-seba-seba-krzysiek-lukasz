package pl.coderstrust.database.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.util.List;

@Repository
public class HibernateInvoiceDatabase<T extends WithNameIdIssueDate> implements Database<T> {

  private final Logger logger = LoggerFactory.getLogger(HibernateInvoiceDatabase.class);

  public HibernateInvoiceDatabase() {
  }

  @Autowired
  InvoiceRepository repository;

  @Autowired
  CompanyRepository companyRepository;


  @Override
  public long addEntry(T entry) {
//    entry.setId(null);
    Invoice invoice = (Invoice) entry;
    companyRepository.save(invoice.getBuyer());
    long buyerId = invoice.getBuyer().getId();
    companyRepository.save(invoice.getSeller());
    long sellerId = invoice.getBuyer().getId();

    invoice.getBuyer().setId(buyerId);
    invoice.getSeller().setId(sellerId);

    Invoice savedInvoice = (Invoice) repository.save(invoice);
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
    return repository.exists((Long)id);
  }
}

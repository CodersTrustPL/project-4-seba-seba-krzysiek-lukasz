package pl.coderstrust.database.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;

@Transactional
@Service
public class HibernateInvoiceDatabase<T extends WithNameIdIssueDate> implements Database<T>,
    Serializable {

  private final Logger logger = LoggerFactory.getLogger(HibernateInvoiceDatabase.class);

  public HibernateInvoiceDatabase() {
  }

  @Autowired
  InvoiceRepository invoiceRepository;

  @Autowired
  HibernateCompanyDatabase hibernateCompanyDatabase;


  @Override
  public synchronized long addEntry(T entry) {

    Invoice invoice = (Invoice) entry;
    Long buyerId = null;
    Long sellerId = null;

    if (hibernateCompanyDatabase.idExist(invoice.getBuyer().getId())) {
      invoice.setBuyer((Company) hibernateCompanyDatabase.getEntryById(invoice.getBuyer().getId()));
      invoice.getBuyer().setId(invoice.getBuyer().getId());
      invoice.getBuyer().getPayments();
    } else {
      buyerId = Long.valueOf(hibernateCompanyDatabase.addEntry(invoice.getBuyer()));
    }
    if (hibernateCompanyDatabase.idExist(invoice.getSeller().getId())) {
      invoice
          .setSeller((Company) hibernateCompanyDatabase.getEntryById(invoice.getSeller().getId()));
      invoice.getSeller().setId(invoice.getSeller().getId());
      invoice.getSeller().getPayments();
    } else {
      buyerId = Long.valueOf(hibernateCompanyDatabase.addEntry(invoice.getSeller()));
    }
    Invoice savedInvoice = (Invoice) invoiceRepository.saveAndFlush(invoice);
    return savedInvoice.getId();
  }

  @Override
  public void deleteEntry(long id) {
    if (!idExist(id)) {
      logger.warn(" from deleteEntry (hibernateDatabase): "
          + ExceptionMsg.INVOICE_NOT_EXIST);
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else invoiceRepository.delete(id);
  }

  @Override
  public T getEntryById(long id) {
    if (!idExist(id)) {
      logger.warn(" from getEntryByiD (hibernateDatabase): "
          + ExceptionMsg.INVOICE_NOT_EXIST);
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }else return (T) invoiceRepository.findOne(id);
  }

  @Override
  public void updateEntry(T entry) {
    Invoice invoice = (Invoice) entry;
    Long buyerId = null;
    Long sellerId = null;

    buyerId=hibernateCompanyDatabase.addEntry(invoice.getBuyer());
    sellerId=hibernateCompanyDatabase.addEntry(invoice.getSeller());
    invoice.getBuyer().setId(buyerId);
    invoice.getSeller().setId(sellerId);

    invoiceRepository.save(invoice);
  }

  @Override
  public List<T> getEntries() {
    return (List<T>) invoiceRepository.findAll();
  }

  @Override
  public boolean idExist(long id) {
    return invoiceRepository.exists((Long)id);
  }
}

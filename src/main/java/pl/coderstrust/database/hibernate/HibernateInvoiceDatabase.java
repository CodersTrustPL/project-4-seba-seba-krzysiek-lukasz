package pl.coderstrust.database.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

import java.util.List;

public class HibernateInvoiceDatabase implements Database<Invoice> {

  private InvoiceRepository invoiceRepository;

  private CompanyRepository companyRepository;

  @Autowired
  public HibernateInvoiceDatabase(InvoiceRepository invoiceRepository,
      CompanyRepository companyRepository) {
    this.invoiceRepository = invoiceRepository;
    this.companyRepository = companyRepository;
  }

  @Override
  public long addEntry(Invoice invoice) {
    return invoiceRepository.save(invoice).getId();
  }

  @Override
  public void deleteEntry(long id) {
    invoiceRepository.delete(id);
  }

  @Override
  public Invoice getEntryById(long id) {
    return invoiceRepository.findOne(id);
  }

  @Override
  public void updateEntry(Invoice invoice) {
    invoiceRepository.save(invoice);
  }

  @Override
  public List<Invoice> getEntries() {
    return invoiceRepository.findAll();
  }

  @Override
  public boolean idExist(long id) {
    return invoiceRepository.exists(id);
  }
}

package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

@Service
public class InvoiceService extends AbstractService<Invoice> {


  @Autowired
  public InvoiceService(@Qualifier("dbWithInvoices") Database<Invoice> dbInvoices) {
    super.entriesDb = dbInvoices;
  }
}

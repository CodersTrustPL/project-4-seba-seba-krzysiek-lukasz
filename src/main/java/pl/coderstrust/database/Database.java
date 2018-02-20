package pl.coderstrust.database;

import pl.coderstrust.model.Invoice;

import java.util.List;

public interface Database {

  long addInvoice(Invoice invoice);

  void deleteInvoice(long id);

  Invoice getInvoiceById(long id);

  void updateInvoice(Invoice invoice);

  List<Invoice> getInvoices();

  boolean idExist(long id);

}

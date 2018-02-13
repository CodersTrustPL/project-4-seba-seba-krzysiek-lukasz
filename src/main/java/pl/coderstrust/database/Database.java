package pl.coderstrust.database;

import pl.coderstrust.model.Invoice;

import java.util.List;

public interface Database {

  void addInvoice(Invoice invoice);

  void deleteInvoiceById(long id);

  Invoice getInvoiceById(long id);

  void updateInvoice(Invoice invoice);

  List<Invoice> getInvoices();

  void cleanDatabase();
}

package pl.coderstrust.database.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryDatabase implements Database {

  private List<Invoice> invoices = new ArrayList<>();

  @Override
  public void addInvoice(Invoice invoice) {
    invoices.add(invoice);
  }

  @Override
  public void deleteInvoiceById(long id) {
    invoices.remove(findListIndexByInvoiceId(id));
  }

  @Override
  public Invoice getInvoiceById(long id) {
    return invoices.get(findListIndexByInvoiceId(id));
  }

  //discuss this method
  @Override
  public void updateInvoice(Invoice invoice) {
    invoices.set(findListIndexByInvoiceId(invoice.getSystemId()), invoice);
  }

  @Override
  public List<Invoice> getInvoices() {
    return invoices;
  }

  private int findListIndexByInvoiceId(long id) {
    for (int i = 0; i < invoices.size(); i++) {
      if (invoices.get(i).getSystemId() == id) {
        return i;
      }
    }
    throw new NoSuchElementException("There is no invoice with id : " + id);
  }

}

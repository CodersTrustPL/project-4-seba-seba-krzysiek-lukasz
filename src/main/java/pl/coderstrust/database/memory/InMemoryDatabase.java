package pl.coderstrust.database.memory;

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
    invoices.remove(findIndexInListByInvoiceId(id));
  }

  @Override
  public Invoice getInvoiceById(long id) {
    return invoices.get(findIndexInListByInvoiceId(id));
  }

  //discuss this method
  @Override
  public void updateInvoice(Invoice invoice) {
    invoices.set(findIndexInListByInvoiceId(invoice.getSystemId()), invoice);
  }

  @Override
  public List<Invoice> getInvoices() {
    return invoices;
  }

  private int findIndexInListByInvoiceId(long id) {
    for (int i = 0; i < invoices.size(); i++) {
      if (invoices.get(i).getSystemId() == id) {
        return i;
      }
    }
    throw new NoSuchElementException("There is no invoice with id : " + id);
  }

  @Override
  public void cleanDatabase() {
    invoices.clear();
  }
}

package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase implements Database {

  private static final int INVALID_INDEX_VALUE = -1;
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
    try {
      return invoices.get(findIndexInListByInvoiceId(id));
    } catch (Exception e) {
      throw new InvoiceProcessingException("Required invoice does not exits.");
      //TODO dfsdfs
    }
  }


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
    return INVALID_INDEX_VALUE;
  }
}

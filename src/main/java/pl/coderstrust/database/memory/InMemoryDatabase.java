package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InMemoryDatabase implements Database {

  private static final int INVALID_INDEX_VALUE = -1;
  private List<Invoice> invoices = new ArrayList<>();
  private HashSet<Long> savedIds = new HashSet<>();

  @Override
  public void addInvoice(Invoice invoice) {
    invoices.add(invoice);
    savedIds.add(invoice.getSystemId());
  }

  @Override
  public void deleteInvoiceById(long id) {
    invoices.remove(findIndexInListByInvoiceId(id));
    savedIds.remove(id);
  }

  @Override
  public Invoice getInvoiceById(long id) {
    try {
      return invoices.get(findIndexInListByInvoiceId(id));
    } catch (Exception e) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
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

  @Override
  public boolean idExist(long id) {
    return savedIds.contains(id);
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

package pl.coderstrust.database.memory;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryDatabase implements Database {

  private static final int INITIAL_ID = 0;
  private static final int INCREMENT_ID = 1;


  private HashMap<Long, Invoice> invoices = new HashMap<>();
  long lastId = INITIAL_ID;

  @Override
  public long addInvoice(Invoice invoice) {
    invoice.setId(getNextId());
    invoices.put(invoice.getId(), invoice);
    return invoice.getId();
  }

  private long getNextId() {
    lastId += INCREMENT_ID;
    return lastId;
  }

  @Override
  public void deleteInvoice(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }

    invoices.remove(id);
  }

  @Override
  public Invoice getInvoiceById(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }
    return invoices.get(id);
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    if (!idExist(invoice.getId())) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }

    invoices.replace(invoice.getId(), invoice);
  }

  @Override
  public ArrayList<Invoice> getInvoices() {
    return new ArrayList<>(invoices.values());
  }

  @Override
  public boolean idExist(long id) {
    return invoices.containsKey(id);
  }
}
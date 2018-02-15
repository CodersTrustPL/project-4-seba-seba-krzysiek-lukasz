package pl.coderstrust.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.PaymentState;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceBookTestWithMocks {

  @Mock
  private Database database;

  @Mock
  private Invoice invoice;

  @Mock
  private Company company;

  @Mock
  private List<InvoiceEntry> product;

  @Test
  public void shouldAddInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    verify(database, times(0)).addInvoice(invoice);
    invoiceBook.addInvoice(invoice);
    verify(database, times(1)).addInvoice(invoice);
  }

  @Test
  public void shouldRemoveInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    Invoice invoice1 = new Invoice("id_1");
    when(database.getInvoices()).thenReturn(Collections.singletonList(invoice1));

    invoiceBook.removeInvoice("id_1");
    verify(database, times(1)).deleteInvoiceById(anyLong());
  }

  @Test
  public void shouldFindInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    Invoice invoice1 = new Invoice("id_1");
    when(database.getInvoices()).thenReturn(Collections.singletonList(invoice1));

    invoiceBook.findInvoice("id_1");
    verify(database, times(1)).getInvoiceById(anyLong());
  }

  @Test
  public void shouldUpdateInvoice() {
    InvoiceBook invoiceBook = new InvoiceBook(database);
    Invoice invoice1 = new Invoice("id_1", company, company, LocalDate.of(2018, 3, 15),
        LocalDate.of(2018, 4, 15), product, PaymentState.NOT_PAID);
    when(database.getInvoices()).thenReturn(Collections.singletonList(invoice1));

    invoiceBook.updateInovoice(invoice1);
    verify(database, times(1)).addInvoice(invoice1);
  }
}
package pl.coderstrust.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceBookTest {

  @Mock
  private Database database;

  @Mock
  private Invoice invoice;

  @InjectMocks
  private InvoiceBook invoiceBook;

  @Test
  public void shouldAddInvoice() {
    //given
    when(database.addInvoice(invoice)).thenReturn(1L);
    //when
    invoiceBook.addInvoice(invoice);
    //then
    assertThat(invoiceBook.addInvoice(invoice), is(equalTo(1L)));
  }

  @Test
  public void shouldRemoveInvoice() {
    //given
    doNothing().when(database).deleteInvoice(1);
    //when
    invoiceBook.deleteInvoice(1);
    //then
    verify(database).deleteInvoice(1);
  }

  @Test
  public void shouldFindInvoice() {
    //given
    when(database.getInvoiceById(1)).thenReturn(invoice);
    //when
    invoiceBook.findInvoice(1);
    //then
    verify(database).getInvoiceById(1);
  }

  @Test
  public void shouldUpdateInvoice() {
    //given
    doNothing().when(database).updateInvoice(invoice);
    //when
    invoiceBook.updateInvoice(invoice);
    //then
    verify(database).updateInvoice(invoice);
  }

  @Test
  public void shouldGetInvoice() {
    //given
    when(database.getInvoices()).thenReturn(Collections.singletonList(invoice));
    //when
    invoiceBook.getInvoices();
    //then
    verify(database).getInvoices();
  }

  @Test
  public void shouldCheckIdExist() {
    //given
    when(database.idExist(anyLong())).thenReturn(true);
    //when
    invoiceBook.idExist(1);
    //then
    verify(database).idExist(1);
  }

  @Test
  public void shouldGetInvoiceByDate() {
    //given
    LocalDate date = LocalDate.of(2018, 3, 15);
    Invoice invoiceDateTest = new Invoice();
    invoiceDateTest.setIssueDate(date);
    when(database.getInvoices()).thenReturn(Collections.singletonList(invoiceDateTest));
    //when
    invoiceBook.getInvoiceByDate(date, date);
    //then
    assertThat(invoiceBook.getInvoices().iterator().next().getIssueDate(), is(equalTo(date)));
  }
}
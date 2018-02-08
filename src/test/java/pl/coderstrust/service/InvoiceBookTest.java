package pl.coderstrust.service;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.model.Invoice;

public class InvoiceBookTest {

  InvoiceBook testBook;
  TestCasesGenerator generator;

  @Before
  public void initializeInvoiceBook() {
    testBook = new InvoiceBook();
    generator = new TestCasesGenerator();
  }

  @Test
  public void shouldAddLargeNumberOfInvoices() {

    int invoiceEntriesCount = 100;
    int invoicesCount = 1_000;
    Invoice[] invoices = new Invoice[invoicesCount];
    String[] invoiceIds = new String[invoicesCount];
    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    Invoice[] output = new Invoice[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      output[i] = testBook.findInvoice(invoiceIds[i]);
    }
    assertArrayEquals(output, invoices);
  }


}


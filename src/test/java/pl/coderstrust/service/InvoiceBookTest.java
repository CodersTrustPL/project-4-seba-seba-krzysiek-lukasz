package pl.coderstrust.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.PaymentState;
import pl.coderstrust.model.Product;

public class InvoiceBookTest {

  private InvoiceBook testBook;
  private TestCasesGenerator generator;

  @Before
  public void initializeInvoiceBook() {
    testBook = new InvoiceBook();
    generator = new TestCasesGenerator();
  }

  @Test
  public void shouldAddLargeNumberOfInvoices() {

    int invoiceEntriesCount = 100;
    int invoicesCount = 1_000;
    Invoice invoices[] = new Invoice[invoicesCount];
    String invoiceIds[] = new String[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    Invoice output[] = new Invoice[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      output[i] = testBook.findInvoice(invoiceIds[i]);
    }

    assertArrayEquals(output, invoices);
  }

  @Test
  public void shouldAddAndThenRemoveInvoices() {

    int invoiceEntriesCount = 100;
    int invoicesCount = 1_000;
    Invoice invoices[] = new Invoice[invoicesCount];
    String invoiceIds[] = new String[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    Invoice output[] = new Invoice[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      output[i] = testBook.findInvoice(invoiceIds[i]);
    }
    assertArrayEquals(invoices, output);

    for (int i = 0; i < invoicesCount; i++) {
      testBook.removeInvoice(invoiceIds[i]);
    }
    for (int i = 0; i < invoicesCount; i++) {
      try {
        assertTrue(testBook.findInvoice(invoiceIds[i]) == null);
      } catch (NoSuchElementException ignored) {
      }
    }
  }

  @Test
  public void shouldUpdateInvoices() {
    Invoice updateInvoice;
    Company buyer;
    Company seller;
    Product product;

    int invoiceEntriesCount = 10;
    int invoicesCount = 1_00;
    Invoice invoices[] = new Invoice[invoicesCount];
    String invoiceIds[] = new String[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    for (int i = 0; i < invoicesCount; i++) {
      updateInvoice = testBook.findInvoice(invoiceIds[i]);
      buyer = testBook.findInvoice(invoiceIds[i]).getBuyer();
      seller = testBook.findInvoice(invoiceIds[i]).getSeller();
      product = testBook.findInvoice(invoiceIds[i]).getProducts().iterator().next().getProduct();

      String newBuyerAddress = "new_address_" + i;
      String newBuyerName = "new_buyer_name" + i;
      String newSellerCity = "new_seller_city" + i;
      String newProductName = "apple_" + i;
      LocalDate newIssueDate = LocalDate.of(2018, 5, 24);
      PaymentState newPaymentState = PaymentState.PAID;

      buyer.setAddress(newBuyerAddress);
      buyer.setName(newBuyerName);
      seller.setCity(newSellerCity);
      product.setName(newProductName);

      updateInvoice.setPaymentState(newPaymentState);
      updateInvoice.setIssueDate(newIssueDate);
      updateInvoice.setBuyer(buyer);
      updateInvoice.setSeller(seller);
      testBook.updateInovoice(updateInvoice);

      assertTrue(
          testBook.findInvoice(invoiceIds[i]).getBuyer().getAddress().equals(newBuyerAddress));
      assertTrue(testBook.findInvoice(invoiceIds[i]).getBuyer().getName().equals(newBuyerName));
      assertTrue(testBook.findInvoice(invoiceIds[i]).getSeller().getCity().equals(newSellerCity));
      assertTrue(testBook.findInvoice(invoiceIds[i]).getProducts().iterator().next().getProduct()
          .getName().equals(newProductName));
      assertTrue(testBook.findInvoice(invoiceIds[i]).getIssueDate().equals(newIssueDate));
      assertTrue(testBook.findInvoice(invoiceIds[i]).getPaymentState().equals(newPaymentState));
    }


  }
}


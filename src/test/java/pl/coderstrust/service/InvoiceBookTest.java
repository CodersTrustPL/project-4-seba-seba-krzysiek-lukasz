package pl.coderstrust.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.PaymentState;
import pl.coderstrust.model.Product;
import pl.coderstrust.model.Vat;

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

    int invoiceEntriesCount = 1000;
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
      buyer = updateInvoice.getBuyer();
      seller = updateInvoice.getSeller();
      product = updateInvoice.getProducts().iterator().next().getProduct();

      String newBuyerName = "new_buyer_name_" + i,
          newBuyerAddress = "new_buyer_address_" + i,
          newBuyerCity = "new_buyer_city_" + i,
          newBuyerZipCode = "new_buyer_zip_code_" + i,
          newBuyerNip = "new_buyer_nip_" + i,
          newBuyerBankAcc = "new_buyer_bank_account_" + i;

      buyer.setName(newBuyerName);
      buyer.setAddress(newBuyerAddress);
      buyer.setCity(newBuyerCity);
      buyer.setZipCode(newBuyerZipCode);
      buyer.setNip(newBuyerNip);
      buyer.setBankAccoutNumber(newBuyerBankAcc);

      String newSellerName = "new_seller_name_" + i,
          newSellerAddress = "new_seller_address_" + i,
          newSellerCity = "new_seller_city_" + i,
          newSellerZipCode = "new_seller_zip_code_" + i,
          newSellerNip = "new_seller_nip_" + i,
          newSellerBankAcc = "new_seller_bank_account_" + i;

      seller.setName(newSellerName);
      seller.setAddress(newSellerAddress);
      seller.setCity(newSellerCity);
      seller.setZipCode(newSellerZipCode);
      seller.setNip(newSellerNip);
      seller.setBankAccoutNumber(newSellerBankAcc);

      String newProductName = "new_product_name_" + i,
          newProductDescription = "new_product_description_" + i;

      product.setName(newProductName);
      product.setDescription(newProductDescription);
      product.setNetValue(BigDecimal.valueOf(i));
      product.setVatRate(Vat.VAT_7);

      LocalDate newIssueDate = LocalDate.of(2018, 5, 24);
      LocalDate newPaymentDate = newIssueDate.plusDays(15);
      PaymentState newPaymentState = PaymentState.PAID;

      updateInvoice.setIssueDate(newIssueDate);
      updateInvoice.setPaymentDate(newPaymentDate);
      updateInvoice.setPaymentState(newPaymentState);

      testBook.updateInovoice(updateInvoice);

      assertTrue(buyer.getName().equals(newBuyerName)
          && buyer.getAddress().equals(newBuyerAddress)
          && buyer.getCity().equals(newBuyerCity)
          && buyer.getZipCode().equals(newBuyerZipCode)
          && buyer.getNip().equals(newBuyerNip)
          && buyer.getBankAccoutNumber().equals(newBuyerBankAcc));

      assertTrue(seller.getName().equals(newSellerName)
          && seller.getAddress().equals(newSellerAddress)
          && seller.getCity().equals(newSellerCity)
          && seller.getZipCode().equals(newSellerZipCode)
          && seller.getNip().equals(newSellerNip)
          && seller.getBankAccoutNumber().equals(newSellerBankAcc));

      assertTrue(product.getName().equals(newProductName)
          && product.getDescription().equals(newProductDescription)
          && product.getNetValue().equals(BigDecimal.valueOf(i))
          && product.getVatRate().equals(Vat.VAT_7));

      assertTrue(updateInvoice.getIssueDate().equals(newIssueDate)
          && updateInvoice.getPaymentDate().equals(newPaymentDate)
          && updateInvoice.getPaymentState().equals(newPaymentState));
    }
  }
}


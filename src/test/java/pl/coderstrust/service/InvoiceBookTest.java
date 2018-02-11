package pl.coderstrust.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.PaymentState;
import pl.coderstrust.model.Product;
import pl.coderstrust.model.Vat;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

public class InvoiceBookTest {

  private InvoiceBook testBook;
  private TestCasesGenerator generator;
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Initializes objects before tests.
   */
  @Before
  public void initializeInvoiceBook() {
    testBook = new InvoiceBook();
    generator = new TestCasesGenerator();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
  }

  @Rule
  public ExpectedException expectedExceptionFromFind = ExpectedException.none();

  @Test
  public void shouldAddLargeNumberOfInvoices() {
    int invoiceEntriesCount = 1000;
    int invoicesCount = 1_000;

    Invoice[] invoices = new Invoice[invoicesCount];
    String[] invoiceIds = new String[invoicesCount];
    Invoice[] output = new Invoice[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    for (int i = 0; i < invoicesCount; i++) {
      output[i] = testBook.findInvoice(invoiceIds[i]);
    }

    assertArrayEquals(output, invoices);
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void shouldAddAndThenRemoveInvoices() {

    int invoiceEntriesCount = 1000;
    int invoicesCount = 10;

    Invoice[] invoices = new Invoice[invoicesCount];
    String[] invoiceIds = new String[invoicesCount];
    Invoice[] output = new Invoice[invoicesCount];

    for (int i = 0; i < invoicesCount; i++) {
      invoices[i] = generator.getTestInvoice(i, invoiceEntriesCount);
      testBook.addInvoice(invoices[i]);
      invoiceIds[i] = invoices[i].getVisibleId();
    }

    for (int i = 0; i < invoicesCount; i++) {
      output[i] = testBook.findInvoice(invoiceIds[i]);
    }
    assertArrayEquals(invoices, output);

    for (int i = 0; i < invoicesCount; i++) {
      testBook.removeInvoice(invoiceIds[i]);
    }
    for (int i = 0; i < invoicesCount; i++) {
      expectedException.expect(NoSuchElementException.class);
      testBook.findInvoice(invoiceIds[i]);
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
    Invoice[] invoices = new Invoice[invoicesCount];
    String[] invoiceIds = new String[invoicesCount];

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

      String newBuyerName = "new_buyer_name_" + i;
      String newBuyerAddress = "new_buyer_address_" + i;
      String newBuyerCity = "new_buyer_city_" + i;
      String newBuyerZipCode = "new_buyer_zip_code_" + i;
      String newBuyerNip = "new_buyer_nip_" + i;
      String newBuyerBankAcc = "new_buyer_bank_account_" + i;

      buyer.setName(newBuyerName);
      buyer.setAddress(newBuyerAddress);
      buyer.setCity(newBuyerCity);
      buyer.setZipCode(newBuyerZipCode);
      buyer.setNip(newBuyerNip);
      buyer.setBankAccoutNumber(newBuyerBankAcc);

      String newSellerName = "new_seller_name_" + i;
      String newSellerAddress = "new_seller_address_" + i;
      String newSellerCity = "new_seller_city_" + i;
      String newSellerZipCode = "new_seller_zip_code_" + i;
      String newSellerNip = "new_seller_nip_" + i;
      String newSellerBankAcc = "new_seller_bank_account_" + i;

      seller.setName(newSellerName);
      seller.setAddress(newSellerAddress);
      seller.setCity(newSellerCity);
      seller.setZipCode(newSellerZipCode);
      seller.setNip(newSellerNip);
      seller.setBankAccoutNumber(newSellerBankAcc);

      String newProductName = "new_product_name_" + i;
      String newProductDescription = "new_product_description_" + i;

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

      assertThat(buyer.getName(), is(equalTo(newBuyerName)));
      assertThat(buyer.getAddress(), is(equalTo(newBuyerAddress)));
      assertThat(buyer.getCity(), is(equalTo(newBuyerCity)));
      assertThat(buyer.getZipCode(), is(equalTo(newBuyerZipCode)));
      assertThat(buyer.getNip(), is(equalTo(newBuyerNip)));
      assertThat(buyer.getBankAccoutNumber(), is(equalTo(newBuyerBankAcc)));

      assertThat(seller.getName(), is(equalTo(newSellerName)));
      assertThat(seller.getAddress(), is(equalTo(newSellerAddress)));
      assertThat(seller.getCity(), is(equalTo(newSellerCity)));
      assertThat(seller.getZipCode(), is(equalTo(newSellerZipCode)));
      assertThat(seller.getNip(), is(equalTo(newSellerNip)));
      assertThat(seller.getBankAccoutNumber(), is(equalTo(newSellerBankAcc)));

      assertThat(product.getName(), is(equalTo(newProductName)));
      assertThat(product.getDescription(), is(equalTo(newProductDescription)));
      assertThat(product.getNetValue(), is(equalTo(BigDecimal.valueOf(i))));
      assertThat(product.getVatRate(), is(equalTo(Vat.VAT_7)));

      assertThat(updateInvoice.getIssueDate(), is(equalTo(newIssueDate)));
      assertThat(updateInvoice.getPaymentDate(), is(equalTo(newPaymentDate)));
      assertThat(updateInvoice.getPaymentState(), is(equalTo(newPaymentState)));
    }
  }
}
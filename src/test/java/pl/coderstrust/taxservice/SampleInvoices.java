package pl.coderstrust.taxservice;

import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBuilder;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Product;
import pl.coderstrust.model.ProductBuilder;
import pl.coderstrust.model.Vat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SampleInvoices {

  private List<Invoice> invoiceListDateInRange = new ArrayList<>();
  private List<Invoice> invoiceListOutOfDate = new ArrayList<>();
  private List<Invoice> invoiceListWholeDates = new ArrayList<>();

  public List<Invoice> listOfInvoicesInDateRange() {
    invoiceListDateInRange.add(sampleInvoice1());
    invoiceListDateInRange.add(sampleInvoice2());
    invoiceListDateInRange.add(sampleInvoice3());
    invoiceListDateInRange.add(sampleInvoice4());

    return invoiceListDateInRange;
  }

  public List<Invoice> listOfInvoicesOutOfDate() {
    invoiceListOutOfDate.add(sampleInvoiceOutOfDate1());
    invoiceListOutOfDate.add(sampleInvoiceOutOfDate2());
    invoiceListOutOfDate.add(sampleInvoiceOutOfDate3());
    invoiceListOutOfDate.add(sampleInvoiceOutOfDate4());

    return invoiceListOutOfDate;
  }

  public List<Invoice> listOfinvoicesWholeDates() {
    invoiceListWholeDates.add(sampleInvoice1());
    invoiceListWholeDates.add(sampleInvoiceOutOfDate1());
    invoiceListWholeDates.add(sampleInvoice2());
    invoiceListWholeDates.add(sampleInvoiceOutOfDate2());
    invoiceListWholeDates.add(sampleInvoice3());
    invoiceListWholeDates.add(sampleInvoiceOutOfDate3());
    invoiceListWholeDates.add(sampleInvoice4());
    invoiceListWholeDates.add(sampleInvoiceOutOfDate4());
    return invoiceListWholeDates;
  }


  private Invoice sampleInvoice1() {
    Product product;
    ProductBuilder productBuilder = new ProductBuilder("apple", 1000);
    productBuilder.setVatRate(Vat.VAT_23);
    product = productBuilder.build();

    Product product1;
    ProductBuilder productBuilder8 = new ProductBuilder("pineapple", 500);
    productBuilder8.setVatRate(Vat.VAT_23);
    product1 = productBuilder8.build();

    InvoiceEntry invoiceEntry = new InvoiceEntry(product, 5);
    InvoiceEntry invoiceEntryTwo = new InvoiceEntry(product1, 10);

    InvoiceBuilder invoiceBuilder = new InvoiceBuilder(1, "amth", "My company");
    invoiceBuilder.setIssueDate(LocalDate.of(2018, 2, 22));
    invoiceBuilder.setProducts(Arrays.asList(invoiceEntry, invoiceEntryTwo));

    return invoiceBuilder.build();
  }

  private Invoice sampleInvoice2() {
    Product product;

    ProductBuilder productBuilder4 = new ProductBuilder("apple", 300);
    productBuilder4.setVatRate(Vat.VAT_23);
    product = productBuilder4.build();

    InvoiceEntry invoiceEntry4 = new InvoiceEntry(product, 11);

    InvoiceBuilder invoiceBuilder4 = new InvoiceBuilder(1, "amth", "My company");
    invoiceBuilder4.setIssueDate(LocalDate.of(2018, 2, 22));
    invoiceBuilder4.setProducts(Collections.singletonList(invoiceEntry4));

    return invoiceBuilder4.build();
  }

  private Invoice sampleInvoice3() {
    Product product;

    ProductBuilder productBuilder1 = new ProductBuilder("apple", 100);
    productBuilder1.setVatRate(Vat.VAT_23);
    product = productBuilder1.build();

    InvoiceEntry invoiceEntry1 = new InvoiceEntry(product, 3);

    InvoiceBuilder invoiceBuilder1 = new InvoiceBuilder(2, "My company", "other");
    invoiceBuilder1.setIssueDate(LocalDate.of(2018, 2, 22));
    invoiceBuilder1.setProducts(Collections.singletonList(invoiceEntry1));

    return invoiceBuilder1.build();
  }

  private Invoice sampleInvoice4() {
    Product product;

    ProductBuilder productBuilder2 = new ProductBuilder("apple", 1000);
    productBuilder2.setVatRate(Vat.VAT_23);
    product = productBuilder2.build();

    InvoiceEntry invoiceEntry1 = new InvoiceEntry(product, 1);
    InvoiceEntry invoiceEntry2 = new InvoiceEntry(product, 2);

    InvoiceBuilder invoiceBuilder2 = new InvoiceBuilder(3, "My company", "other");
    invoiceBuilder2.setIssueDate(LocalDate.of(2018, 2, 22));
    invoiceBuilder2.setProducts(Arrays.asList(invoiceEntry2, invoiceEntry1));

    return invoiceBuilder2.build();
  }

  private Invoice sampleInvoiceOutOfDate1() {
    Product product;
    ProductBuilder productBuilder = new ProductBuilder("apple", 1000);
    productBuilder.setVatRate(Vat.VAT_23);
    product = productBuilder.build();

    Product product1;
    ProductBuilder productBuilder8 = new ProductBuilder("pineapple", 500);
    productBuilder8.setVatRate(Vat.VAT_23);
    product1 = productBuilder8.build();

    InvoiceEntry invoiceEntry = new InvoiceEntry(product, 5);
    InvoiceEntry invoiceEntryTwo = new InvoiceEntry(product1, 10);

    InvoiceBuilder invoiceBuilder = new InvoiceBuilder(1, "amth", "My company");
    invoiceBuilder.setIssueDate(LocalDate.of(2018, 1, 22));
    invoiceBuilder.setProducts(Arrays.asList(invoiceEntry, invoiceEntryTwo));

    return invoiceBuilder.build();
  }

  private Invoice sampleInvoiceOutOfDate2() {
    Product product;

    ProductBuilder productBuilder4 = new ProductBuilder("apple", 300);
    productBuilder4.setVatRate(Vat.VAT_23);
    product = productBuilder4.build();

    InvoiceEntry invoiceEntry4 = new InvoiceEntry(product, 11);

    InvoiceBuilder invoiceBuilder4 = new InvoiceBuilder(1, "amth", "My company");
    invoiceBuilder4.setIssueDate(LocalDate.of(2018, 5, 22));
    invoiceBuilder4.setProducts(Collections.singletonList(invoiceEntry4));

    return invoiceBuilder4.build();
  }

  private Invoice sampleInvoiceOutOfDate3() {
    Product product;

    ProductBuilder productBuilder1 = new ProductBuilder("apple", 100);
    productBuilder1.setVatRate(Vat.VAT_23);
    product = productBuilder1.build();

    InvoiceEntry invoiceEntry1 = new InvoiceEntry(product, 3);

    InvoiceBuilder invoiceBuilder1 = new InvoiceBuilder(2, "My company", "other");
    invoiceBuilder1.setIssueDate(LocalDate.of(2018, 1, 22));
    invoiceBuilder1.setProducts(Collections.singletonList(invoiceEntry1));

    return invoiceBuilder1.build();
  }

  private Invoice sampleInvoiceOutOfDate4() {
    Product product;

    ProductBuilder productBuilder2 = new ProductBuilder("apple", 1000);
    productBuilder2.setVatRate(Vat.VAT_23);
    product = productBuilder2.build();

    InvoiceEntry invoiceEntry1 = new InvoiceEntry(product, 1);
    InvoiceEntry invoiceEntry2 = new InvoiceEntry(product, 2);

    InvoiceBuilder invoiceBuilder2 = new InvoiceBuilder(5, "My company", "other");
    invoiceBuilder2.setIssueDate(LocalDate.of(2018, 5, 22));
    invoiceBuilder2.setProducts(Arrays.asList(invoiceEntry2, invoiceEntry1));

    return invoiceBuilder2.build();
  }
}

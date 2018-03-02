package pl.coderstrust.e2e.testHelpers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.model.InvoiceEntry;
import pl.coderstrust.e2e.model.PaymentState;
import pl.coderstrust.e2e.model.Product;
import pl.coderstrust.e2e.model.Vat;

public class TestCasesGenerator {


  public Invoice getTestInvoice(int invoiceNumber, int entriesCount) {
    LocalDate dateIssue = LocalDate.of(2018, 10, 1);

    return Invoice.builder()
        .id(invoiceNumber)
        .invoiceName("idVisible_" + Integer.toString(invoiceNumber))
        .buyer(getTestCompany(invoiceNumber, "buyer_"))
        .seller(getTestCompany(invoiceNumber, "seller_"))
        .issueDate(dateIssue)
        .paymentDate(dateIssue.plusDays(15))
        .products(getTestEntries(invoiceNumber, entriesCount))
        .paymentState(PaymentState.NOT_PAID)
        .build();
  }

  public Company getTestCompany(int invoiceNumber, String prefix) {
    return Company.builder()
        .name(prefix + "name_" + Integer.toString(invoiceNumber))
        .address(prefix + "address_" + Integer.toString(invoiceNumber))
        .city(prefix + "city_" + Integer.toString(invoiceNumber))
        .zipCode(prefix + "zipCode_" + Integer.toString(invoiceNumber))
        .nip(prefix + "nip_" + Integer.toString(invoiceNumber))
        .bankAccoutNumber(prefix + "bankAccoutNumber_" + Integer.toString(invoiceNumber))
        .build();
  }

  public List<InvoiceEntry> getTestEntries(int invoiceNumber, int productsCount) {

    ArrayList<InvoiceEntry> entries = new ArrayList<>(productsCount);
    for (int i = 0; i < productsCount; i++) {
      entries.add(new InvoiceEntry(getTestProduct(invoiceNumber, i), i));
    }
    return entries;
  }


  public Product getTestProduct(int invoiceNumber, int productCount) {
    String name = "name_" + Integer.toString(invoiceNumber) + "_" + Integer.toString(productCount);
    return Product.builder()
        .name(name)
        .netValue(BigDecimal.valueOf(invoiceNumber))
        .vatRate(Vat.VAT_23)
        .description(name + "_" + "description_" + Integer.toString(invoiceNumber))
        .build();
  }
}

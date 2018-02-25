package pl.coderstrust.e2e.testHelpers;

import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.CompanyBuilder;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.model.InvoiceBuilder;
import pl.coderstrust.e2e.model.InvoiceEntry;
import pl.coderstrust.e2e.model.PaymentState;
import pl.coderstrust.e2e.model.Product;
import pl.coderstrust.e2e.model.ProductBuilder;
import pl.coderstrust.e2e.model.Vat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestCasesGenerator {


  public Invoice getTestInvoice(int invoiceNumber, int entriesCount) {

    String idVisible = "idVisible_" + Integer.toString(invoiceNumber);
    Company buyer = getTestCompany(invoiceNumber, "buyer_");
    Company seller = getTestCompany(invoiceNumber, "seller_");
    InvoiceBuilder builder = new InvoiceBuilder(invoiceNumber, buyer.getName(), seller.getName());
    builder.setVisibleId(idVisible);
    builder.setBuyer(buyer);
    builder.setSeller(seller);
    LocalDate dateIssue = LocalDate.of(2018, 10, 1);
    builder.setIssueDate(dateIssue);
    builder.setPaymentDate(dateIssue.plusDays(15));
    builder.setProducts(getTestEntries(invoiceNumber, entriesCount));
    builder.setPaymentState(PaymentState.NOT_PAID);

    return builder.build();
  }

  public Company getTestCompany(int invoiceNumber, String prefix) {
    String name = prefix + "name_" + Integer.toString(invoiceNumber);
    CompanyBuilder builder = new CompanyBuilder(name);
    builder.setAddress(prefix + "address_" + Integer.toString(invoiceNumber));
    builder.setCity(prefix + "city_" + Integer.toString(invoiceNumber));
    builder.setZipCode(prefix + "zipCode_" + Integer.toString(invoiceNumber));
    builder.setNip(prefix + "nip_" + Integer.toString(invoiceNumber));
    builder.setBankAccoutNumber(prefix + "bankAccoutNumber_" + Integer.toString(invoiceNumber));
    return builder.build();
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
    double netValue = invoiceNumber;
    ProductBuilder builder = new ProductBuilder(name, netValue);
    builder.setDescription(name + "_" + "description_" + Integer.toString(invoiceNumber));
    builder.setVatRate(Vat.VAT_23);
    return builder.build();
  }

}

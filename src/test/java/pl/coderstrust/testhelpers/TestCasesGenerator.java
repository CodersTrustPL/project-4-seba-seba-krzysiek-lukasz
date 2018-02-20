package pl.coderstrust.testhelpers;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.PaymentState;
import pl.coderstrust.model.Product;
import pl.coderstrust.model.Vat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestCasesGenerator {


  public Invoice getTestInvoice(int invoiceNumber, int entriesCount) {

    String idVisible = "idVisible_" + Integer.toString(invoiceNumber);
    Company buyer = getTestCompany(invoiceNumber, "buyer_");
    Company seller = getTestCompany(invoiceNumber, "seller_");
    LocalDate dateIssue = LocalDate.of(2018, 1, 1);
    LocalDate datePayment = dateIssue.plusDays(15);
    List<InvoiceEntry> entries = getTestEntries(invoiceNumber, entriesCount);
    PaymentState paymentState = PaymentState.NOT_PAID;

    return new Invoice(idVisible, buyer, seller, dateIssue, datePayment, entries, paymentState);
  }

  public Company getTestCompany(int invoiceNumber, String prefix) {
    String name = prefix + "name_" + Integer.toString(invoiceNumber);
    String address = prefix + "address_" + Integer.toString(invoiceNumber);
    String city = prefix + "city_" + Integer.toString(invoiceNumber);
    String zipCode = prefix + "zipCode_" + Integer.toString(invoiceNumber);
    String nip = prefix + "nip_" + Integer.toString(invoiceNumber);
    String bankAccoutNumber = prefix + "bankAccoutNumber_" + Integer.toString(invoiceNumber);
    return new Company(name, address, city, zipCode, nip, bankAccoutNumber);
  }


  public List<InvoiceEntry> getTestEntries(int invoiceNumber, int productsCount) {
    ArrayList<InvoiceEntry> entries = new ArrayList<>(productsCount);
    for (int i = 0; i < productsCount; i++) {
      entries.add(new InvoiceEntry(getTestProduct(invoiceNumber, i), i));
    }
    return entries;
  }


  public Product getTestProduct(int invoiceNumber, int productCount) {
    String name = "name_" + Integer.toString(invoiceNumber);
    String description = "description_" + Integer.toString(invoiceNumber);
    BigDecimal netValue = new BigDecimal(invoiceNumber);
    Vat vatRate = Vat.VAT_23;
    name += "_" + Integer.toString(productCount);
    description += "_" + Integer.toString(productCount);
    return new Product(name, description, netValue, vatRate);
  }

}

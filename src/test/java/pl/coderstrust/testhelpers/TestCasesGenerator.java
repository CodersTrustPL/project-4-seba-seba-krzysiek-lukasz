package pl.coderstrust.testhelpers;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.CompanyBuilder;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceBuilder;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.PaymentState;
import pl.coderstrust.model.Product;
import pl.coderstrust.model.ProductBuilder;
import pl.coderstrust.model.Vat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestCasesGenerator {


  public Invoice getTestInvoice(int invoiceNumber, int entriesCount) {

    String idVisible = "idVisible_" + Integer.toString(invoiceNumber);
    Company buyer = getTestCompany(invoiceNumber, "buyer_");
    Company seller = getTestCompany(invoiceNumber, "seller_");
    InvoiceBuilder builder = new InvoiceBuilder(invoiceNumber, buyer.getName(), seller.getName());
    builder.setVisibleId(idVisible);
    builder.setBuyer(buyer);
    builder.setSeller(seller);
    LocalDate dateIssue = LocalDate.now().plusYears(1);
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
    for (int i = 1; i <= productsCount; i++) {
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

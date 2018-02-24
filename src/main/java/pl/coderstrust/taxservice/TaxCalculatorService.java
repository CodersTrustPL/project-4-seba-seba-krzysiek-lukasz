package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaxCalculatorService {

  private Database database;
  private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

  @Autowired
  public TaxCalculatorService(Database database) {
    this.database = database;
  }

  public String calculateIncomeCost(LocalDate beginDate, LocalDate endDate) {
    BigDecimal income = BigDecimal.valueOf(0);
    BigDecimal cost = BigDecimal.valueOf(0);

    List<Invoice> invoiceByDates = new ArrayList<>(getInvoiceByDate(beginDate, endDate));

    for (Invoice invoice : invoiceByDates) {
      if (invoice.getSeller().getName().equals("My company")) {
        income = income.add(getNetValue(invoice));
      }
      if (invoice.getBuyer().getName().equals("My company")) {
        cost = cost.add(getNetValue(invoice));
      }
    }

    return decimalFormat.format(income.subtract(cost));
  }

  public String calculateVat(LocalDate beginDate, LocalDate endDate) {
    BigDecimal income = BigDecimal.valueOf(0);
    BigDecimal cost = BigDecimal.valueOf(0);

    List<Invoice> invoiceByDates = new ArrayList<>(getInvoiceByDate(beginDate, endDate));

    for (Invoice invoice : invoiceByDates) {
      if (invoice.getSeller().getName().equals("My company")) {
        income = income.add(getVatValue(invoice));
      }
      if (invoice.getBuyer().getName().equals("My company")) {
        cost = cost.add(getVatValue(invoice));
      }
    }
    return decimalFormat.format(income.subtract(cost));
  }

  private BigDecimal getNetValue(Invoice invoice) {
    BigDecimal netValueMultiplyAmount = BigDecimal.valueOf(0);

    for (InvoiceEntry invoiceEntry : invoice.getProducts()) {
      int amount = invoiceEntry.getAmount();
      BigDecimal netValue = invoiceEntry.getProduct().getNetValue();
      netValueMultiplyAmount = netValueMultiplyAmount
          .add(netValue.multiply(new BigDecimal(amount)));
    }
    return netValueMultiplyAmount;
  }

  private BigDecimal getVatValue(Invoice invoice) {
    BigDecimal netValueMultiplyAmount = BigDecimal.valueOf(0);

    for (InvoiceEntry invoiceEntry : invoice.getProducts()) {
      int amount = invoiceEntry.getAmount();
      double vatRate = invoiceEntry.getProduct().getVatRate().getVatPercent();
      BigDecimal netValue = invoiceEntry.getProduct().getNetValue()
          .multiply(BigDecimal.valueOf(vatRate));
      netValueMultiplyAmount = netValueMultiplyAmount
          .add(netValue.multiply(new BigDecimal(amount)));
    }
    return netValueMultiplyAmount;
  }

  private List<Invoice> getInvoiceByDate(LocalDate beginDate, LocalDate endDate) {
    if (beginDate == null) {
      beginDate = LocalDate.MIN;
    }
    if (endDate == null) {
      endDate = LocalDate.MAX;
    }
    List<Invoice> selectedInvoices = new ArrayList<>();
    List<Invoice> allInvoices = database.getInvoices();
    for (Invoice invoice : allInvoices) {
      if ((invoice.getIssueDate().isBefore(endDate) || invoice.getIssueDate().isEqual(endDate))
          && (invoice.getIssueDate().isAfter(beginDate) || invoice.getIssueDate()
          .isEqual(beginDate))) {
        selectedInvoices.add(invoice);
      }
    }
    return selectedInvoices;
  }
}
package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaxCalculatorService {

  private Database database;

  @Autowired
  public TaxCalculatorService(Database database) {
    this.database = database;
  }

  public BigDecimal calculateIncome(String companyName, LocalDate beginDate, LocalDate endDate) {
    BigDecimal income = BigDecimal.valueOf(0);
    List<Invoice> invoiceByDates = new ArrayList<>(getInvoiceByDate(beginDate, endDate));

    for (Invoice invoice : invoiceByDates) {
      if (invoice.getSeller().getName().equals(companyName)) {
        income = income.add(getNetValue(invoice));
      }
    }
    return income;
  }

  public BigDecimal calculateCost(String companyName, LocalDate beginDate, LocalDate endDate) {
    BigDecimal cost = BigDecimal.valueOf(0);
    List<Invoice> invoiceByDates = new ArrayList<>(getInvoiceByDate(beginDate, endDate));

    for (Invoice invoice : invoiceByDates) {
      if (invoice.getBuyer().getName().equals(companyName)) {
        cost = cost.add(getNetValue(invoice));
      }
    }
    return cost;
  }

  public BigDecimal calculateIncomeCost(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    return calculateIncome(companyName, beginDate, endDate)
        .subtract(calculateCost(companyName, beginDate, endDate));
  }

  public BigDecimal calculateIncomeVat(String companyName, LocalDate beginDate, LocalDate endDate) {
    BigDecimal incomeVat = BigDecimal.valueOf(0);
    List<Invoice> invoiceByDates = new ArrayList<>(getInvoiceByDate(beginDate, endDate));

    for (Invoice invoice : invoiceByDates) {
      if (invoice.getBuyer().getName().equals(companyName)) {
        incomeVat = incomeVat.add(getVatValue(invoice));
      }
    }
    return incomeVat;
  }

  public BigDecimal calculateOutcomeVat(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    BigDecimal outcomeVat = BigDecimal.valueOf(0);
    List<Invoice> invoiceByDates = new ArrayList<>(getInvoiceByDate(beginDate, endDate));

    for (Invoice invoice : invoiceByDates) {
      if (invoice.getSeller().getName().equals(companyName)) {
        outcomeVat = outcomeVat.add(getVatValue(invoice));
      }
    }
    return outcomeVat;
  }

  public BigDecimal calculateDifferenceVat(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    return calculateOutcomeVat(companyName, beginDate, endDate)
        .subtract(calculateIncomeVat(companyName, beginDate, endDate));
  }

  private BigDecimal getNetValue(Invoice invoice) {
    BigDecimal netValueMultiplyAmount = BigDecimal.valueOf(0);

    for (InvoiceEntry products : invoice.getProducts()) {
      int amount = products.getAmount();
      BigDecimal netValue = products.getProduct().getNetValue();
      netValueMultiplyAmount = netValueMultiplyAmount
          .add(netValue.multiply(new BigDecimal(amount)));
    }
    return netValueMultiplyAmount;
  }

  private BigDecimal getVatValue(Invoice invoice) {
    BigDecimal netValueMultiplyAmount = BigDecimal.valueOf(0);

    for (InvoiceEntry products : invoice.getProducts()) {
      int amount = products.getAmount();
      double vatRate = products.getProduct().getVatRate().getVatPercent();

      BigDecimal netValue = products.getProduct().getNetValue()
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
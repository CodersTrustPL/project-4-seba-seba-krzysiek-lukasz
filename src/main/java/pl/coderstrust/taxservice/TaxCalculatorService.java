package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TaxCalculatorService {

  private Database database;

  @Autowired
  public TaxCalculatorService(Database database) {
    this.database = database;
  }

  public BigDecimal calculateIncome(String companyName, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> x.getSeller().getName()
        .equals(companyName) ? getNetValue(x) : BigDecimal.ZERO;

    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateCost(String companyName, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> x.getBuyer().getName()
        .equals(companyName) ? getNetValue(x) : BigDecimal.ZERO;

    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateIncomeCost(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    return calculateIncome(companyName, beginDate, endDate)
        .subtract(calculateCost(companyName, beginDate, endDate));
  }

  public BigDecimal calculateIncomeVat(String companyName, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> x.getBuyer().getName()
        .equals(companyName) ? getVatValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateOutcomeVat(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> x.getSeller().getName()
        .equals(companyName) ? getVatValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateDifferenceVat(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    return calculateOutcomeVat(companyName, beginDate, endDate)
        .subtract(calculateIncomeVat(companyName, beginDate, endDate));
  }

  private BigDecimal calculatePattern(Function<Invoice, BigDecimal> getValueFunction,
      LocalDate beginDate, LocalDate endDate) {
    BigDecimal income = BigDecimal.valueOf(0);
    for (Invoice invoice : getInvoiceByDate(beginDate, endDate)) {
      income = income.add(getValueFunction.apply(invoice));
    }
    return income;
  }

  private BigDecimal getNetValue(Invoice invoice) {
    BigDecimal netValueMultiplyAmount = BigDecimal.valueOf(0);

    for (InvoiceEntry products : invoice.getProducts()) {
      int amount = products.getAmount();
      netValueMultiplyAmount = netValueMultiplyAmount
          .add(products.getProduct().getNetValue().multiply(new BigDecimal(amount)));
    }
    return netValueMultiplyAmount;
  }

  private BigDecimal getVatValue(Invoice invoice) {
    BigDecimal netValueMultiplyAmount = BigDecimal.valueOf(0);

    for (InvoiceEntry products : invoice.getProducts()) {
      int amount = products.getAmount();
      double vatRate = products.getProduct().getVatRate().getVatPercent();
      netValueMultiplyAmount = netValueMultiplyAmount.add(products.getProduct().getNetValue()
          .multiply(BigDecimal.valueOf(vatRate)).multiply(new BigDecimal(amount)));
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
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
import java.util.function.Predicate;

public class TaxCalculatorService {

  private Database database;

  @Autowired
  public TaxCalculatorService(Database database) {
    this.database = database;
  }

  public BigDecimal calculateIncome(String companyName, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameSeller(companyName).test(x)
        ? getNetValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateCost(String companyName, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameBuyer(companyName).test(x)
        ? getNetValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateIncomeCost(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    return calculateIncome(companyName, beginDate, endDate)
        .subtract(calculateCost(companyName, beginDate, endDate));
  }

  public BigDecimal calculateIncomeVat(String companyName, LocalDate beginDate, LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameBuyer(companyName).test(x)
        ? getVatValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateOutcomeVat(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    Function<Invoice, BigDecimal> getValueFunction = x -> getCompanyNameSeller(companyName).test(x)
        ? getVatValue(x) : BigDecimal.ZERO;
    return calculatePattern(getValueFunction, beginDate, endDate);
  }

  public BigDecimal calculateDifferenceVat(String companyName, LocalDate beginDate,
      LocalDate endDate) {
    return calculateOutcomeVat(companyName, beginDate, endDate)
        .subtract(calculateIncomeVat(companyName, beginDate, endDate));
  }

  private Predicate<Invoice> getCompanyNameSeller(String companyName) {
    return x -> x.getSeller().getName().equals(companyName);
  }

  private Predicate<Invoice> getCompanyNameBuyer(String companyName) {
    return x -> x.getBuyer().getName().equals(companyName);
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
    BigDecimal netValue = BigDecimal.valueOf(0);
    for (InvoiceEntry products : invoice.getProducts()) {
      netValue = netValue.add(products.getProduct()
          .getNetValue().multiply(new BigDecimal(products.getAmount())));
    }
    return netValue;
  }

  private BigDecimal getVatValue(Invoice invoice) {
    BigDecimal vatValue = BigDecimal.valueOf(0);
    for (InvoiceEntry products : invoice.getProducts()) {
      vatValue = vatValue.add(products.getProduct().getNetValue()
          .multiply(BigDecimal.valueOf(products.getProduct().getVatRate().getVatPercent()))
          .multiply(new BigDecimal(products.getAmount())));
    }
    return vatValue;
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
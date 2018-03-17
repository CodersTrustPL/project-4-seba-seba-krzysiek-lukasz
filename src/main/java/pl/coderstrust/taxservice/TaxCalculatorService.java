package pl.coderstrust.taxservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Resource;

@Service
public class TaxCalculatorService {

  @Resource(name = "masterWithInvoices")
  private Database<Invoice> database;

  @Autowired
  public TaxCalculatorService() {
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
    List<Invoice> allInvoices = database.getEntries();
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
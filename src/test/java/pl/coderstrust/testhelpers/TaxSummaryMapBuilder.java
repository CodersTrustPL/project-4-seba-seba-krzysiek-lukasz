package pl.coderstrust.testhelpers;

import pl.coderstrust.model.Messages;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaxSummaryMapBuilder {

  private LinkedHashMap<String, BigDecimal> map = new LinkedHashMap<>();

  public TaxSummaryMapBuilder setIncome(double income) {
    map.put(Messages.TAX_SUMMARY_INCOME, BigDecimal.valueOf(income).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setCosts(double costs) {
    map.put(Messages.TAX_SUMMARY_COSTS, BigDecimal.valueOf(costs).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeMinusCosts(double incomeMinusCosts) {
    map.put(Messages.TAX_SUMMARY_INCOME_MINUS_COSTS,
        BigDecimal.valueOf(incomeMinusCosts).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setPensionInsurancePaid(double pensionInsurancePaid) {
    map.put(Messages.TAX_SUMMARY_PENSION_INSURANCE_PAID,
        BigDecimal.valueOf(pensionInsurancePaid).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setTaxCalculationBase(double taxCalculationBase) {
    map.put(Messages.TAX_SUMMARY_TAX_CALCULATION_BASE,
        BigDecimal.valueOf(taxCalculationBase).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeTax(double incomeTax) {
    map.put(Messages.TAX_SUMMARY_INCOME_TAX, BigDecimal.valueOf(incomeTax).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setDecresingTaxAmount(double decresingTaxAmount) {
    map.put(Messages.TAX_SUMMARY_DECREASING_TAX_AMOUNT,
        BigDecimal.valueOf(decresingTaxAmount).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeTaxPaid(double incomeTaxPaid) {
    map.put(Messages.TAX_SUMMARY_INCOME_TAX_PAID, BigDecimal.valueOf(incomeTaxPaid).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setHealthInsurancePaid(double healthInsurancePaid) {
    map.put(Messages.TAX_SUMMARY_HEALTH_INSURANCE_PAID,
        BigDecimal.valueOf(healthInsurancePaid).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setHealthInsurancetoSusbstract(double healthInsurancetoSusbstract) {
    map.put(Messages.TAX_SUMMARY_HEALTH_INSURANCE_TO_SUBSTRACT,
        BigDecimal.valueOf(healthInsurancetoSusbstract).setScale(2));
    return this;
  }

  public TaxSummaryMapBuilder setIncomeTaxToPay(double incomeTaxToPay) {
    map.put(Messages.TAX_SUMMARY_INCOME_TAX_TO_PAY,
        BigDecimal.valueOf(incomeTaxToPay).setScale(2));
    return this;
  }

  public Map<String, BigDecimal> build() {
    return map;
  }
}
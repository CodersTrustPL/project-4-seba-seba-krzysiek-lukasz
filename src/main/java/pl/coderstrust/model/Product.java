package pl.coderstrust.model;

import java.math.BigDecimal;

public class Product {

  private String name;
  private String description;
  private BigDecimal netValue;
  private Vat vatRate;

  public Product(){
  }

  /**
   * Constructor.
   */
  public Product(String name, String description, BigDecimal netValue,
      Vat vatRate) {
    this.name = name;
    this.description = description;
    this.netValue = netValue;
    this.vatRate = vatRate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getNetValue() {
    return netValue;
  }

  public void setNetValue(BigDecimal netValue) {
    this.netValue = netValue;
  }

  public Vat getVatRate() {
    return vatRate;
  }

  public void setVatRate(Vat vatRate) {
    this.vatRate = vatRate;
  }
}

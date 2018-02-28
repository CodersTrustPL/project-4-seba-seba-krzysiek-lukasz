package pl.coderstrust.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

  private String name;
  private String description;
  private BigDecimal netValue;
  private Vat vatRate;

  public Product() {
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

  @Override
  public String toString() {
    return "Product{"
        + "name='" + name + '\''
        + ", description='" + description + '\''
        + ", netValue=" + netValue
        + ", vatRate=" + vatRate
        + '}';
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Product product = (Product) object;
    return Objects.equals(name, product.name)
        && Objects.equals(description, product.description)
        && Objects.equals(netValue, product.netValue)
        && vatRate == product.vatRate;
  }

  @Override
  public int hashCode() {

    return Objects.hash(name, description, netValue, vatRate);
  }
}

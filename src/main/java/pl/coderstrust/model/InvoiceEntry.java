package pl.coderstrust.model;

import java.util.Objects;

public class InvoiceEntry {

  private Product product;
  private int amount;

  public InvoiceEntry(Product product, int amount) {
    this.product = product;
    this.amount = amount;
  }

  public InvoiceEntry() {
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "InvoiceEntry{" +
        "product=" + product +
        ", amount=" + amount +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvoiceEntry that = (InvoiceEntry) o;
    return amount == that.amount &&
        Objects.equals(product, that.product);
  }

  @Override
  public int hashCode() {

    return Objects.hash(product, amount);
  }
}

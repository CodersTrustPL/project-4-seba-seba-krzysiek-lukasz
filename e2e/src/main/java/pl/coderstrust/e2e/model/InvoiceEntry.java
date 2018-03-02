package pl.coderstrust.e2e.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceEntry {

  private Product product;
  private int amount;

  public InvoiceEntry(Product product, int amount) {
    this.product = product;
    this.amount = amount;
  }
}

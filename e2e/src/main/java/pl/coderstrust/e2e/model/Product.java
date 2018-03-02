package pl.coderstrust.e2e.model;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Builder
public class Product {

  private String name;
  private String description;
  private BigDecimal netValue;
  private Vat vatRate;


//  @Override
//  public String toString() {
//    return "Product{"
//        + "name='" + name + '\''
//        + ", description='" + description + '\''
//        + ", netValue=" + netValue
//        + ", vatRate=" + vatRate
//        + '}';
//  }
}

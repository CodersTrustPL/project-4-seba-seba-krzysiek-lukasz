package pl.coderstrust.e2e.model;


import java.time.LocalDate;
import java.util.List;
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
public class Invoice {

  private long id;
  private String invoiceName;
  private Company buyer;
  private Company seller;
  private LocalDate issueDate;
  private LocalDate paymentDate;
  List<InvoiceEntry> products;
  private PaymentState paymentState;

//  @Override
//  public String toString() {
//    return "Invoice{"
//        + "id=" + id
//        + ", invoiceName='" + invoiceName + '\''
//        + ", buyer=" + buyer
//        + ", seller=" + seller
//        + ", issueDate=" + issueDate
//        + ", paymentDate=" + paymentDate
//        + ", products=" + products
//        + ", paymentState=" + paymentState + '}';
//  }
}

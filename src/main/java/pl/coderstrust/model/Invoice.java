package pl.coderstrust.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Invoice {

  private long id;
  private String invoiceName;
  private Company buyer;
  private Company seller;
  private LocalDate issueDate;
  private LocalDate paymentDate;
  List<InvoiceEntry> products = new ArrayList<>();
  private PaymentState paymentState;

  public Invoice() {
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "id=" + id
        + ", invoiceName='" + invoiceName + '\''
        + ", buyer=" + buyer
        + ", seller=" + seller
        + ", issueDate=" + issueDate
        + ", paymentDate=" + paymentDate
        + ", products=" + products
        + ", paymentState=" + paymentState + '}';
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getInvoiceName() {
    return invoiceName;
  }

  public void setInvoiceName(String invoiceName) {
    this.invoiceName = invoiceName;
  }

  public Company getBuyer() {
    return buyer;
  }

  public void setBuyer(Company buyer) {
    this.buyer = buyer;
  }

  public Company getSeller() {
    return seller;
  }

  public void setSeller(Company seller) {
    this.seller = seller;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = LocalDate.parse(issueDate);
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = LocalDate.parse(paymentDate);
  }

  public List<InvoiceEntry> getProducts() {
    return products;
  }

  public void setProducts(List<InvoiceEntry> products) {
    this.products = products;
  }

  public PaymentState getPaymentState() {
    return paymentState;
  }

  public void setPaymentState(PaymentState paymentState) {
    this.paymentState = paymentState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Invoice invoice = (Invoice) o;
    return id == invoice.id &&
        Objects.equals(invoiceName, invoice.invoiceName) &&
        Objects.equals(buyer, invoice.buyer) &&
        Objects.equals(seller, invoice.seller) &&
        Objects.equals(issueDate, invoice.issueDate) &&
        Objects.equals(paymentDate, invoice.paymentDate) &&
        Objects.equals(products, invoice.products) &&
        paymentState == invoice.paymentState;
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(id, invoiceName, buyer, seller, issueDate, paymentDate, products, paymentState);
  }
}

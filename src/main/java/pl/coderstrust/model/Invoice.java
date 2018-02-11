package pl.coderstrust.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

  private long systemId;
  private String visibleId;
  private Company buyer;
  private Company seller;
  private LocalDate issueDate;
  private LocalDate paymentDate;
  List<InvoiceEntry> products = new ArrayList<>();
  private PaymentState paymentState;

  public Invoice() {
  }

  /**
   * Constructor.
   */
  public Invoice(String visibleId, Company buyer, Company seller,
      LocalDate issueDate, LocalDate paymentDate,
      List<InvoiceEntry> products, PaymentState paymentState) {
    this.visibleId = visibleId;
    this.buyer = buyer;
    this.seller = seller;
    this.issueDate = issueDate;
    this.paymentDate = paymentDate;
    this.products = products;
    this.paymentState = paymentState;
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "systemId=" + systemId
        + ", visibleId='" + visibleId + '\''
        + ", buyer=" + buyer
        + ", seller=" + seller
        + ", issueDate=" + issueDate
        + ", paymentDate=" + paymentDate
        + ", products=" + products
        + ", paymentState=" + paymentState + '}';
  }

  public long getSystemId() {
    return systemId;
  }

  public void setSystemId(long systemId) {
    this.systemId = systemId;
  }

  public String getVisibleId() {
    return visibleId;
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

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
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
}

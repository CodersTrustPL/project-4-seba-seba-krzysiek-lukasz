package pl.coderstrust.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.format.annotation.NumberFormat;
import pl.coderstrust.database.hibernate.LocalDateTimeConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Payment implements WithValidation {

  private Long id;

  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDate issueDate;

  @NumberFormat(pattern = "#,###,###,###.##")
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private PaymentType type;

  public Payment(Long id, LocalDate issueDate, BigDecimal amount,
      PaymentType type) {
    this.id = id;
    this.issueDate = issueDate;
    this.amount = amount;
    this.type = type;
  }

  public Payment() {
  }

  @Override
  public List<String> validate() {
    List<String> errors = new ArrayList<>();
    if (issueDate == null) {
      errors.add("Date is empty.");
    }
    if (amount == null) {
      errors.add("Amount is empty.");
    }
    if (type == null) {
      errors.add("Payment type is empty.");
    }
    return errors;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public PaymentType getType() {
    return type;
  }

  public void setType(PaymentType type) {
    this.type = type;
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  @Override
  public boolean equals(Object object) {
    return EqualsBuilder.reflectionEquals(this, object);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, true);
  }

}



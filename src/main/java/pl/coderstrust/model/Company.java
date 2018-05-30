package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import pl.coderstrust.database.hibernate.LocalDateAttributeConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comapnies")
public class Company implements WithNameIdIssueDate, WithValidation {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private long id;
  private String name;
  @Convert(converter = LocalDateAttributeConverter.class)
  private LocalDate issueDate;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccountNumber;
  @Enumerated(EnumType.STRING)
  private TaxType taxType;
  private boolean personalCarUsage;
  @ElementCollection
  private List<Payment> payments = new ArrayList<>();

  public Company() {
  }

  public Company(String name) {
    this();
    this.name = name;
  }

  @Override
  @JsonProperty("companyId")
  public long getId() {
    return id;
  }

  @Override
  public void setId(long id) {
    this.id = id;
  }

  @Override
  public LocalDate getIssueDate() {
    return issueDate;
  }

  @Override
  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  @Override
  @ApiModelProperty(example = "Company Name")
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @ApiModelProperty(example = "ul. Jaworzyńska 7/9")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @ApiModelProperty(example = "Warszawa")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @ApiModelProperty(example = "00-634")
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  @ApiModelProperty(example = "NIP of your company")
  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  @ApiModelProperty(example = "bank account number of your company")
  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
  }

  public TaxType getTaxType() {
    return taxType;
  }

  public void setTaxType(TaxType taxType) {
    this.taxType = taxType;
  }

  public boolean isPersonalCarUsage() {
    return personalCarUsage;
  }

  public void setPersonalCarUsage(boolean personalCarUsage) {
    this.personalCarUsage = personalCarUsage;
  }

  public List<Payment> getPayments() {
    return payments;
  }

  public void setPayments(List<Payment> payments) {
    this.payments = payments;
  }

  @Override
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

  @Override
  public List<String> validate() {
    List<String> errors = new ArrayList<>();
    if (checkInputString(this.getName())) {
      errors.add(Messages.COMPANY_NO_NAME);
    }
    if (checkInputString(this.getAddress())) {
      errors.add(Messages.COMPANY_NO_ADRESS);
    }
    if (checkInputString(this.getCity())) {
      errors.add(Messages.COMPANY_NO_CITY);
    }
    if (checkInputString(this.getNip())) {
      errors.add(Messages.COMPANY_NO_NIP);
    }
    if (checkInputString(this.getZipCode())) {
      errors.add(Messages.COMPANY_NO_ZIPCODE);
    }
    if (checkInputString(this.getBankAccountNumber())) {
      errors.add(Messages.COMPANY_NO_BACC);
    }

    return errors;
  }
}
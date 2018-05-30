package pl.coderstrust.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;
import pl.coderstrust.configurations.MoneySerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Component("product")
@Embeddable
public class Product implements WithValidation {

  private String name;
  private String description;
  @JsonSerialize(using = MoneySerializer.class)
  private BigDecimal netValue;
  @Enumerated(EnumType.STRING)
  private Vat vatRate;
  @Enumerated(EnumType.STRING)
  private ProductType productType;

  public Product() {
  }

  public Product(String name, String description, BigDecimal netValue, Vat vatRate,
      ProductType productType) {
    this.name = name;
    this.description = description;
    if (netValue != null) {
      this.netValue = netValue.setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      this.netValue = null;
    }

    this.vatRate = vatRate;
    this.productType = productType;
  }

  @ApiModelProperty(example = "Apple")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ApiModelProperty(example = "Green Fresh Apple")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @ApiModelProperty(example = "2.50")
  public BigDecimal getNetValue() {
    return netValue;
  }

  public void setNetValue(BigDecimal netValue) {
    if (netValue != null) {
      this.netValue = netValue.setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      this.netValue = null;
    }
  }

  public Vat getVatRate() {
    return vatRate;
  }

  public void setVatRate(Vat vatRate) {
    this.vatRate = vatRate;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
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
      errors.add(Messages.PRODUCT_NO_NAME);
    }

    if (checkInputString(this.getDescription())) {
      errors.add(Messages.PRODUCT_NO_DESCRIPTION);
    }

    if (this.getVatRate() == null) {
      errors.add(Messages.PRODUCT_NO_VAT);
    }

    if (this.getNetValue() == null) {
      errors.add(Messages.PRODUCT_NO_NET_VALUE);
    } else if (this.getNetValue().compareTo(BigDecimal.ZERO) <= 0) {
      errors.add(Messages.PRODUCT_WRONG_NET_VALUE);
    }
    return errors;
  }
}

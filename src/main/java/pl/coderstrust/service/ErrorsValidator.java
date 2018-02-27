package pl.coderstrust.service;

import org.springframework.stereotype.Service;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.Messages;
import pl.coderstrust.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ErrorsValidator {

  public List<String> validateInvoice(Invoice invoice) {
    List<String> errors = new ArrayList<>();
    errors.addAll(checkCompany(invoice.getSeller()));
    errors.addAll(checkCompany(invoice.getBuyer()));
    errors.addAll(checkDate(invoice.getIssueDate()));
    errors.addAll(checkDate(invoice.getPaymentDate()));
    if (invoice.getProducts().size() == 0) {
      errors.add(Messages.PRODUCTS_LIST_EMPTY);
    } else {
      for (int i = 0; i < invoice.getProducts().size(); i++) {
        errors.addAll(checkProduct(invoice.getProducts().get(i).getProduct()));
      }
    }
    if (invoice.getPaymentState() == null) {
      errors.add(Messages.PAYMENT_STATE_EMPTY);
    }
    return errors;
  }

  public List<String> checkCompany(Company company) {

    List<String> errors = new ArrayList<>();
    if (checkInputString(company.getName())) {
      errors.add(Messages.COMPANY_NO_NAME);
    }
    if (checkInputString(company.getAddress())) {
      errors.add(Messages.COMPANY_NO_ADRESS);
    }
    if (checkInputString(company.getCity())) {
      errors.add(Messages.COMPANY_NO_CITY);
    }
    if (checkInputString(company.getNip())) {
      errors.add(Messages.COMPANY_NO_NIP);
    }
    if (checkInputString(company.getZipCode())) {
      errors.add(Messages.COMPANY_NO_ZIPCODE);
    }
    if (checkInputString(company.getBankAccoutNumber())) {
      errors.add(Messages.COMPANY_NO_BACC);
    }

    return errors;
  }

  public List<String> checkDate(LocalDate date) {
    if (date == null) {
      return Arrays.asList(Messages.DATE_EMPTY);
    }
    List<String> errors = new ArrayList<>();
    if (date.isBefore(LocalDate.now())) {
      errors.add(Messages.DATE_TOO_EARLY);
    }
    return errors;
  }

  public List<String> checkProduct(Product product) {

    List<String> errors = new ArrayList<>();

    if (checkInputString(product.getName())) {
      errors.add(Messages.PRODUCT_NO_NAME);
    }

    if (checkInputString(product.getDescription())) {
      errors.add(Messages.PRODUCT_NO_DESCRIPTION);
    }

    if (product.getVatRate() == null) {
      errors.add(Messages.PRODUCT_NO_VAT);
    }

    if (product.getNetValue() == null) {
      errors.add(Messages.PRODUCT_NO_NET_VALUE);
    } else {
      if (BigDecimal.ZERO.compareTo(product.getNetValue()) >= 0) {
        errors.add(Messages.PRODUCT_WRONG_NET_VALUE);
      }
    }

    return errors;
  }

  private boolean checkInputString(String input) {
    return ((input == null || input.trim().length() == 0));
  }
}

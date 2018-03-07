package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.Messages;

import java.time.LocalDate;
import java.util.List;

@RestController
@Configuration
public class InvoiceBookController {

  private InvoiceBook invoiceBook;
  private ErrorsValidator errorsValidator;

  @Autowired
  public InvoiceBookController(InvoiceBook invoiceBook,
      ErrorsValidator errorsValidator) {
    this.invoiceBook = invoiceBook;
    this.errorsValidator = errorsValidator;
  }

  @RequestMapping(value = "invoice", method = RequestMethod.POST)
  public ResponseEntity addInvoice(@RequestBody Invoice invoice) {
    List<String> invoiceState = errorsValidator.validateInvoice(invoice);
    if (invoiceState.isEmpty()) {
      long id = invoiceBook.addInvoice(invoice);
      return ResponseEntity.ok(Messages.CONTROLLER_INVOICE_ADDED + id);
    }
    return ResponseEntity.badRequest().body(invoiceState);
  }

  @RequestMapping(value = "invoice/{id}", method = RequestMethod.GET)
  public ResponseEntity getInvoiceById(@PathVariable("id") long id) {
    if (!invoiceBook.idExist(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(invoiceBook.findInvoice(id));
  }

  @RequestMapping(value = "invoice", method = RequestMethod.GET)
  public ResponseEntity getInvoiceByDate(
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate) {
    if (startDate == null && endDate == null) {
      return ResponseEntity.ok(invoiceBook.getInvoices());
    }
    return ResponseEntity.ok(invoiceBook.getInvoiceByDate(startDate,
        endDate));
  }

  @RequestMapping(value = "invoice/{id}", method = RequestMethod.PUT)
  public ResponseEntity updateInvoice(@PathVariable("id") long id, @RequestBody Invoice invoice) {
    List<String> invoiceState = errorsValidator.validateInvoice(invoice);
    if (!invoiceState.isEmpty()) {
      return ResponseEntity.badRequest().body(invoiceState);
    }
    invoice.setId(id);
    invoiceBook.updateInvoice(invoice);
    return ResponseEntity.ok().build();

  }

  @RequestMapping(value = "invoice/{id}", method = RequestMethod.DELETE)
  public ResponseEntity removeInvoice(@PathVariable("id") long id) {
    if (!invoiceBook.idExist(id)) {
      return ResponseEntity.notFound().build();
    }
    invoiceBook.deleteInvoice(id);
    return ResponseEntity.ok().build();
  }
}

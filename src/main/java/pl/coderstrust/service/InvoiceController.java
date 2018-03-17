package pl.coderstrust.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

@RestController
@RequestMapping("invoice")
public class InvoiceController extends AbstractController<Invoice> {

  public InvoiceController(InvoiceService invoiceService, InvoiceByCompanyFilter byCustomerFilter) {
    super.service = invoiceService;
    super.byCustomerFilter = byCustomerFilter;
  }
}

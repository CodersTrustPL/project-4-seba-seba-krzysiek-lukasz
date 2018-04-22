package pl.coderstrust.service.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.AbstractController;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.filters.InvoiceDummyFilter;
import pl.coderstrust.service.soap.bindingClasses.*;


import java.time.LocalDate;
import java.util.List;


@Endpoint
public class InvoiceEndpoint extends AbstractController<Invoice> {

  private static final String NAMESPACE_URI = "http://invoice-service.com";
  private ModelSoapConverter converter = new ModelSoapConverter();

  @Autowired
  public InvoiceEndpoint(InvoiceService invoiceService, InvoiceDummyFilter dummyFilter) {
    super.service = invoiceService;
    super.filter = dummyFilter;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceAddRequest")
  @ResponsePayload
  public InvoiceAddResponse addInvoice(@RequestPayload InvoiceAddRequest request) {
    Invoice invoice = converter.soapInvoiceToInvoice(request.getInvoice());
    List<String> entryState = invoice.validate();

    long invoiceId;
    InvoiceAddResponse response = new InvoiceAddResponse();

    if (entryState.isEmpty()) {
      invoiceId = service.addEntry(invoice);
      response.setInvoiceId(invoiceId);
      response.getErrorMsg().add("No errors.");
    } else {
      response.setInvoiceId(-1);
      response.getErrorMsg().addAll(entryState);
    }
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceGetByIdRequest")
  @ResponsePayload
  public InvoiceGetByIdResponse getInvoiceById(@RequestPayload InvoiceGetByIdRequest request) {

    long id = request.getId();
    InvoiceGetByIdResponse response = new InvoiceGetByIdResponse();

    if (!service.idExist(id)) {
      response.setInvoice(null);
      response.getErrorMsg().add("Invoice with specified id does not exist.");

    } else {
      response.setInvoice(converter.invoiceToSoapInvoice(service.findEntry(id)));
      response.getErrorMsg().add("No errors.");
    }
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceGetByDateRequest")
  @ResponsePayload
  public InvoiceGetByDateResponse getInvoiceByDate(
      @RequestPayload InvoiceGetByDateRequest request) {

    LocalDate startDate = LocalDate.parse(request.getDateFrom());
    LocalDate endDate = LocalDate.parse(request.getDateTo());
    List<Invoice> invoices;

    invoices = service.getEntryByDate(startDate, endDate);
    InvoiceList soapInvoices = new InvoiceList();

    for (Invoice invoice : invoices) {
      soapInvoices.getInvoice().add(converter.invoiceToSoapInvoice(invoice));
    }

    InvoiceGetByDateResponse response = new InvoiceGetByDateResponse();
    response.setInvoiceList(soapInvoices);
    response.getErrorMsg().add("No errors.");
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceUpdateRequest")
  @ResponsePayload
  public InvoiceUpdateResponse updateInvoice(@RequestPayload InvoiceUpdateRequest request) {
    Invoice invoice = converter.soapInvoiceToInvoice(request.getInvoice());
    List<String> entryState = invoice.validate();

    InvoiceUpdateResponse response = new InvoiceUpdateResponse();
    long invoiceId = request.getId();
    invoice.setId(invoiceId);

    if (entryState.isEmpty()) {
      service.updateEntry(invoice);
      response.setInvoiceId(invoiceId);
      response.getErrorMsg().add("No errors.");
    } else {
      response.setInvoiceId(-1);
      response.getErrorMsg().addAll(entryState);
    }
    return response;
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "invoiceRemoveRequest")
  @ResponsePayload
  public InvoiceRemoveResponse removeInvoice(@RequestPayload InvoiceRemoveRequest request) {

    long id = request.getId();
    InvoiceRemoveResponse response = new InvoiceRemoveResponse();

    if (!service.idExist(id)) {
      response.getErrorMsg().add("Invoice with specified id does not exist.");
    } else {
      service.deleteEntry(id);
      response.setResponse("Invoice deleted.");
      response.getErrorMsg().add("No errors.");
    }
    return response;
  }
}
package pl.coderstrust.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InFileDatabase implements Database {

  private FileHelper fileHelper;
  private ObjectMapperProvider mapper;

  /**
   * Constructor that sets Jakson mapper and creates FileHelper objects.
   */
  public InFileDatabase() {
    mapper = new ObjectMapperProvider();
    fileHelper = new FileHelper();
  }

  @Override
  public void addInvoice(Invoice invoice) {
    try {
      fileHelper.addLine(mapper.getJsonMapper().writeValueAsString(invoice));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteInvoiceById(long systemId) {
    fileHelper.deleteLine(idToLineKey(systemId));
  }

  @Override
  public Invoice getInvoiceById(long systemId) {
    String jsonInvoice = fileHelper.getLine(idToLineKey(systemId));
    Invoice invoice = null;
    try {
      invoice = mapper.getJsonMapper().readValue(jsonInvoice, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoice;
  }

  String idToLineKey(long systemId) {
    return "\"systemId\":" + String.valueOf(systemId) + ",";
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    deleteInvoiceById(invoice.getSystemId());
    addInvoice(invoice);
  }

  @Override
  public ArrayList<Invoice> getInvoices() {

    ArrayList<String> lines = fileHelper.getAllLines();
    return lines.stream()
        .map(line -> jsonToInvoice(line))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    try {
      invoice = mapper.getJsonMapper().readValue(json, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoice;
  }

  @Override
  public void cleanDatabase() {
    fileHelper.cleanDatabase();
  }
}

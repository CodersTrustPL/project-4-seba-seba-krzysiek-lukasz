package pl.coderstrust.database.file;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class InFileDatabase implements Database {

  private ObjectMapper mapper;
  private FileHelper fileHelper;

  /**
   * Construcor that sets jakson mapper and creates FileHelper objects.
   */
  public InFileDatabase() {
    mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    fileHelper = new FileHelper();
  }

  @Override
  public void addInvoice(Invoice invoice) {
    String json;
    try {
      json = mapper.writeValueAsString(invoice);
      fileHelper.addLine(json);
      System.out.println("Adding invoice:" + invoice.getSystemId());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteInvoiceById(long id) {
    fileHelper.deleteLine(idToLineKey(id));
  }

  @Override
  public Invoice getInvoiceById(long id) {
    String jsonInvoice = fileHelper.getLine(idToLineKey(id));
    Invoice invoice = null;
    try {
      invoice = mapper.readValue(jsonInvoice, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Found invoice:" + invoice.getSystemId());
    return invoice;
  }

  String idToLineKey(long id) {
    return "\"systemId\":" + String.valueOf(id) + ",";
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

  @Override
  public void cleanDatabase() {
    fileHelper.cleanDatabase();
  }

  private Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    try {
      invoice = mapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoice;
  }
}

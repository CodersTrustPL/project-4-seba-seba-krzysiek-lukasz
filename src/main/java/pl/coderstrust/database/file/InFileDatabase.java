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

  private ObjectMapper jsonMapper;
  private FileHelper fileHelper;

  /**
   * Constructor that sets Jakson mapper and creates FileHelper objects.
   */
  public InFileDatabase() {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
    fileHelper = new FileHelper();
  }

  @Override
  public void addInvoice(Invoice invoice) {
    try {
      fileHelper.addLine(jsonMapper.writeValueAsString(invoice));
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
      invoice = jsonMapper.readValue(jsonInvoice, Invoice.class);
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

  @Override
  public void cleanDatabase() {
    fileHelper.cleanDatabase();
  }

  private Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    try {
      invoice = jsonMapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoice;
  }
}

package pl.coderstrust.database.file;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.ObjectMapperProvider;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class InFileDatabase implements Database {

  private FileHelper fileHelper;
  private ObjectMapperProvider mapper;

  public InFileDatabase() {
    mapper = new ObjectMapperProvider();
    fileHelper = new FileHelper();
  }

  @Override
  public void addInvoice(Invoice invoice) {
    fileHelper.addLine(mapper.toJson(invoice));
  }

  @Override
  public void deleteInvoiceById(long systemId) {
    fileHelper.deleteLine(idToLineKey(systemId));
  }

  @Override
  public Invoice getInvoiceById(long systemId) {
    String jsonInvoice = fileHelper.getLine(idToLineKey(systemId));
    return mapper.toInvoice(jsonInvoice);
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
        .map(line -> mapper.toInvoice(line))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
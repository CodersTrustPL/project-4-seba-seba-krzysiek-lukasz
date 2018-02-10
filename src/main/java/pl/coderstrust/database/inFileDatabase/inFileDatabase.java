package pl.coderstrust.database.inFileDatabase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class inFileDatabase implements Database {

  private ObjectMapper mapper;
  private FileHelper fileHelper;

  public inFileDatabase() {
    mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    fileHelper = new FileHelper();
  }

  @Override
  public void addInvoice(Invoice invoice) {
    ObjectWriter ow = mapper.writer();
    String json;
    try {
      json = ow.writeValueAsString(invoice);
      fileHelper.addLine(json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteInvoiceById(long id) {
    String key = "\"systemId\":" + String.valueOf(id) + ",";
    fileHelper.deleteLine(key);
  }

  @Override
  public Invoice getInvoiceById(long id) {
    String key = "\"systemId\":" + String.valueOf(id) + ",";
    String jsonInvoice = fileHelper.getLine(key);
    Invoice invoice = null;
    try {
      invoice = mapper.readValue(jsonInvoice, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoice;
  }

//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//    Invoice invoice = null;
//    try (FileInputStream fis = new FileInputStream("myfile.txt")) {
//      JsonFactory jf = new JsonFactory();
//      objectMapper.registerModule(new JavaTimeModule());
//      JsonParser jp = jf.createParser(fis);
//      jp.setCodec(objectMapper);
//      jp.nextToken();
//      while (jp.hasCurrentToken()) {
//        invoice = jp.readValueAs(Invoice.class);
//        if (invoice.getSystemId() == id) {
//          break;
//        }
//        jp.nextToken();
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return invoice;
//  }

  @Override
  public void updateInvoice(Invoice invoice) {
    deleteInvoiceById(invoice.getSystemId());
    addInvoice(invoice);
  }

  @Override
  public List<Invoice> getInvoices() {

    ArrayList<String> lines = fileHelper.getAllLines();
    return lines.stream()
        .map(line -> jsonToInvoice(line))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private Invoice jsonToInvoice(String json) {
    Invoice invoice = null;
    try {
      mapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoice;
  }
}

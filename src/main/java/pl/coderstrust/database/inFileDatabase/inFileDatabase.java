package pl.coderstrust.database.inFileDatabase;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class inFileDatabase implements Database {


  @Override
  public void addInvoice(Invoice invoice) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      ObjectWriter ow = mapper.writer();
      String json = ow.writeValueAsString(invoice);
      json += System.lineSeparator();
      Path path = Paths.get("myfile.txt");
      if (Files.exists(path)) {
        Files.write(path, json.getBytes(), StandardOpenOption.APPEND);
      } else {
        Files.write(path, json.getBytes(), StandardOpenOption.CREATE);
      }

    } catch (IOException e) {

    }
  }

  @Override
  public void deleteInvoiceById(long id) {
    File file = new File("myfile.txt");
    File temp = new File("_temp_");
    String key = "\"systemId\":" + String.valueOf(id);
      try (
          PrintWriter out =
              new PrintWriter(new FileWriter(temp));
          Stream<String> stream =
              Files.lines(file.toPath())
      ){
        stream
            .filter(line -> !line.contains(key))
            .forEach(out::println);
      }catch(Exception e){
        e.printStackTrace();
      }
      try{
        Files.delete(file.toPath());
        Files.copy(temp.toPath(),file.toPath());
        Files.delete(temp.toPath());
      }catch (IOException e) {
        e.printStackTrace();
    }
  }
  @Override
  public Invoice getInvoiceById(long id) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Invoice invoice = null;
    try (FileInputStream fis = new FileInputStream("myfile.txt")) {
      JsonFactory jf = new JsonFactory();
      objectMapper.registerModule(new JavaTimeModule());
      JsonParser jp = jf.createParser(fis);
      jp.setCodec(objectMapper);
      jp.nextToken();
      while (jp.hasCurrentToken()) {
        invoice = jp.readValueAs(Invoice.class);
        if (invoice.getSystemId() == id) {
          break;
        }
        jp.nextToken();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return invoice;
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    deleteInvoiceById(invoice.getSystemId());
    addInvoice(invoice);
  }

  @Override
  public List<Invoice> getInvoices() {
    return new ArrayList(2);
  }
}

package pl.coderstrust.database.inFileDatabase;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jdk.nashorn.internal.ir.ObjectNode;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class inFileDatabase implements Database {


  @Override
  public void addInvoice(Invoice invoice) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
      String json = ow.writeValueAsString(invoice);
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

    try (FileInputStream fis = new FileInputStream("myfile.txt")) {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(fis);
      ObjectNode object = root.get(1).r



    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Invoice getInvoiceById(long id) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Invoice token = null;
    try (FileInputStream fis = new FileInputStream("myfile.txt")){
      JsonFactory jf = new JsonFactory();
      objectMapper.registerModule(new JavaTimeModule());

      JsonParser jp = jf.createParser(fis);
      jp.setCodec(objectMapper);
      jp.nextToken();
      while (jp.hasCurrentToken()) {
       token = jp.readValueAs(Invoice.class);
        jp.nextToken();
        System.out.println("Token serial " + token.getVisibleId());
      }
    }catch (IOException e) {
      e.printStackTrace();
    }
   return token;
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);




    try (FileInputStream fis = new FileInputStream("myfile.txt")){
      JsonFactory jf = new JsonFactory();
      objectMapper.registerModule(new JavaTimeModule());
      JsonParser jp = jf.createParser(fis);
      jp.setCodec(objectMapper);
      jp.hasCurrentToken();
      JsonNode test = jp.readValueAsTree();
      test.
      int inv = test.size();
//      while (jp.hasCurrentToken()) {
//          jp.setCurrentValue(invoice);
//          jp
//      }
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Invoice> getInvoices() {
    return new ArrayList(2);
  }
}

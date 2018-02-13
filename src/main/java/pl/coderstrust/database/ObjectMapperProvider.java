package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.database.memory.InvoiceProcessingException;
import pl.coderstrust.model.Invoice;

import java.io.IOException;

public class ObjectMapperProvider {

  private ObjectMapper jsonMapper;

  /**
   * Construct object mapper and sets its proper configuration.
   */
  public ObjectMapperProvider() {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Wraps wireValueAsString method.
   * @param value Object ot be converted to Json.
   * @return Json String representing Object.
   */

  public String toJson(Object value) {
    try {
      return jsonMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new InvoiceProcessingException("Internal invoice processing error");
    }
  }

  public Invoice toInvoice(String json) {
    try {
      return jsonMapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      throw new InvoiceProcessingException("Internal invoice processing error");
    }
  }
}

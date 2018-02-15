package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.model.Invoice;

import java.io.IOException;

public class ObjectMapperHelper {

  private ObjectMapper jsonMapper;

  /**
   * Construct object mapper and sets its proper configuration.
   */
  public ObjectMapperHelper() {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
  }

  /**
   * Wraps wireValueAsString method.
   *
   * @param value Object ot be converted to Json.
   * @return Json String representing Object.
   */

  public String toJson(Object value) {
    try {
      return jsonMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new DbException("Internal invoice processing error");
    }
  }

  /**
   * Wraps readValue method.
   *
   * @param json jsonString.
   * @return Invoice object based on json string.
   */
  public Invoice toInvoice(String json) {
    try {
      return jsonMapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      throw new DbException("Internal invoice processing error");
    }
  }
}

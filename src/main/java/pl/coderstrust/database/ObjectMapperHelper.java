package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.coderstrust.model.Invoice;

import java.io.IOException;

public class ObjectMapperHelper {

  private ObjectMapper jsonMapper;

  public ObjectMapperHelper() {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
  }

  public String toJson(Object value) {
    try {
      return jsonMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR);
    }
  }

  public Invoice toInvoice(String json) {
    try {
      return jsonMapper.readValue(json, Invoice.class);
    } catch (IOException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR);
    }
  }
}

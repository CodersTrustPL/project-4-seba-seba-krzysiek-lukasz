package pl.coderstrust.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
   * getter For configure ObjectMapper provider
   *
   * @return configured ObjectMapper provider
   */
  public ObjectMapper getJsonMapper() {
    return jsonMapper;

  }
}

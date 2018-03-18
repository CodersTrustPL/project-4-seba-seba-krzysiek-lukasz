package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class ObjectMapperHelper<T> {

  private ObjectMapper jsonMapper;
  private Class<T> entryClass;

  public ObjectMapperHelper(Class<T> entryClass) {
    jsonMapper = new ObjectMapper();
    jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    jsonMapper.registerModule(new JavaTimeModule());
    this.entryClass = entryClass;
  }

  public String toJson(T object) {
    try {
      return jsonMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      //TODO add logging.
    }
  }

  public T toObject(String json) {
    try {
      return jsonMapper.readValue(json, entryClass);
    } catch (IOException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      //TODO add logging.
    }
  }

  public String idToJson(long id) {
    try {
      return jsonMapper.writeValueAsString(id);
    } catch (JsonProcessingException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      //TODO add logging.
    }
  }

  public long jsonToId(String json) {
    try {
      return jsonMapper.readValue(json, new TypeReference<Long>() {
      });
    } catch (IOException e) {
      throw new DbException(ExceptionMsg.INTERNAL_PROCESSING_ERROR, e);
      //TODO add logging.
    }
  }

}


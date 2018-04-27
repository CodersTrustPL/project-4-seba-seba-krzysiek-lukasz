package pl.coderstrust.database.hibernate;

import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;

public class LocalDateTimeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public java.sql.Date convertToDatabaseColumn(java.time.LocalDate attribute) {
    return attribute == null ? null : java.sql.Date.valueOf(attribute);
  }

  @Override
  public java.time.LocalDate convertToEntityAttribute(java.sql.Date dbData) {
    return dbData == null ? null : dbData.toLocalDate();
  }
}
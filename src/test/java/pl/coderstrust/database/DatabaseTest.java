package pl.coderstrust.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import pl.coderstrust.model.Invoice;

public abstract class DatabaseTest {

  ObjectMapper mapper;
  public abstract Database getDatabase();
  Database database;

  @Before
  public void initializeTestingObjects() {
    mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
  }



  @Test
  public void addInvoice() throws Exception {
  }

  @Test
  public void deleteInvoiceById() throws Exception {
  }

  @Test
  public void getInvoiceById() throws Exception {
  }

  @Test
  public void updateInvoice() throws Exception {
  }

  @Test
  public void getInvoices() throws Exception {
  }

  boolean isInvoicesContentEqual(Invoice inv1, Invoice inv2) {
    try {
      return mapper.writeValueAsString(inv1).equals(mapper.writeValueAsString(inv2));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
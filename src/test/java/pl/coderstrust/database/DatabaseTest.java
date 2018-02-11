package pl.coderstrust.database;

import static org.junit.Assert.assertArrayEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testHelpers.TestCasesGenerator;

import java.util.NoSuchElementException;

public abstract class DatabaseTest {

  private ObjectMapper mapper =  new ObjectMapper();
  private TestCasesGenerator generator = new TestCasesGenerator();

  public abstract Database getDatabase();
  Database database = getDatabase();

  @Before
  public void prepareDatabase(){
    database.cleanDatabase();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void shouldAddAndGetInvoices() throws Exception {
    int invoiceEntriesCount = 3;
    int invoicesCount = 10;
    Invoice testInvoice;
    String[] should = new String[invoicesCount];
    String[] output = new String[invoicesCount];
    for (int i = 0; i < invoicesCount; i++) {
      testInvoice = generator.getTestInvoice(i, invoiceEntriesCount);
      testInvoice.setSystemId(i);
      should[i] =   mapper.writeValueAsString(testInvoice);
      database.addInvoice(testInvoice);
    }
    for (int i = 0; i < invoicesCount; i++) {
      output[i] = mapper.writeValueAsString(database.getInvoiceById(i));
    }
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @Test
  public void shouldDeleteInvoicesById() throws Exception {

    int invoiceEntriesCount = 3;
    int invoicesCount = 10;
    Invoice testInvoice;

    for (int i = 0; i < invoicesCount; i++) {
      testInvoice = generator.getTestInvoice(i, invoiceEntriesCount);
      testInvoice.setSystemId(i);
      database.addInvoice(testInvoice);
    }
    for (int i = 0; i < invoicesCount; i++) {
      database.deleteInvoiceById(i);
    }
    for (int i = 0; i < invoicesCount; i++) {
      expectedException.expect(NoSuchElementException.class);
      database.getInvoiceById(i);
    }
  }
  @Test
  public void shouldUpdateInvoices(){
    int invoiceEntriesCount = 3;
    int invoicesCount = 10;
    Invoice testInvoice;
    String[] output = new String[invoicesCount];
    String[] should = new String[invoicesCount];
    for (int i = 0; i < invoicesCount; i++) {
      testInvoice = generator.getTestInvoice(i, invoiceEntriesCount);
      testInvoice.setSystemId(i);
      database.addInvoice(testInvoice);
    }
    try{
      for (int i = 0; i < invoicesCount; i++) {
        testInvoice = generator.getTestInvoice(i + 1, invoiceEntriesCount);
        testInvoice.setSystemId(i);
        should[i] = mapper.writeValueAsString(testInvoice);
        database.updateInvoice(testInvoice);
      }
      for (int i = 0; i < invoicesCount; i++) {
        output[i] = mapper.writeValueAsString(database.getInvoiceById(i));
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    assertArrayEquals(should, output);
  }
  @Test
  public void shouldCleanDatabase(){
    int invoiceEntriesCount = 3;
    int invoicesCount = 10;
    Invoice testInvoice;

    for (int i = 0; i < invoicesCount; i++) {
      testInvoice = generator.getTestInvoice(i, invoiceEntriesCount);
      testInvoice.setSystemId(i);
      database.addInvoice(testInvoice);
    }

    database.cleanDatabase();
    for (int i = 0; i < invoicesCount; i++) {
      expectedException.expect(NoSuchElementException.class);
      database.getInvoiceById(i);
    }
  }
}
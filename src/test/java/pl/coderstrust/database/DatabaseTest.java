package pl.coderstrust.database;

import static org.junit.Assert.assertArrayEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.util.NoSuchElementException;

public abstract class DatabaseTest {

  private static final int INVOICE_ENTRIES_COUNT = 3;
  private static final int INVOICES_COUNT = 10;

  private ObjectMapper mapper = new ObjectMapper();
  protected TestCasesGenerator generator = new TestCasesGenerator();
  private String[] should;
  private String[] output;
  protected Invoice testInvoice;
  protected Database database = getDatabase();

  public abstract Database getDatabase();


  /**
   * Cleaning of database and preparation of mapper before each test.
   */
  @Before
  public void defaultGiven() {
    should = new String[INVOICES_COUNT];
    output = new String[INVOICES_COUNT];
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    database.cleanDatabase();
    expectedException = ExpectedException.none();
    for (int i = 0; i < INVOICES_COUNT; i++) {
      testInvoice = generator.getTestInvoice(i, INVOICE_ENTRIES_COUNT);
      testInvoice.setSystemId(i);
      database.addInvoice(testInvoice);
      try {
        should[i] = mapper.writeValueAsString(testInvoice);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void shouldAddAndGetInvoices() throws Exception {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      output[i] = mapper.writeValueAsString(database.getInvoiceById(i));
    }

    //then
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException expectedException;

  @Test
  public void shouldDeleteInvoicesById() throws Exception {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      database.deleteInvoiceById(i);
    }

    //then
    for (int i = 0; i < INVOICES_COUNT; i++) {
      expectedException.expect(NoSuchElementException.class);
      database.getInvoiceById(i);
    }
  }

  @Test
  public void shouldUpdateInvoices() {
    //when
    try {
      for (int i = 0; i < INVOICES_COUNT; i++) {
        testInvoice = generator.getTestInvoice(i + 1, INVOICE_ENTRIES_COUNT);
        testInvoice.setSystemId(i);
        should[i] = mapper.writeValueAsString(testInvoice);
        database.updateInvoice(testInvoice);
      }

      //then
      for (int i = 0; i < INVOICES_COUNT; i++) {
        output[i] = mapper.writeValueAsString(database.getInvoiceById(i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test()
  public void shouldCleanDatabase() {
    //when
    database.cleanDatabase();

    //then
    for (int i = 0; i < INVOICES_COUNT; i++) {
      thrown.expect(NoSuchElementException.class);
      database.getInvoiceById(i);
    }
  }
}
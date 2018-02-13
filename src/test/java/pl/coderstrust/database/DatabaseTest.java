package pl.coderstrust.database;

import static org.junit.Assert.assertArrayEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.database.memory.InvoiceProcessingException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.TestCasesGenerator;

public abstract class DatabaseTest {

  private static final int INVOICE_ENTRIES_COUNT = 3;
  protected static final int INVOICES_COUNT = 10;

  private ObjectMapperProvider mapper = new ObjectMapperProvider();
  protected TestCasesGenerator generator = new TestCasesGenerator();
  private String[] should=new String[INVOICES_COUNT];
  private String[] output=new String[INVOICES_COUNT];
  protected Invoice testInvoice;
  protected Database database;

  public abstract Database getCleanDatabase();

  @Before
  public void defaultGiven() {
    database = getCleanDatabase();
    for (int i = 0; i < INVOICES_COUNT; i++) {
      testInvoice = generator.getTestInvoice(i, INVOICE_ENTRIES_COUNT);
      testInvoice.setSystemId(i);
      database.addInvoice(testInvoice);
      try {
        should[i] = mapper.getJsonMapper().writeValueAsString(testInvoice);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void shouldAddAndGetInvoices() throws Exception {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      output[i] = mapper.getJsonMapper().writeValueAsString(database.getInvoiceById(i));
    }

    //then
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException atDeletedInvoiceAccess= ExpectedException.none();

  @Test
  public void shouldDeleteInvoicesById() throws Exception {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      database.deleteInvoiceById(i);
    }

    //then
    for (int i = 0; i < INVOICES_COUNT; i++) {
      atDeletedInvoiceAccess.expect(InvoiceProcessingException.class);
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
        should[i] = mapper.getJsonMapper().writeValueAsString(testInvoice);
        database.updateInvoice(testInvoice);
      }

      //then
      for (int i = 0; i < INVOICES_COUNT; i++) {
        output[i] = mapper.getJsonMapper().writeValueAsString(database.getInvoiceById(i));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException atNonexistantInvoiceAccess = ExpectedException.none();

  @Test()
  public void shouldCleanDatabase() {
    //when
    Database cleanDatabase = getCleanDatabase();

    //then
    for (int i = 0; i < INVOICES_COUNT; i++) {
      atNonexistantInvoiceAccess.expect(InvoiceProcessingException.class);
      cleanDatabase.getInvoiceById(i);
    }
  }
}
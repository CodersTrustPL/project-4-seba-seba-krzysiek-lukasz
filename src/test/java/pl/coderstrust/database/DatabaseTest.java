package pl.coderstrust.database;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.util.ArrayList;
import java.util.Random;

public abstract class DatabaseTest {

  private static final int INVOICE_ENTRIES_COUNT = 3;
  protected static final int INVOICES_COUNT = 10;

  private ObjectMapperHelper mapper = new ObjectMapperHelper();
  protected TestCasesGenerator generator = new TestCasesGenerator();
  protected Invoice testInvoice;
  protected ArrayList<Invoice> testInvoiceList = new ArrayList<>(INVOICES_COUNT);
  protected Database database;

  private String[] should = new String[INVOICES_COUNT];
  private String[] output = new String[INVOICES_COUNT];
  protected long[] invoiceIds = new long[INVOICES_COUNT];

  public abstract Database getCleanDatabase();

  @Before
  public void defaultGiven() {
    database = getCleanDatabase();
    for (int i = 0; i < INVOICES_COUNT; i++) {
      testInvoice = generator.getTestInvoice(i, INVOICE_ENTRIES_COUNT);
      invoiceIds[i] = database.addInvoice(testInvoice);
      testInvoice.setId(invoiceIds[i]);
      should[i] = mapper.toJson(testInvoice);
    }
  }

  @Test
  public void shouldAddAndGetSingleInvoice() {
    //given
    database = getCleanDatabase();
    long invoiceId = database.addInvoice(testInvoice);

    //when
    String output = mapper.toJson(database.getInvoiceById(invoiceId));
    String should = mapper.toJson(testInvoice);

    //then
    assertEquals(should, output);
  }

  @Rule
  public ExpectedException atDeletedInvoiceAccess = ExpectedException.none();

  @Test
  public void shouldDeleteSingleInvoiceById() throws Exception {
    //given
    database = getCleanDatabase();
    long invoiceId = database.addInvoice(testInvoice);

    //when
    database.deleteInvoice(invoiceId);

    //then
    atDeletedInvoiceAccess.expect(DbException.class);
    database.getInvoiceById(invoiceId);
  }

  @Test
  public void shouldUpdateSingleInvoice() {
    //given
    database = getCleanDatabase();
    long invoiceId = database.addInvoice(testInvoice);

    //when
    testInvoice = generator.getTestInvoice(INVOICE_ENTRIES_COUNT + 1, INVOICE_ENTRIES_COUNT);
    testInvoice.setId(invoiceId);
    database.updateInvoice(testInvoice);
    String should = mapper.toJson(testInvoice);
    String output = mapper.toJson(database.getInvoiceById(invoiceId));

    //then
    assertEquals(output, should);
  }


  @Test
  public void shouldAddAndGetSeveralInvoices() {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      output[i] = mapper.toJson(database.getInvoiceById(invoiceIds[i]));
    }
    //then
    assertArrayEquals(should, output);
  }

  @Rule
  public ExpectedException atDeletedInvoicesAccess = ExpectedException.none();

  @Test
  public void shouldDeleteSeveralInvoicesById() throws Exception {
    //when
    for (int i = 0; i < INVOICES_COUNT; i++) {
      database.deleteInvoice(invoiceIds[i]);
    }

    //then
    for (int i = 0; i < INVOICES_COUNT; i++) {
      atDeletedInvoicesAccess.expect(DbException.class);
      database.getInvoiceById(invoiceIds[i]);
    }
  }

  @Test
  public void shouldUpdateSeveralInvoices() {
    //when
    try {
      for (int i = 0; i < INVOICES_COUNT; i++) {
        testInvoice = generator.getTestInvoice(i + 1, INVOICE_ENTRIES_COUNT);
        testInvoice.setId(invoiceIds[i]);
        should[i] = mapper.toJson(testInvoice);
        database.updateInvoice(testInvoice);
      }

      //then
      for (int i = 0; i < INVOICES_COUNT; i++) {
        output[i] = mapper.toJson(database.getInvoiceById(invoiceIds[i]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertArrayEquals(should, output);
  }

  @Test
  public void shouldGetAllInvoices() {
    //then
    ArrayList<Invoice> allInvoices = new ArrayList<>(database.getInvoices());
    String[] output = new String[INVOICES_COUNT];

    for (int i = 0; i < INVOICES_COUNT; i++) {
      output[i] = mapper.toJson(allInvoices.get(i));
    }

    //should
    assertArrayEquals(should, output);
  }

  @Test
  public void shouldReturnTrueWhenInvoiceExist() {
    Random randomInvoiceId = new Random();
    assertTrue(database.idExist(randomInvoiceId.nextInt(INVOICES_COUNT)));
  }

  @Test
  public void shouldReturnFalseWhenInvoiceDoesNotExist() {
    assertFalse(database.idExist(INVOICES_COUNT + INVOICES_COUNT));
  }

  @Test
  public void shouldReturnFalseForRemovedInvoice() {
    database.deleteInvoice(INVOICES_COUNT - 1);
    assertFalse(database.idExist(INVOICES_COUNT - 1));
  }
}
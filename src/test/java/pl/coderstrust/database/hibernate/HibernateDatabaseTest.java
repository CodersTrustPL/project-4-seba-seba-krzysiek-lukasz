package pl.coderstrust.database.hibernate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.TestCasesGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HibernateDatabaseTest //extends DatabaseTest
 {
     protected static final int INVOICES_COUNT = 2;
     private static final int INVOICE_ENTRIES_COUNT = 1;
     @Rule
     public ExpectedException atDeletedInvoiceAccess = ExpectedException.none();
     protected TestCasesGenerator generator = new TestCasesGenerator();
     protected Invoice givenInvoice;
     protected Database givenDatabase;
     protected long[] invoiceIds = new long[INVOICES_COUNT];
     private ObjectMapperHelper mapper = new ObjectMapperHelper<Invoice>(Invoice.class);
     private String[] expected = new String[INVOICES_COUNT];
     private String[] output = new String[INVOICES_COUNT];

     @Before
     public void defaultGiven() {
         for (int i = 0; i < INVOICES_COUNT; i++) {
             givenInvoice = generator.getTestInvoice(i, INVOICE_ENTRIES_COUNT);
             givenInvoice.setId(invoiceIds[i]);
         }
     }

     @Autowired
     HibernateDatabase database;

    @Test
    public void addEntry() {
        long id = database.addEntry(givenInvoice);
        System.out.println(id);
    }

    @Test
    public void deleteEntry() {
    }

    @Test
    public void getEntryById() {
    }

    @Test
    public void updateEntry() {
    }

    @Test
    public void getEntries() {
    }

    @Test
    public void idExist() {
    }
}
package pl.coderstrust.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.coderstrust.database.Database;
import pl.coderstrust.helpers.TestCasesGenerator;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.pdf.PdfDateTimeProvider;
import pl.coderstrust.service.pdf.PdfFontsProvider;
import pl.coderstrust.service.pdf.PdfGenerator;

import java.io.InputStream;

@SpringBootTest
public class InvoiceServiceGetPdfReportTest {

  private static final String PDF_CREATION_DATE = "2018/05/28 17:59:42";
  private static final String SAMPLE_PDF_PATH = "src/test/resources/pdf/sampleInvoice.pdf";
  private static final int PDF_PAGES_COUNT = 1;
  private static final int INVOICE_ID = 1;
  private static final int INVOICE_NUMBER = 1;
  private static final int INVOICE_ENTRIES_COUNT = 5;

  private Database database;
  private PdfDateTimeProvider pdfDateTimeProvider;

  private CompanyService companyService;
  private InvoiceService invoiceService;

  @Before
  public void testSetUp() {
    database = Mockito.mock(Database.class);
    companyService = Mockito.mock(CompanyService.class);
    pdfDateTimeProvider = Mockito.mock(PdfDateTimeProvider.class);
    PdfGenerator pdfGenerator = new PdfGenerator(pdfDateTimeProvider, new PdfFontsProvider());
    invoiceService = new InvoiceService(companyService, database, pdfGenerator);
  }

  @Test
  public void shouldReturnPdfWithCorrectContent() throws Exception {
    //given
    TestCasesGenerator generator = new TestCasesGenerator();
    Invoice invoice = generator.getTestInvoice(INVOICE_NUMBER, INVOICE_ENTRIES_COUNT);
    when(database.getEntryById(INVOICE_ID)).thenReturn(invoice);
    when(pdfDateTimeProvider.getDateTime()).thenReturn(PDF_CREATION_DATE);

    //when
    InputStream pdfStream = invoiceService.getPdfReport(INVOICE_ID);

    byte[] pdfContent = new byte[pdfStream.available()];
    pdfStream.read(pdfContent);

    String shouldContent = pdfFileToString();
    String generatedContent = pdfByteArrayToString(pdfContent);

    //then
    assertThat(generatedContent, is(equalTo(shouldContent)));
  }

  private String pdfFileToString() throws Exception {
    return pdfReaderToString(new PdfReader(InvoiceServiceGetPdfReportTest.SAMPLE_PDF_PATH));
  }

  private String pdfByteArrayToString(byte[] input) throws Exception {
    return pdfReaderToString(new PdfReader(input));
  }

  private String pdfReaderToString(PdfReader reader) throws Exception {
    PdfReaderContentParser parser = new PdfReaderContentParser(reader);
    TextExtractionStrategy strategy;
    strategy = parser.processContent(PDF_PAGES_COUNT, new SimpleTextExtractionStrategy());
    String content = strategy.getResultantText();
    reader.close();
    return content;
  }
}
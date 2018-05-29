package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.pdf.PdfGenerator;

@Service
public class InvoiceService extends AbstractService<Invoice> {

  private CompanyService companyService;

  @Autowired
  public InvoiceService(CompanyService companyService,
      @Qualifier("invoicesDatabase") Database<Invoice> dbInvoices, PdfGenerator pdfGenerator) {
    this.companyService = companyService;
    super.entriesDb = dbInvoices;
    super.pdfGenerator = pdfGenerator;
  }

  @Override
  public long addEntry(Invoice entry) {
    entry.setId(-1);
    checkIfCompaniesExistInDbAndAddIfNot(entry);
    super.setDefaultEntryNameIfEmpty(entry);
    return entriesDb.addEntry(entry);
  }

  @Override
  public void updateEntry(Invoice entry) {
    checkIfCompaniesExistInDbAndAddIfNot(entry);
    super.setDefaultEntryNameIfEmpty(entry);
    super.updateEntry(entry);
  }

  private void checkIfCompaniesExistInDbAndAddIfNot(Invoice invoice) {
    if (companyService.nipExist(invoice.getBuyer().getNip())) {
      invoice.setBuyer(companyService.getEntryByNip(invoice.getBuyer().getNip()));
    } else {
      long buyerId = companyService.addEntry(invoice.getBuyer());
      invoice.setBuyer(companyService.findEntry(buyerId));
    }
    if (companyService.nipExist(invoice.getSeller().getNip())) {
      invoice.setSeller(companyService.getEntryByNip(invoice.getSeller().getNip()));
    } else {
      long buyerId = companyService.addEntry(invoice.getSeller());
      invoice.setSeller(companyService.findEntry(buyerId));
    }
  }
}
package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.pdfservice.PdfGenerator;

@Service
public class InvoiceService extends AbstractService<Invoice> {

  Database<Company> companyDatabase;

  @Autowired
  public InvoiceService(@Qualifier("companiesDatabase") Database<Company> dbCompany,
      @Qualifier("invoicesDatabase") Database<Invoice> dbInvoices, PdfGenerator pdfGenerator) {
    this.companyDatabase = dbCompany;
    super.entriesDb = dbInvoices;
    super.pdfGenerator = pdfGenerator;
  }

  @Override
  public long addEntry(Invoice entry) {
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
    if (companyDatabase.idExist(invoice.getBuyer().getId())) {
      invoice.setBuyer(companyDatabase.getEntryById(invoice.getBuyer().getId()));
    } else {
      long buyerId = companyDatabase.addEntry(invoice.getBuyer());
      invoice.setBuyer(companyDatabase.getEntryById(buyerId));
    }
    if (companyDatabase.idExist(invoice.getSeller().getId())) {
      invoice.setSeller(companyDatabase.getEntryById(invoice.getSeller().getId()));
    } else {
      long sellerId = companyDatabase.addEntry(invoice.getSeller());
      invoice.setSeller(companyDatabase.getEntryById(sellerId));
    }
  }
}

package pl.coderstrust.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceByCompanyFilter implements EntriesFilter<Invoice> {

  @Qualifier("filterWithCompanies")
  Database<Company> dbCompanies;

  public InvoiceByCompanyFilter(@Qualifier("filterWithCompanies") Database<Company> dbCompanies) {
    this.dbCompanies = dbCompanies;
  }

  @Override
  public boolean hasField(Invoice entry, long companyId) {
    return hasBuyerOrSeller(entry, companyId);
  }

  @Override
  public boolean hasObject(Invoice entry, Object company) {
    return hasBuyerOrSeller(entry, (Company) company);
  }

  @Override
  public boolean hasObjectById(Invoice entry, long companyId) {
    return hasBuyerOrSeller(entry, dbCompanies.getEntryById(companyId));
  }

  @Override
  public List<Invoice> filterByField(List<Invoice> entries, long companyId) {
    return entries.stream()
        .filter(line -> hasBuyerOrSeller(line, companyId))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private boolean hasBuyerOrSeller(Invoice entry, long filterId) {
    if (entry.getSeller().getId() == filterId ||
        entry.getBuyer().getId() == filterId) {
      return true;
    } else {
      return false;
    }
  }

  private boolean hasBuyerOrSeller(Invoice entry, Company company) {
    if (entry.getSeller().equals(company) ||
        entry.getBuyer().equals(company)) {
      return true;
    } else {
      return false;
    }
  }
}

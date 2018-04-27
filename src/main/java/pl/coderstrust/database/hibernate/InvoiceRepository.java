package pl.coderstrust.database.hibernate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@Repository
interface InvoiceRepository<T extends Invoice> extends CrudRepository<T, Long> {
  @Query("SELECT coalesce(max(i.id), 0) FROM Invoice i")
  Long getMaxId();
}

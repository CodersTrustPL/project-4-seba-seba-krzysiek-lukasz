package pl.coderstrust.database.hibernate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@Repository
interface InvoiceRepository<T extends Invoice> extends CrudRepository<T, Long> {
}

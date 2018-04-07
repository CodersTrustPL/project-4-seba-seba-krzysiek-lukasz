package pl.coderstrust.database.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@Repository
interface InvoiceRepository<T extends Invoice> extends JpaRepository<T, Long> {
}

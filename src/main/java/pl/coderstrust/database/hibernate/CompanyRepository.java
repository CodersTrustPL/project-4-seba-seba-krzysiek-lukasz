package pl.coderstrust.database.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderstrust.model.Company;

public interface CompanyRepository <T extends Company> extends JpaRepository<T, Long> {
  @Query("SELECT coalesce(max(i.id), 0) FROM Invoice i")
  Long getMaxId();
}

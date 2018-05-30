package pl.coderstrust.database.hibernate;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Company;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

  Company getByNip(String nip);
}

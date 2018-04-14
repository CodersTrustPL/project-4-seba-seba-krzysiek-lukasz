package pl.coderstrust.model;

    import java.time.LocalDate;

public interface WithNameIdIssueDate {

  String getName();

  void setName(String name);

  Long getId();

  void setId(Long id);

  LocalDate getIssueDate();

  void setIssueDate(LocalDate issueDate);
}

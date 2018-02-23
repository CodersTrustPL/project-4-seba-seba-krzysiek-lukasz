package pl.coderstrust.e2e.model;


public class CompanyBuilder {

  private Company company = new Company();

  public CompanyBuilder(String name) {
    company.setName(name);
  }

  public CompanyBuilder setAddress(String address) {
    company.setAddress(address);
    return this;
  }

  public CompanyBuilder setCity(String city) {
    company.setCity(city);
    return this;
  }

  public CompanyBuilder setZipCode(String zipCode) {
    company.setZipCode(zipCode);
    return this;
  }

  public CompanyBuilder setNip(String nip) {
    company.setNip(nip);
    return this;
  }

  public CompanyBuilder setBankAccoutNumber(String bankAccoutNumber) {
    company.setBankAccoutNumber(bankAccoutNumber);
    return this;
  }

  public Company build() {
    return company;
  }
}
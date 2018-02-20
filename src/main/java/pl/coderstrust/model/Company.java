package pl.coderstrust.model;

public class Company {

  private String name;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccoutNumber;


  public Company() {
  }

  public Company(String name, String address, String city, String zipCode, String nip,
      String bankAccoutNumber) {
    this.name = name;
    this.address = address;
    this.city = city;
    this.zipCode = zipCode;
    this.nip = nip;
    this.bankAccoutNumber = bankAccoutNumber;
  }

  public Company(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getNip() {
    return nip;
  }

  public void setNip(String nip) {
    this.nip = nip;
  }

  public String getBankAccoutNumber() {
    return bankAccoutNumber;
  }

  public void setBankAccoutNumber(String bankAccoutNumber) {
    this.bankAccoutNumber = bankAccoutNumber;
  }

  @Override
  public String toString() {
    return "Company{"
        + "name='" + name + '\''
        + ", address='" + address + '\''
        + ", city='" + city + '\''
        + ", zipCode='" + zipCode + '\''
        + ", nip='" + nip + '\''
        + ", bankAccoutNumber='" + bankAccoutNumber + '\'' + '}';
  }
}

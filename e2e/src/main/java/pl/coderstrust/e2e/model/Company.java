package pl.coderstrust.e2e.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
  private String name;
  private String address;
  private String city;
  private String zipCode;
  private String nip;
  private String bankAccoutNumber;


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

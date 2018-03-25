package pl.coderstrust.service;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Company;

@RestController
@RequestMapping("company")
public class CompanyController extends AbstractController<Company> {

  public CompanyController(CompanyService companyService, CompanyDummyFilter dummyFilter) {
    super.service = companyService;
    super.byCustomerFilter = dummyFilter;
  }
}

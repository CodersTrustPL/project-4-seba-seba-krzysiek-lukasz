package pl.coderstrust.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.helpers.InvoicesWithSpecifiedData;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.CompanyBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser
public class CompanyControllerIntegrationTest {

  private static final String GET_COMPANIES_BY_DATE = "getCompanyByDate";
  private static final String GET_COMPANY_BY_ID_METHOD = "getCompanyById";
  private static final String REMOVE_COMPANY_METHOD = "removeCompany";
  private static final String ADD_COMPANY_METHOD = "addCompany";
  private static final String DEFAULT_PATH = "/v2/company";
  private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
  private static final String INT_FROM_STRING_REGEX_PATTERN = "([0-9])+";

  private Pattern extractIntFromString = Pattern.compile(INT_FROM_STRING_REGEX_PATTERN);

  private Company testCompany = InvoicesWithSpecifiedData.getPolishCompanySeller();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void shouldAddCompany() throws Exception {
    //given
    Company expectedCompany = testCompany;
    //when
    String respone = this.mockMvc
        .perform(post(DEFAULT_PATH).content(json(testCompany)).contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_COMPANY_METHOD)).andExpect(status().isOk()).andReturn()
        .getResponse().getContentAsString();
    //then
    expectedCompany.setId(getEntryIdFromServiceResponse(respone));

    String response = this.mockMvc.perform(get(DEFAULT_PATH))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_COMPANIES_BY_DATE)).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<Company> companies = getCompaniesFromResponse(response);
    assertThat(companies.size(), is(1));
    assertThat(companies.get(0), is(equalTo(testCompany)));
  }

  @Test
  public void shouldReturnErrorCausedByEmptyFields() throws Exception {
    //given
    Company givenCompany = new CompanyBuilder("Test").setId(1).build();
    givenCompany.setName(null);
    //then
    this.mockMvc.perform(post(DEFAULT_PATH).content(json(givenCompany)).contentType(CONTENT_TYPE))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(handler().methodName(ADD_COMPANY_METHOD)).andExpect(content().string(
        "[\"Company name is empty.\"" + ",\"Company address is empty.\",\"Company city is empty.\","
            + "\"Company NIP number is empty.\",\"Company zip code is empty.\""
            + ",\"Company bank account number is empty.\"]"));
  }

  @Test
  public void shouldReturnCompanyById() throws Exception {
    //given
    Company expectedCompany = testCompany;
    //when
    String response = this.mockMvc
        .perform(post(DEFAULT_PATH).content(json(testCompany)).contentType(CONTENT_TYPE))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    //then
    expectedCompany.setId(getEntryIdFromServiceResponse(response));
    response = this.mockMvc.perform(get(DEFAULT_PATH + "/1"))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_COMPANY_BY_ID_METHOD)).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    Company returnedCompany = jsonToCompany(response);
    assertThat(returnedCompany, is(equalTo(testCompany)));
  }

  @Test
  public void shouldUpdateCompany() throws Exception {
    //given
    String response = this.mockMvc
        .perform(post(DEFAULT_PATH).content(json(testCompany)).contentType(CONTENT_TYPE))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Company companyToUpdate = testCompany;
    companyToUpdate.setId(getEntryIdFromServiceResponse(response));
    companyToUpdate.setName("Szpital dla roślin");
    //when
    this.mockMvc
        .perform(put(DEFAULT_PATH + "/1").content(json(companyToUpdate)).contentType(CONTENT_TYPE))
        .andExpect(status().isOk());
    //then
    response = this.mockMvc.perform(get(DEFAULT_PATH + "/1"))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_COMPANY_BY_ID_METHOD)).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    Company returnedCompany = jsonToCompany(response);
    assertThat(returnedCompany, is(equalTo(companyToUpdate)));
  }

  @Test
  public void shouldReturnCompanyListByStringKey() throws Exception {
    //given
    Company polishCompanyBuyer = InvoicesWithSpecifiedData.getPolishCompanyBuyer();
    String elementOfNameOfBuyerCompany = "Marian";

    this.mockMvc.perform(post(DEFAULT_PATH).content(json(testCompany)).contentType(CONTENT_TYPE))
        .andExpect(status().isOk());

    this.mockMvc
        .perform(post(DEFAULT_PATH).content(json(polishCompanyBuyer)).contentType(CONTENT_TYPE))
        .andExpect(status().isOk());

    //when
    String response = this.mockMvc
        .perform(get(DEFAULT_PATH + "/name?name=" + elementOfNameOfBuyerCompany))
        .andExpect(content().contentType(CONTENT_TYPE)).andExpect(status().isOk()).andReturn()
        .getResponse().getContentAsString();

    List<Company> returnedCompanies = getCompaniesFromResponse(response);

    assertThat(returnedCompanies.size(), is(1));
    assertTrue(returnedCompanies.get(0).getName().contains(elementOfNameOfBuyerCompany));
  }

  @Test
  public void shouldDeleteCompany() throws Exception {
    //given
    this.mockMvc.perform(post(DEFAULT_PATH).content(json(testCompany)).contentType(CONTENT_TYPE))
        .andExpect(status().isOk());
    //when
    this.mockMvc.perform(delete(DEFAULT_PATH + "/1"))
        .andExpect(handler().methodName(REMOVE_COMPANY_METHOD)).andExpect(status().isOk());
    //then
    String response = this.mockMvc.perform(get(DEFAULT_PATH))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_COMPANIES_BY_DATE)).andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<Company> returnedCompanies = getCompaniesFromResponse(response);
    assertThat(returnedCompanies.size(), is(0));
  }

  private String json(Company company) throws Exception {
    return mapper.writeValueAsString(company);
  }

  private Company jsonToCompany(String jsonCompany) throws Exception {
    return mapper.readValue(jsonCompany, Company.class);
  }

  private List<Company> getCompaniesFromResponse(String response) throws Exception {
    return mapper.readValue(response,
        mapper.getTypeFactory().constructCollectionType(List.class, Company.class));
  }

  private long getEntryIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }
}
package pl.coderstrust.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InvoiceControllerWithCompanyFilterIntegrationTests {

  private static final String GET_ENTRY_BY_DATE_METHOD = "getEntryByDate";
  private static final String GET_ENTRY_BY_ID_METHOD = "getEntryById";
  private static final String REMOVE_ENTRY_METHOD = "removeEntry";
  private static final String ADD_ENTRY_METHOD = "addEntry";
  private static final String DEFAULT_PATH_INVOICE = "/invoice";
  private static final String DEFAULT_PATH_COMPANY = "/company";
  private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
  private static final  String INT_FROM_STRING_REGEX_PATTERN = "([0-9])+";

  private Pattern extractIntFromString = Pattern.compile(INT_FROM_STRING_REGEX_PATTERN);

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private TestCasesGenerator generator;

  @Test
  public void shouldAddInvoiceWhenBuyerMatchesCompanyId() throws Exception {
    //when
    Invoice givenInvoice = generator.getTestInvoice(1, 1);
    long buyerId = addCompanyToDb(givenInvoice.getBuyer());
    givenInvoice.getBuyer().setId(buyerId);

    this.mockMvc
        .perform(post(DEFAULT_PATH_INVOICE + "/" + String.valueOf(buyerId))
            .content(mapper.writeValueAsString(givenInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldAddInvoiceWhenSellerMatchesCompanyId() throws Exception {
    //when
    Invoice givenInvoice = generator.getTestInvoice(1, 1);
    long sellerId = addCompanyToDb(givenInvoice.getSeller());
    givenInvoice.getSeller().setId(sellerId);

    this.mockMvc
        .perform(post(DEFAULT_PATH_INVOICE + "/" + String.valueOf(sellerId))
            .content(mapper.writeValueAsString(givenInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnErrorWhenCompaniesNotMatchCompanyId() throws Exception {
    //when
    Invoice givenInvoice = generator.getTestInvoice(1, 1);
    long sellerId = addCompanyToDb(givenInvoice.getSeller());
    givenInvoice.getSeller().setId(sellerId + 1);

    this.mockMvc
        .perform(post(DEFAULT_PATH_INVOICE + "/" + String.valueOf(sellerId))
            .content(mapper.writeValueAsString(givenInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isBadRequest());
  }

  private long addCompanyToDb(Company company) throws Exception {
    String serviceResponse = this.mockMvc
        .perform(post(DEFAULT_PATH_COMPANY)
            .content(mapper.writeValueAsString(company))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    return getEntryIdFromServiceResponse(serviceResponse);
  }

  long getEntryIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }
}

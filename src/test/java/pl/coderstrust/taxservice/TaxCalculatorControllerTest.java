package pl.coderstrust.taxservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.InvoicesWithSpecifiedData;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaxCalculatorControllerTest {

  private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
  private static final String DEFAULT_PATH = "/invoice";
  private static final String MY_COMPANY_NAME =
      "?companyName=Ferdynand Kiepski i Syn Sp.zoo&startDate=";

  @Autowired
  private TestCasesGenerator generator;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void calculateIncome() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/income" + MY_COMPANY_NAME + "2018-12-01&endDate=2019-12-01"))
        .andExpect(handler().methodName("calculateIncome"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(1170.0)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("1170.00")));
  }

  @Test
  public void calculateCost() throws Exception {
    //given
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/cost" + MY_COMPANY_NAME + "2018-12-01&endDate=2019-06-01"))
        .andExpect(handler().methodName("calculateCost"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(315.0)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("315.00")));
  }

  @Test
  public void calculateIncomeTax() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/incomeTax" + MY_COMPANY_NAME + "2018-12-01&endDate=2019-12-01"))
        .andExpect(handler().methodName("calculateIncomeTax"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(855.0)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("855.00")));
  }


  @Test
  public void calculateIncomeVat() throws Exception {
    //given
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/incVat" + MY_COMPANY_NAME + "2018-12-01&endDate=2019-06-01"))
        .andExpect(handler().methodName("calculateIncomeVat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(72.45)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("72.45")));
  }

  @Test
  public void calculateOutcomeVat() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/outVat" + MY_COMPANY_NAME + "2018-12-01&endDate=2019-12-01"))
        .andExpect(handler().methodName("calculateOutcomeVat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(269.1)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("269.10")));
  }

  @Test
  public void calculateDifferenceVat() throws Exception {
    //given
    for (int i = 1; i <= 12; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setSeller(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    for (int i = 1; i <= 6; i++) {
      Invoice invoice = generator.getTestInvoice(i, 5);
      invoice.setIssueDate(LocalDate.of(2018, 12, 1).plusMonths(i));
      invoice.setBuyer(InvoicesWithSpecifiedData.getPolishCompanySeller());

      this.mockMvc
          .perform(post(DEFAULT_PATH)
              .content(json(invoice))
              .contentType(CONTENT_TYPE))
          .andExpect(status().isOk());
    }
    //when
    String response = this.mockMvc
        .perform(
            get("/diffVat" + MY_COMPANY_NAME + "2018-12-01&endDate=2019-12-01"))
        .andExpect(handler().methodName("calculateDifferenceVat"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(196.65)))
        .andReturn()
        .getResponse()
        .getContentAsString();
    //then
    assertThat(response, is(equalTo("196.65")));
  }

  private String json(Invoice invoice) throws Exception {
    return mapper.writeValueAsString(invoice);
  }
}
package pl.coderstrust.service;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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

import java.time.LocalDate;
import java.util.List;
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
  private static final String INT_FROM_STRING_REGEX_PATTERN = "([0-9])+";

  private Pattern extractIntFromString = Pattern.compile(INT_FROM_STRING_REGEX_PATTERN);
  private Invoice givenInvoice;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private TestCasesGenerator generator;

  @Before
  public void testSetup() {
    givenInvoice = generator.getTestInvoice(1, 1);
  }

  private String addInvoiceUrl(long companyId) {
    return DEFAULT_PATH_INVOICE + "/" + String.valueOf(companyId);
  }

  private String getInvoiceUrl(long invoiceId, long companyId) {
    return DEFAULT_PATH_INVOICE + "/" + String.valueOf(invoiceId) + "/" + String
        .valueOf(companyId);
  }

  @Test
  public void shouldAddInvoiceWhenBuyerMatchesCompanyId() throws Exception {
    //when
    long buyerId = registerBuyerAtDb(givenInvoice);
    this.mockMvc
        .perform(post(addInvoiceUrl(buyerId))
            .content(mapper.writeValueAsString(givenInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isOk());
  }

  private long registerBuyerAtDb(Invoice invoice) throws Exception {
    long buyerId = addCompanyToDb(invoice.getBuyer());
    givenInvoice.getBuyer().setId(buyerId);
    return buyerId;
  }

  private long registerSellerAtDb(Invoice invoice) throws Exception {
    long sellerId = addCompanyToDb(invoice.getSeller());
    givenInvoice.getSeller().setId(sellerId);
    return sellerId;
  }


  @Test
  public void shouldAddInvoiceWhenSellerMatchesCompanyId() throws Exception {
    //when
    long sellerId = registerSellerAtDb(givenInvoice);

    this.mockMvc
        .perform(post(addInvoiceUrl(sellerId))
            .content(mapper.writeValueAsString(givenInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnErrorWhileAddWhenCompaniesNotMatchCompanyId() throws Exception {
    //when
    addCompanyToDb(givenInvoice.getSeller());
    long anotherSellerId = addCompanyToDb(givenInvoice.getSeller());

        this.mockMvc
        .perform(post(addInvoiceUrl(anotherSellerId))
            .content(mapper.writeValueAsString(givenInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldGetEntryByIdWhenSellerMatchesCompanyId() throws Exception {
    //when
    long sellerId = registerSellerAtDb(givenInvoice);
    givenInvoice.setId(addInvoiceToDb(givenInvoice));

    this.mockMvc
        .perform(get(getInvoiceUrl(givenInvoice.getId(),sellerId)))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_ENTRY_BY_ID_METHOD))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(givenInvoice)));
  }

  @Test
  public void shouldGetEntryByIdWhenBuyerMatchesCompanyId() throws Exception {
    //when
    long buyerId = registerBuyerAtDb(givenInvoice);
    givenInvoice.setId(addInvoiceToDb(givenInvoice));

    this.mockMvc
        .perform(get(getInvoiceUrl(givenInvoice.getId(),buyerId)))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_ENTRY_BY_ID_METHOD))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(givenInvoice)));
  }

  @Test
  public void shouldReturnErrorWhileGetEntryByIdWhenBuyerOrSellerIdNotMatchIdAtInvoice()
      throws Exception {
    //when
    long buyerId = addCompanyToDb(givenInvoice.getSeller());
    givenInvoice.getBuyer().setId(buyerId);
    givenInvoice.setId(addInvoiceToDb(givenInvoice));

    this.mockMvc
        .perform(get(getInvoiceUrl(givenInvoice.getId(),buyerId+1)))
        .andExpect(handler().methodName(GET_ENTRY_BY_ID_METHOD))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldGetCorrectEntriesByDateWhenBuyerMatchesCompanyId() throws Exception {
    //given
    long buyerId = registerBuyerAtDb(givenInvoice);
    LocalDate startDate = LocalDate.of(2020, 1, 1);
    LocalDate endDate = LocalDate.of(2060, 1, 1);
    LocalDate invoiceDate = startDate;

    while (invoiceDate.isBefore(endDate)) {
      givenInvoice.setIssueDate(invoiceDate);
      invoiceDate = invoiceDate.plusYears(1);
      addInvoiceToDb(givenInvoice);
    }

    //when
    String response = this.mockMvc
        .perform(get(DEFAULT_PATH_INVOICE + "?filterKey=" + String.valueOf(buyerId)
            + "&startDate=2021-01-01&endDate=2023-01-02"))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_ENTRY_BY_DATE_METHOD))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<Invoice> invoiceList = getInvoicesFromResponse(response);
    //then
    for (Invoice inv : invoiceList) {
      assertTrue((inv.getIssueDate().isBefore(endDate.plusDays(1))
          && (inv.getIssueDate().isAfter(startDate.minusDays(1)))));
    }
  }

  @Test
  public void shouldGetEntryByDateWhenSellerMatchesCompanyId() throws Exception {
    //given
    long sellerId = registerSellerAtDb(givenInvoice);
    LocalDate startDate = LocalDate.of(2020, 1, 1);
    LocalDate endDate = LocalDate.of(2060, 1, 1);
    LocalDate invoiceDate = startDate;

    while (invoiceDate.isBefore(endDate)) {
      givenInvoice.setIssueDate(invoiceDate);
      invoiceDate = invoiceDate.plusYears(1);
      addInvoiceToDb(givenInvoice);
    }

    //when
    String response = this.mockMvc
        .perform(get(DEFAULT_PATH_INVOICE + "?filterKey=" + String.valueOf(sellerId)
            + "&startDate=2021-01-01&endDate=2023-01-02"))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_ENTRY_BY_DATE_METHOD))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    List<Invoice> invoiceList = getInvoicesFromResponse(response);
    //then
    for (Invoice inv : invoiceList) {
      assertTrue((inv.getIssueDate().isBefore(endDate.plusDays(1))
          && (inv.getIssueDate().isAfter(startDate.minusDays(1)))));
    }
  }


  @Test
  public void shouldGetErrorWhileEntryByDateWhenBuyerAndSellerNotMatchesCompanyId()
      throws Exception {
    //given
    long sellerId = registerSellerAtDb(givenInvoice);
    LocalDate startDate = LocalDate.of(2020, 1, 1);
    LocalDate endDate = LocalDate.of(2060, 1, 1);
    LocalDate invoiceDate = startDate;

    while (invoiceDate.isBefore(endDate)) {
      givenInvoice.setIssueDate(invoiceDate);
      invoiceDate = invoiceDate.plusYears(1);
      addInvoiceToDb(givenInvoice);
    }

    //when
    String response = this.mockMvc
        .perform(get(DEFAULT_PATH_INVOICE + "?filterKey=" + String.valueOf(sellerId + 10)
            + "&startDate=" + startDate.toString() + "&endDate=" + endDate.toString()))
        .andExpect(content().contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(GET_ENTRY_BY_DATE_METHOD))
        .andReturn().getResponse().getContentAsString();
    assertThat(response, is(equalTo("[]")));
  }

  @Test
  public void shouldUpdateInvoiceWhenBuyerMatchesId() throws Exception {
    //given
    long buyerId = registerBuyerAtDb(givenInvoice);
    long invoiceId = addInvoiceToDb(givenInvoice);

    Invoice updatedInvoice = generator.getTestInvoice(2, 1);
    updatedInvoice.setId(invoiceId);
    updatedInvoice.setBuyer(givenInvoice.getBuyer());

    //when
    this.mockMvc
        .perform(put(DEFAULT_PATH_INVOICE + "/" + String.valueOf(buyerId) + "/" + invoiceId)
            .content(mapper.writeValueAsString(updatedInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(status().isOk());
    //then
  }

  @Test
  public void shouldUpdateInvoiceWhenSellerMatchesId() throws Exception {
    //given
    long sellerId = registerSellerAtDb(givenInvoice);
    long invoiceId = addInvoiceToDb(givenInvoice);

    Invoice updatedInvoice = generator.getTestInvoice(2, 1);
    updatedInvoice.setId(invoiceId);
    updatedInvoice.setSeller(givenInvoice.getSeller());

    //when
    this.mockMvc
        .perform(put(DEFAULT_PATH_INVOICE + "/" + String.valueOf(sellerId) + "/" + invoiceId)
            .content(mapper.writeValueAsString(updatedInvoice))
            .contentType(CONTENT_TYPE))
        .andExpect(status().isOk());
    //then
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

  private long addInvoiceToDb(Invoice invoice) throws Exception {
    String serviceResponse = this.mockMvc
        .perform(post(DEFAULT_PATH_INVOICE)
            .content(mapper.writeValueAsString(invoice))
            .contentType(CONTENT_TYPE))
        .andExpect(handler().methodName(ADD_ENTRY_METHOD))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    return getEntryIdFromServiceResponse(serviceResponse);
  }


  private long getEntryIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  private Invoice getInvoiceFromServiceResponce(String responce) throws Exception {
    return mapper.readValue(responce, Invoice.class);
  }

  private List<Invoice> getInvoicesFromResponse(String response) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return mapper.readValue(
        response,
        mapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));
  }

}

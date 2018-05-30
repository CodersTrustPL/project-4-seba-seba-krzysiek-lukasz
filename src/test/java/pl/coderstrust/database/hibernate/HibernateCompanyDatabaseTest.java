package pl.coderstrust.database.hibernate;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.helpers.InvoicesWithSpecifiedData;
import pl.coderstrust.model.Company;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HibernateCompanyDatabaseTest {

  private Company testCompany;

  @Mock
  CompanyRepository companyRepository;

  @InjectMocks
  private HibernateCompanyDatabase hibernateCompanyDatabase;

  @Before
  public void setup() {
    testCompany = InvoicesWithSpecifiedData.getPolishCompanySeller();
    testCompany.setId(1);
  }

  @Test
  public void shouldAddEntry() {
    //given
    when(companyRepository.save(testCompany)).thenReturn(testCompany);
    //when
    long returnedId = hibernateCompanyDatabase.addEntry(testCompany);
    //then
    assertThat(returnedId, is(testCompany.getId()));
  }

  @Test
  public void deleteEntry() {
    //given
    doNothing().when(companyRepository).delete(1L);
    //when
    hibernateCompanyDatabase.deleteEntry(1);
    //then
    verify(companyRepository).delete(1L);
  }

  @Test
  public void getEntryById() {
    //given
    when(companyRepository.findOne(1L)).thenReturn(testCompany);
    //when
    Company returnedCompany = hibernateCompanyDatabase.getEntryById(1);
    //then
    assertThat(returnedCompany, is(equalTo(testCompany)));
  }

  @Test
  public void updateEntry() {
    //given
    Company companyToUpdate = testCompany;
    companyToUpdate.setName("Sklep ABC");
    when(companyRepository.save(companyToUpdate)).thenReturn(companyToUpdate);
    //when
    hibernateCompanyDatabase.updateEntry(companyToUpdate);
    //then
    verify(companyRepository).save(companyToUpdate);
  }

  @Test
  public void getEntries() {
    //given
    when(companyRepository.findAll()).thenReturn(Arrays.asList(testCompany));
    //when
    List<Company> returnedCompanies = hibernateCompanyDatabase.getEntries();
    //then
    assertThat(returnedCompanies, is(equalTo(Arrays.asList(testCompany))));
  }

  @Test
  public void idExist() {
    //given
    when(companyRepository.exists(1L)).thenReturn(true);
    //then
    assertTrue(hibernateCompanyDatabase.idExist(1L));
  }
}
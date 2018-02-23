package pl.coderstrust;

import com.sun.deploy.net.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.service.InvoiceBookController;
import pl.coderstrust.testhelpers.TestCasesGenerator;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

  @Autowired
  InvoiceBookController invoiceBookController;

  @Test
  public void contextLoads() {
    assertThat(invoiceBookController).isNotNull();
  }


}

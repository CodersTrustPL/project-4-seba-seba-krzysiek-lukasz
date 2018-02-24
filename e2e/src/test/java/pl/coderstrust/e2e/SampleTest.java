package pl.coderstrust.e2e;


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class SampleTest {
//
//  private int port;
//
//  @Test
//  public void sampleTest(){
//
//    RestAssured.baseURI ="http://localhost:"+"/invoice";
//    RequestSpecification request = given();
//
//    String invoice = "{\"invoiceName\":\"idVisible_111\",\"buyer\":{\"name\":\"buyer_name_0\",\"address\""
//        + ":\"buyer_address_0\",\"city\":\"buyer_city_0\",\"zipCode\":\"buyer_zipCode_0\""
//        + ",\"nip\":\"buyer_nip_0\",\"bankAccoutNumber\":\"buyer_bankAccoutNumber_0\"},"
//        + "\"seller\":{\"name\":\"seller_name_0\",\"address\":\"seller_address_0\","
//        + "\"city\":\"seller_city_0\",\"zipCode\":\"seller_zipCode_0\",\"nip\":\"seller_nip_0\""
//        + ",\"bankAccoutNumber\":\"seller_bankAccoutNumber_0\"},\"issueDate\":\"2018-05-01\","
//        + "\"paymentDate\":\"2018-06-16\",\"products\":[{\"product\":{\"name\":\"name_0_0\","
//        + "\"description\":\"description_0_0\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\""
//        + ":0},{\"product\":{\"name\":\"name_0_1\",\"description\":\"description_0_1\","
//        + "\"netValue\":30,\"vatRate\":\"VAT_23\"},\"amount\":1},{\"product\""
//        + ":{\"name\":\"name_0_2\",\"description\":\"description_0_2\",\"netValue\":1,"
//        + "\"vatRate\":\"VAT_23\"},\"amount\":2}],\"paymentState\":\"NOT_PAID\"}";
//
//    Response r = given()
//        .contentType("application/json").
//            body(invoice).
//            when().
//            post("");
//
//    String body = r.getBody().asString();
//    System.out.println(body);
//
//  }

//}

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

public class SampleTest {
  @BeforeClass
  public static void setup() {
    String port = System.getProperty("server.port");
    if (port == null) {
      RestAssured.port = Integer.valueOf(8080);
    }
    else{
      RestAssured.port = Integer.valueOf(port);
    }


    String basePath = System.getProperty("server.base");
    if(basePath==null){
      basePath = "/invoice/";
    }
    RestAssured.basePath = basePath;

    String baseHost = System.getProperty("server.host");
    if(baseHost==null){
      baseHost = "http://localhost";
    }
    RestAssured.baseURI = baseHost;

  }

  @Test
  public void basicPingTest() {
    given().when().get("").then().statusCode(200);
  }

  @Test
  public void sampleTest(){


    String invoice = "{\"invoiceName\":\"idVisible_111\",\"buyer\":{\"name\":\"buyer_name_0\",\"address\""
        + ":\"buyer_address_0\",\"city\":\"buyer_city_0\",\"zipCode\":\"buyer_zipCode_0\""
        + ",\"nip\":\"buyer_nip_0\",\"bankAccoutNumber\":\"buyer_bankAccoutNumber_0\"},"
        + "\"seller\":{\"name\":\"seller_name_0\",\"address\":\"seller_address_0\","
        + "\"city\":\"seller_city_0\",\"zipCode\":\"seller_zipCode_0\",\"nip\":\"seller_nip_0\""
        + ",\"bankAccoutNumber\":\"seller_bankAccoutNumber_0\"},\"issueDate\":\"2018-05-01\","
        + "\"paymentDate\":\"2018-06-16\",\"products\":[{\"product\":{\"name\":\"name_0_0\","
        + "\"description\":\"description_0_0\",\"netValue\":1,\"vatRate\":\"VAT_23\"},\"amount\""
        + ":0},{\"product\":{\"name\":\"name_0_1\",\"description\":\"description_0_1\","
        + "\"netValue\":30,\"vatRate\":\"VAT_23\"},\"amount\":1},{\"product\""
        + ":{\"name\":\"name_0_2\",\"description\":\"description_0_2\",\"netValue\":1,"
        + "\"vatRate\":\"VAT_23\"},\"amount\":2}],\"paymentState\":\"NOT_PAID\"}";

    String s = given()
        .contentType("application/json")
        .body(invoice)
        .when().post("")
        .getBody().asString();

    System.out.println(s);

  }


}

package pl.coderstrust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);

  }
  //TODO create better examples for swagger (company example + invoice example should be linked)
  //TODO add spring context for tests with MOGNO to shorten test excecution time
}

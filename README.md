## Accouting-system
Accounting system with possibility to add invoices, calculate taxes, generate PDFs and send emails. There are multiple implementations of databases provided to exercise various concepts: sql, no-sql, custom file database.
The Project contains 8 various REST services, over 0xx test cases and xxx lines of code with 82% test coverage.


## Code style
[![js-standard-style](https://img.shields.io/badge/code%20style-Google_Style-brightgreen.svg?style=flat)](https://github.com/checkstyle/checkstyle)
 
## Screenshots
![Alt text](https://github.com/pio-kol/accouting-system/blob/master/src/main/resources/readme/swagger-screenshot.png)

## Tech/framework used

<b>Built with</b>
- [Maven](https://maven.apache.org/)
- [Spring](https://spring.io/)
- [spring boot](https://projects.spring.io/spring-boot/)
- [Swagger](https://swagger.io/)
- [Jacoco](https://www.eclemma.org/jacoco/)
- [Mockito](http://site.mockito.org/)
- [JUnit](https://maven.apache.org/)
- [JUnit Params](https://github.com/junit-team/junit4/wiki/parameterized-tests)
- [Json](https://www.json.org/)
- [Jackson](https://github.com/FasterXML/jackson)
- [PostgreSQL](https://www.postgresql.org/)
- [Hibernate](http://hibernate.org/)
- [MongoDB](https://www.mongodb.com/)
- [Lombok](https://projectlombok.org/)
- [REST Assured](http://rest-assured.io/)
- [GreenMail](http://www.icegreen.com/greenmail/)
- [iText](https://itextpdf.com/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Gradle](https://gradle.org/)

## Installation
- git clone https://github.com/pio-kol/accouting-system.git
- [invoice.xsd](https://github.com/pio-kol/accouting-system/blob/master/src/main/resources/invoice.xsd) Generate schema from *`src\main\resources\invoice.xsd`* for SOAP binding classes.

## API Reference
Start the application and open the URL for API Documentation http://localhost:8080/swagger-ui.html

## Tests
We have three different types of tests,
JUnit, integrations, and e2e tests.
For e2e tests, you have to first build project's gradle file then enable annotation processing for lombok.
After that u need to start the main application and run e2e test as TestNG.

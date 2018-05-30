## Accouting-system
Accounting system with possibility to add invoices, calculate taxes, generate PDFs and send emails. There are multiple implementations of databases provided to exercise various concepts: sql, no-sql, custom file database.
The Project contains 8 various REST services, over 295 test cases and over 27618 lines of code with 80% test coverage.

## Code style
[![js-standard-style](https://img.shields.io/badge/code%20style-Google_Style-brightgreen.svg?style=flat)](https://github.com/checkstyle/checkstyle)

## Tech/framework used

<b>Built with</b>
- [Maven](https://maven.apache.org/)
- [Spring](https://spring.io/)
- [Spring boot](https://projects.spring.io/spring-boot/)
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
- Using JDK 1.8 is recomended. Project was test on this JDK version.<br/>
**1)** Open project with your IDE eg. IntellJI, Eclipse<br/>
**2)** Set checkstyle to [google-checkstyle](https://github.com/pio-kol/accouting-system/blob/master/checkstyle-config/intellij-java-google-style.xml)<br/>
**3)** Generate binding classes from `src\main\resources\invoice.xsd`.<br/>
Run Maven-compile or use command 'xjc -d src -p com.example.xjc example.xsd' in terminal <br/>
**4)** You can run Maven-verify to check, if project builds correctly.<br/>

## Setup Database
In application you can choose between six databases:
- InFile
- Multifile
- Mongo
- Mongo embedded
- SQL (JDBC)
- SQL (Hibernate)
- and InMemmory database (set by default).

  ```
  private static final String IN_FILE = "inFile";
  private static final String MULTIFILE = "multifile";
  private static final String MONGO = "mongo";
  private static final String MONGO_EMB = "mongo_emb";
  private static final String SQL_DB = "sql_db";
  ```
 You can change the database in 'application.properties` file
 ```
pl.coderstrust.database.MasterDatabase=mongo_emb
pl.coderstrust.database.FilterDatabase=mongo_emb
```

## API Reference
Start the application and open the URL for API Documentation http://localhost:8080/swagger-ui.html
![Swagger API](https://github.com/pio-kol/accouting-system/blob/master/readme/swagger-screenshot.png)

## Tests
We have three different types of tests : JUnit, integrations, and E2E tests.<br/>
To run e2e tests :<br/>
**1)** Run main application<br/>
**2)** Build E2E project from gradle build file ( /e2e/build.gradle ) as separate project. <br/>
**3)** Enable annotation processing for lombok. <br/>
![Enable annotations](https://github.com/pio-kol/accouting-system/blob/master/readme/annotatnion.png)
**4)** Run e2e tests as TestNG.<br/>

Acutator:
http://localhost:8080/actuator

HAL:
http://localhost:8080

H2 database console
http://localhost:8080/h2-console

Create and Run MySQL Docker Container<br>
```bash
docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword \
--env MYSQL_USER=social-media-user \
--env MYSQL_PASSWORD=dummypassword \
--env MYSQL_DATABASE=social-media-database \
--name mysql --publish 3306:3306 mysql:8-oracle
```

Start MySQL<br>
```bash
docker start mysql
```

Stop MySQL<br>
```<bash>
docker stop mysql
```


mysqlsh commands<br>
```bash
mysqlsh
\connect social-media-user@localhost:3306
\sql
use social-media-database
select * from user_details;
select * from post;
\quit
```

/pom.xml Modified<br>
```xml
<!-- Use this for Spring Boot 3.1 and higher -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency> 
 
<!-- Use this if you are using Spring Boot 3.0 or lower
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency> 
-->
```

/src/main/resources/application.properties<br>
```bash
#spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.show-sql=true
spring.datasource.url=jdbc:mysql://localhost:3306/social-media-database
spring.datasource.username=social-media-user
spring.datasource.password=dummypassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
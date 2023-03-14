package com.afulvio.booklify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class BooklifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooklifyApplication.class, args);
    }

}

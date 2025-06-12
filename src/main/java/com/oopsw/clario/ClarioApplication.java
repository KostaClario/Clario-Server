package com.oopsw.clario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ClarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClarioApplication.class, args);
    }

}

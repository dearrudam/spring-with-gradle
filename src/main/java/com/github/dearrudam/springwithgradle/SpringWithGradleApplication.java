package com.github.dearrudam.springwithgradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringWithGradleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWithGradleApplication.class, args);
    }

}

@RestController
@RequestMapping(path = "/hello")
class HelloController {

    @GetMapping(produces = { MediaType.TEXT_PLAIN_VALUE })
    public String hello(@RequestParam("name") String name) {
        return String.format("Hello, %s",name);
    }

}
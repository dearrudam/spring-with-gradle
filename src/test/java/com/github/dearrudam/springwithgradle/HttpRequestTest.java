package com.github.dearrudam.springwithgradle;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void helloShouldReturnDefaultMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/hello?name=Max",
                String.class)).contains("Hello, Max");

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/hello",
				String.class)).contains("Hello, unknown");
    }
    

	@Test
	public void byeShouldReturnDefaultMessage() throws Exception {

		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/bye?name=Max",
				String.class)).contains("Bye, Max");
    
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/bye",
				String.class)).contains("Bye, unknown");
    
    }
}
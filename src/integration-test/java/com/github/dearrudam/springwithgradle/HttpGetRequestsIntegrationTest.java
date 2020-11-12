package com.github.dearrudam.springwithgradle;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpGetRequestsIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @DisplayName("testing -> GET requests")
  @ParameterizedTest(name = "When a GET \"{0}\" request with \"{1}\" as " +
    "parameters performs then it must return HTTP \"{3}\" with \"{2}\" as the" +
    " response body")
  @MethodSource("testsArguments")
  void tests(final String path,
             final Map<String, String> params,
             final String responseBody,
             final int httpStatusCode) {

    UriComponentsBuilder targetUri =
      UriComponentsBuilder.fromHttpUrl("http://localhost:" + port).path(path);

    targetUri = targetUri.queryParams(new LinkedMultiValueMap<>(
      params.entrySet().stream()
        .map(entry -> Map.of(entry.getKey(), List.of(entry.getValue())))
        .reduce((stringListEntry, stringListEntry2) -> {
          Map<String, List<String>> result =
            new HashMap<>(stringListEntry);
          result.putAll(stringListEntry2);
          return result;
        }).orElseGet(HashMap::new)
    ));

    ResponseEntity<String> response = this.restTemplate
      .getForEntity(targetUri.build().toUriString(),
        String.class);

    assertThat(response.getStatusCodeValue()).isEqualTo(httpStatusCode);
    assertThat(response.getBody()).isEqualTo(responseBody);

  }

  static Stream<Arguments> testsArguments() {
    return Stream.of(
      Arguments.of(
        "hello",
        Map.of("name", "Max"),
        "Hello, Max",
        HttpStatus.OK.value()
      ),
      Arguments.of(
        "hello",
        Map.of(),
        "Hello, unknown",
        HttpStatus.OK.value()
      ),
      Arguments
        .of(
          "bye",
          Map.of("name", "Max"),
          "Bye, Max",
          HttpStatus.OK.value()
        ),
      Arguments.of(
        "bye",
        Map.of(),
        "Bye, unknown",
        HttpStatus.OK.value()
      )
    );
  }

}

package example.restapi.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import example.restapi.payload.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    private final TestRestTemplate testRestTemplate;

    @Autowired
    public AuthControllerTest(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }

    @Test
    void shouldReturnAppUserDtoWhenUserIsCreated(){
        var signupRequest = new SignupRequest("a@gmail.com","password");
        ResponseEntity<String> createResponse = testRestTemplate.postForEntity("/uni-api/auth/signup",signupRequest,String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(createResponse.getBody());
        String username  = documentContext.read("$.username");
        assertThat(username).isEqualTo("a@gmail.com");
    }

    @Test
    void shouldReturnForbiddenWhenAnyOfRequestValueIsNotValid(){
        var signupRequest = new SignupRequest("dc","");
        ResponseEntity<String> createResponse = testRestTemplate.postForEntity("/uni-api/auth/signup",signupRequest,String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}

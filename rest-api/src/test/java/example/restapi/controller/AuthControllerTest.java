package example.restapi.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import example.restapi.payload.LoginCredentials;
import example.restapi.payload.SignupRequest;
import example.restapi.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>Controller test case class {@code AuthControllerTest} for {@link example.restapi.controllers.AuthController} using {@link SpringBootTest}
 * with {@link SpringBootTest.WebEnvironment#RANDOM_PORT}</p>
 * <p></p>
 * <p><b>Signup Handler Test :</b><ul>
 *     <li>{@link AuthControllerTest#shouldReturnAppUserDtoWhenUserIsCreatedOrConflictIfAlreadyExist()}</li>
 *     <li>{@link AuthControllerTest#shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_SignUpParam()}</li>
 * </ul></p>
 *
 * <p><b>Login Handler Test :</b><ul>
 *     <li>{@link AuthControllerTest#shouldReturnAppUserDTOWithToken()}</li>
 *     <li>{@link AuthControllerTest#shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_LoginParam()}</li>
 * </ul></p>
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    private final String REQUEST_MAPPING_VALUE = "/uni-api/auth";

    private final TestRestTemplate testRestTemplate;

    @Autowired
    public AuthControllerTest(TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
    }
    /**
     * Test case {@code shouldReturnAppUserDtoWhenUserIsCreatedOrConflictIfAlreadyExist}, in the process
     * to signup -> create a new {@link example.restapi.entity.AppUser @AppUserEntity} we should validate
     * data structure in the entry point with {@link AuthControllerTest#shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_SignUpParam()}.
     * <p></p>
     * <p><b>Params:</b><ul>
     *     <li>{@link TestRestTemplate}</li>
     *     <li>{@link SignupRequest}</li>
     *     <li>{@link org.springframework.web.bind.annotation.PostMapping @PostMapping}    <i>("/uni-api/auth/signup")</i></li>
     * </ul></p>
     * <p><b>Two scenario to take into consideration</b>:<ol>
     *     <li>New User signup option</li>
     *     In case of new user registration a new {@link example.restapi.entity.AppUser @AppUserEntity} is created.
     *     Action status code {@link HttpStatus#OK @200_OK}.Verification of user information {@link example.restapi.dto.AppUserDTO#username @AppUserDTO.username} after creation is added.
     *     <li>User already exist in db</li>
     *     In case of a user try to register with an old {@link example.restapi.entity.AppUser#username @AppUserEntity.username},already exist unique key,
     *     a conflict occur in this action. Status code {@link HttpStatus#CONFLICT @409_CONFLICT}
     * </ol></p>
     *
     * @return {@link HttpStatus#OK} or {@link HttpStatus#CONFLICT}
     */
    @Test
    void shouldReturnAppUserDtoWhenUserIsCreatedOrConflictIfAlreadyExist() {
        var signupRequest = SignupRequest.builder().username("al@gmail.com").password("password").build();
        ResponseEntity<String> response = testRestTemplate.postForEntity(REQUEST_MAPPING_VALUE +"/signup", signupRequest, String.class);
        //condition of request is created -> status code = 200 ok
        if (response.getStatusCode() == HttpStatus.OK) {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            //verifies returned data from request
            DocumentContext documentContext = JsonPath.parse(response.getBody());
            String username = documentContext.read("$.username");
            assertThat(username).isEqualTo("al@gmail.com");
        } else {
            //condition_2 this user already exist -> status code = 409 conflict
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    /**
     * Test case {@code shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid}
     * verifies validation rule added to payload {@link SignupRequest}.
     * <p></p>
     * <p><b>Params:</b><ul>
     *     <li>{@link TestRestTemplate}</li>
     *     <li>{@link SignupRequest}</li>
     *     <li>{@link org.springframework.web.bind.annotation.PostMapping @PostMapping}    <i>("/uni-api/auth/signup")</i></li>
     * </ul></p>
     * <p><b>Tow Scenario to take into consideration:</b><ol>
     *     <li>Validation of username structure {@link SignupRequest#username} -> {@link jakarta.validation.constraints.Email @Email}</li>
     *     <code>var signupRequest = new SignupRequest("myFirstInvalidEmail","123456789");</code>
     *     <li>Validation of password value {@link  SignupRequest#password} -> {@link  jakarta.validation.constraints.NotNull @NotNull} and  {@link jakarta.validation.constraints.NotBlank @NotBlank}</li>
     *     <code>var signupRequest = new SignupRequest("myFirstEmail@domain.com","");</code>
     *</ol></p>
     *
     * @return {@link HttpStatus#UNAUTHORIZED}
     */
    @Test
    void shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_SignUpParam(){
        var signupRequest = SignupRequest.builder().username("dc").password("123456789").build();
        ResponseEntity<String> createResponse = testRestTemplate.postForEntity(REQUEST_MAPPING_VALUE+"/signup",signupRequest,String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }
    /**
     * Test case {@code shouldReturnAppUserDTOWithToken},login scenario verifies user info {@link LoginCredentials} by validation
     * the in coming request {@link AuthControllerTest#shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_LoginParam()}.
     * Then verifies username and password.
     * <p></p>
     * <p><b>Params:</b><ul>
     *     <li>{@link TestRestTemplate}</li>
     *     <li>{@link LoginCredentials}</li>
     *     <li>{@link org.springframework.web.bind.annotation.PostMapping @PostMapping}    <i>("/uni-api/auth/login")</i></li>
     * </ul></p>
     * <p><b>Two scenario to take into consideration</b>:<ol>
     *     <li>All user info ar correct</li>
     *     In case of all user information are correct user info {@link example.restapi.dto.AppUserDTO @AppUserDTO} return in the response body,
     *     and a new token added to the header under the name of {@link example.restapi.utils.Constants#SECURITY_ATTRIBUTE_TOKEN @SECURITY_ATTRIBUTE_TOKEN}.
     *     Status code {@link HttpStatus#OK @200_OK}.
     *     <li>User already exist in db</li>
     *     In case of a problem with username {@link LoginCredentials#username} not exist, or password {@link LoginCredentials#password} not correct base
     *     on argon2 algo {@link org.springframework.security.crypto.argon2.Argon2PasswordEncoder @Argon2PasswordEncoder}.
     *     Status code {@link HttpStatus#UNAUTHORIZED @401_UNAUTHORIZED}
     * </ol></p>
     *
     * @return {@link HttpStatus#OK} or {@link HttpStatus#UNAUTHORIZED}
     */
    @Test
    void shouldReturnAppUserDTOWithToken(){
        var loginCredentials = new LoginCredentials("al@gmail.com","password");
        ResponseEntity<String> loginResponse = testRestTemplate.postForEntity(REQUEST_MAPPING_VALUE+"/login",loginCredentials,String.class);
        //condition of login is done -> status code = 200 ok
        if(loginResponse.getStatusCode() == HttpStatus.OK){
            assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            //verifies body data {user info}
            DocumentContext documentContext = JsonPath.parse(loginResponse.getBody());
            String username = documentContext.read("$.username");
            assertThat(username).isEqualTo("al@gmail.com");
            //verifies header data {token exist}
            String token = loginResponse.getHeaders().getFirst(Constants.SECURITY_ATTRIBUTE_TOKEN);
            assertThat(token).isNotEmpty();
        }else{
            // condition of user don't exist or password is not valid base on argon2 encoder
            // status code = 401 unauthorized
            assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Test case {@code shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_LoginParam}
     * verifies validation rule added to payload {@link LoginCredentials}.
     * <p></p>
     * <p><b>Params:</b><ul>
     *     <li>{@link TestRestTemplate}</li>
     *     <li>{@link LoginCredentials}</li>
     *     <li>{@link org.springframework.web.bind.annotation.PostMapping @PostMapping}    <i>("/uni-api/auth/login")</i></li>
     * </ul></p>
     * <p><b>Tow Scenario to take into consideration:</b><ol>
     *     <li>Validation of username structure {@link LoginCredentials#username} -> {@link jakarta.validation.constraints.Email @Email}</li>
     *     <code>var loginCredentials = new LoginCredentials("myFirstInvalidEmail","123456789");</code>
     *     <li>Validation of password value {@link  LoginCredentials#password} -> {@link  jakarta.validation.constraints.NotNull @NotNull} and  {@link jakarta.validation.constraints.NotBlank @NotBlank}</li>
     *     <code>var loginCredentials = new LoginCredentials("myFirstEmail@domain.com","");</code>
     *</ol></p>
     *
     * @return {@link HttpStatus#UNAUTHORIZED}
     */
    @Test
    void shouldReturnUnauthorizedWhenAnyOfRequestValueIsNotValid_LoginParam(){
        var loginCredentials = new LoginCredentials("dc","123456789");
        ResponseEntity<String> loginResponse = testRestTemplate.postForEntity(REQUEST_MAPPING_VALUE+"/login",loginCredentials,String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

}

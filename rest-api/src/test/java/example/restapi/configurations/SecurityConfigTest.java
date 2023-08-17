package example.restapi.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.restapi.payload.SignupRequest;
import example.restapi.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Security test case class {@code SecurityConfigTest} for {@link example.restapi.configurations.SecurityConfig @SecurityConfig} using
 * {@link SpringBootTest} with {@link org.springframework.boot.test.context.SpringBootTest.WebEnvironment#RANDOM_PORT @WebEnvireoment.RANDOM_PORT}
 * and {@link AutoConfigureMockMvc}
 * <p></p>
 * <p><b>Test Case:</b><ul>
 *      <li>{@link SecurityConfigTest#shouldPublicEndpointsBeAccessible()}</li>
 *      <li>{@link SecurityConfigTest#shouldPrivateEndpointsBeAuthenticateWithToken()}</li>
 * </ul></p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SecurityConfigTest {

    private final String REQUEST_MAPPING_VALUE = "/uni-api";

    private final MockMvc mockMvc;

    @Autowired
    public SecurityConfigTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    /**
     * Test case {@code shouldPublicEndpointsBeAccessible} verifies that the public
     * endpoint is accessible by client without any authentication.
     * <p></p>
     * <p><b>Public Endpoint:</b><ul>
     *     <li>{@link example.restapi.controllers.AuthController @AuthController} <i>/uni-api/auth/**</i></li>
     * </ul></p>
     */
    @Test
    void shouldPublicEndpointsBeAccessible() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post(REQUEST_MAPPING_VALUE+"/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new SignupRequest("malek@gmail.com","123456"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("malek@gmail.com"))
                .andExpect(header().exists(Constants.SECURITY_ATTRIBUTE_TOKEN));
    }

    /**
     * Test case {@code shouldPrivateEndpointsBeAuthenticateWithToken} verifies that the secure
     * endpoint is not accessible without authentication process {@link example.restapi.utils.jwt.JWTFilter @JWTFilter}
     *
     */
    @Test
    void shouldPrivateEndpointsBeAuthenticateWithToken() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_MAPPING_VALUE+"/Hello")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.SECURITY_ATTRIBUTE_AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJhcHBVc2VySWQiOiJiY2JhYjZjOC1kMTU5LTQ0ZWEtYjFmYy05NDIzOGE0YTI2N2EiLCJ1c2VybmFtZSI6ImFsaUBnbWFpbC5jb20iLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE2OTIyNzM1MDcsImV4cCI6MTcwODE4MTkwNywiaXNzIjoic3ByaW5nLWRlbW8vcmVzdC1hcGkvdW5pLWFwaSJ9.ePXpGHzHRG4OnaSyu0NsSOzzJicavY-yG_303Mi-T1k"))
                .andExpect(status().isOk());

    }


}

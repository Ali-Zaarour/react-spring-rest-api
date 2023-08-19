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
                        .content(new ObjectMapper().writeValueAsString(SignupRequest.builder().username("Y@gmail.com").password("123456789").build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Y@gmail.com"))
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
                .header(Constants.SECURITY_ATTRIBUTE_AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJ1c2VybmFtZSI6IllAZ21haWwuY29tIiwiaWF0IjoxNjkyNDgxMzQyLCJleHAiOjE3MDgzODk3NDIsImlzcyI6InNwcmluZy1kZW1vL3Jlc3QtYXBpL3VuaS1hcGkifQ.2w_RhDQ047Exc4_u-FtZSw2WvnaRKO__WdhshYxX7Yc"))
                .andExpect(status().isOk());

    }


}

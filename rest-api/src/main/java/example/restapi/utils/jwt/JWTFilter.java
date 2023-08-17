package example.restapi.utils.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import example.restapi.model.AuthenticationUserInformation;
import example.restapi.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extracting the "token" header
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Optional.ofNullable(requestTokenHeader).isEmpty() || !requestTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Verify token and extract user info
           var UserInfo = jwtUtil.validateJWTTokenAndRetrieveSubject(requestTokenHeader.substring(7));

            //set auth info
            var authenticationUserInformation=  AuthenticationUserInformation.builder()
                    .id(UUID.fromString((String) UserInfo.get(Constants.JWT_CLAIM_APP_USER_ID)))
                    .username((String) UserInfo.get(Constants.JWT_CLAIM_USERNAME))
                    .role((String) UserInfo.get(Constants.JWT_CLAIM_ROLE))
                    .build();

            // Setting the authentication on the Security Context using the created token
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(authenticationUserInformation,null,new ArrayList<>()));

        }catch (JWTVerificationException exc) {
            // Failed to verify JWT
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
        }

        // Continuing the execution of the filter chain
        filterChain.doFilter(request, response);
    }
}

package example.restapi.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uni-api")
@SecurityRequirement(name = "bearerAuth")
@Hidden
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TestController {

    @GetMapping("/Hello")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<String> hello(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken data){
            return ResponseEntity.ok(""+data.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        }
        return ResponseEntity.ok("Hello World");
    }

}
